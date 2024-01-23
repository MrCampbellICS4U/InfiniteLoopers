package server;

import java.net.Socket;
import java.util.*;

import world.Tiles.Tile;
import world.Tiles.AirTile;
import shared.*;
import packets.*;
import client.*;
import entities.*;
import collision.*;

// clients from the server's perspective
class SClient extends Circle implements Entity {
	private double lastTileUpdateX, lastTileUpdateY;
	private Tile[][][] visibleTiles;
	private Tile[][][] oldVisibleTiles;
	private GlobalConstants gc;

	/**
	 * Returns the array of visible tiles.
	 *
	 * @return The array of visible tiles.
	 */
	public Tile[][][] getVisibleTiles() {
		return this.visibleTiles;
	}

	/**
	 * Sets the visible tiles for the current entity.
	 *
	 * @param tiles The 3D array of tiles to set as visible tiles
	 * @return The updated visible tiles array
	 */
	public Tile[][][] setVisibleTiles(Tile[][][] tiles) {
		return this.visibleTiles = tiles;
	}

	/**
	 * Calculates the visible tiles based on the current position of the player.
	 *
	 * @param map The 3D array representing the entire map of tiles.
	 * @return A 3D array of visible tiles.
	 */
	public Tile[][][] calculateVisibleTiles(Tile[][][] map) {
		Tile[][][] visibleTiles = new Tile[gc.DRAWING_AREA_WIDTH / gc.TILE_WIDTH
				+ 2 * gc.TILE_X_BUFFER][gc.DRAWING_AREA_HEIGHT / gc.TILE_HEIGHT
						+ 2 * gc.TILE_Y_BUFFER][3];
		int x = (int) getX();
		int y = (int) getY();

		// calculating the top left corner of the visible tiles base on the screen size
		// 13 tiles wide and 8 tiles tall but we have a buffer of 1 tile in each
		// direction
		// converting the pixel location to tile location and round to nearest tile size
		int topLeftX = (int) (x - gc.DRAWING_AREA_WIDTH / 2) / gc.TILE_WIDTH
				- gc.TILE_X_BUFFER;
		int topLeftY = (int) (y - gc.DRAWING_AREA_HEIGHT / 2) / gc.TILE_HEIGHT
				- gc.TILE_Y_BUFFER;

		// loop through the visible tiles and set them to the corresponding tiles in the
		// map
		for (int x1 = 0; x1 < visibleTiles.length; x1++) {
			for (int y1 = 0; y1 < visibleTiles[0].length; y1++) {
				for (int z = 0; z < visibleTiles[0][0].length; z++) {
					int xIndex = topLeftX + x1;
					int yIndex = topLeftY + y1;
					if (xIndex < 0 || gc.WORLD_TILE_WIDTH <= xIndex || yIndex < 0
							|| gc.WORLD_TILE_HEIGHT <= yIndex) {
						// tile is out of bounds of the world, send an air tile
						visibleTiles[x1][y1][z] = new AirTile(xIndex, yIndex, z, 0, "default");
					} else
						visibleTiles[x1][y1][z] = map[xIndex][yIndex][z];
				}
			}
		}
		return visibleTiles;
	}

	/**
	 * Handles updates to the visible tiles on the map.
	 *
	 * @param map The 3D array representing the map tiles.
	 */
	public void handleVisibleTileUpdates(Tile[][][] map) { // sends the client its new visible tiles if anything has
															// changed, ie their location >= 1 tile away from the last
															// update or if any tile needs updating
		if (visibleTiles == null) { // this happens on the first update
			setVisibleTiles(calculateVisibleTiles(map));
			send(new SendFullClientFOV(visibleTiles));
			return;
		}
		Set<Tile> oldVisibleTiles = new HashSet<>(ConvertToArrayList.convert(getVisibleTiles()));
		Set<Tile> newVisibleTiles = new HashSet<>(
				ConvertToArrayList.convert(setVisibleTiles(calculateVisibleTiles(map))));

		newVisibleTiles.removeAll(oldVisibleTiles);
		Set<Tile> tilesToSend = new HashSet<>(newVisibleTiles);

		if (tilesToSend.size() > 0) {
			send(new PartialFOVUpdate(new ArrayList<>(tilesToSend)));
			return;
		}
		return;

	}

	private double angle;

	public double setAngle(double angle) {
		return this.angle = angle;
	}

	/**
	 * Retrieves the information of the player.
	 *
	 * @return The PlayerInfo object containing the player's information.
	 */
	public PlayerInfo getInfo() {
		return new PlayerInfo((int) getX(), (int) getY(), id, (int) getRadius(), angle, health, armor, new String[0],
				name, kills);
	}

	private int health;
	private int kills = 0;
	public String hotBar[];
	private int armor = 0;

	PacketLord<Server> pl;
	private final int id;

	public int getID() {
		return id;
	}

	/**
	 * Sends a packet to the client.
	 *
	 * @param p The packet to send.
	 */
	public void send(PacketTo<Client> p) {
		pl.send(p);
	}

	private boolean shouldRemove = false;

	/**
	 * Removes the entity by closing the associated resource and marking it for
	 * removal.
	 * Used for when a player loses/dies
	 */
	public void remove() {
		pl.close();
		shouldRemove = true;
	}

	public boolean shouldRemove() {
		return shouldRemove;
	}

	Tile[][][] map;
	private Server server;
	private Chunker chunker;
	private long nextShot; // the time of the soonest next shot

	/**
	 * Constructs a new SClient object with the given parameters.
	 *
	 * @param socket The socket associated with the client connection.
	 * @param server The server instance.
	 * @param id     The unique identifier for the client.
	 * @param c      The Chunker object for handling chunks.
	 * @param map    The 3D array representing the game map.
	 * @param gc     The GlobalConstants object containing global game constants.
	 */
	SClient(Socket socket, Server server, int id, Chunker c, Tile[][][] map, GlobalConstants gc) {
		super((int) (Math.random() * gc.WORLD_WIDTH),
				(int) (Math.random() * gc.WORLD_HEIGHT), 25, c);

		this.gc = gc;

		this.health = gc.MAX_HEALTH;
		this.hotBar = new String[gc.MAX_HOTBAR];

		this.id = id;
		pl = new PacketLord<Server>(socket, server);
		pl.setID(id);
		this.map = map;
		this.server = server;
		server.addEntity(this);
		this.chunker = c;
	}

	// is the client ready to receive messages?
	// had a weird issue where i had to wait a bit before sending a ton of messages
	// to the client;
	// it would throw a `UTFDataFormatException` if i immediately started sending
	// messages after opening the socket
	private boolean ready = false;
	String name = "";

	public void setReady(String name) {
		ready = true;
		this.name = name;
	}

	private boolean up, down, left, right;

	/**
	 * Handles the input based on the given input and input state.
	 *
	 * @param i  The input to handle.
	 * @param is The state of the input (DOWN or UP).
	 */
	public void handleInput(Input i, InputState is) {
		boolean isDown = is == InputState.DOWN;
		switch (i) {
			case UP -> up = isDown;
			case DOWN -> down = isDown;
			case LEFT -> left = isDown;
			case RIGHT -> right = isDown;

			case ATTACK -> attack();
			case RELOAD -> reload();
			case USE -> useItem();
			case DROP -> dropItem();
			case SUICIDE -> {
				if (gc.CAN_SUICIDE)
					kysURSELF();
			}

		}
	}

	int counter = 0;

	/**
	 * Suicide Button
	 */
	private void kysURSELF() {
		health = 0;
		counter++;
		if (counter > 10) {
			pl.close();
		}
	}

	final int gunLength = 35;

	/**
	 * Initiates an attack by creating a new bullet if the specified time has
	 * elapsed since the last shot. The bullet is created at the calculated position
	 * based on the current angle and gun length
	 * 
	 * @param gc The game controller object
	 */
	private void attack() {
		long time = System.currentTimeMillis();
		if (time > nextShot) {
			new Bullet(getX() + (Math.cos(angle) * gunLength), getY() + (Math.sin(angle) * gunLength), 6, angle,
					gc.BULLET_SPEED, id, chunker, server, this.gc);
			nextShot = time + gc.SHOT_DELAY;
		}
	}

	/**
	 * Reloads the client.
	 * 
	 * This method is called when the client with the specified ID attempts to
	 * reload.
	 * It prints a message indicating the client ID that is attempting to reload.
	 *
	 * @param id The ID of the client attempting to reload.
	 */
	private void reload() {
		System.out.printf("Client %d attempts to reload\n", id);
	}

	private void useItem() {
		System.out.printf("Client %d tries to use an item\n", id);
	}

	private void dropItem() {
		System.out.printf("Client %d drops something on the ground\n", id);
	}

	/**
	 * Handles a shot event for a player.
	 *
	 * If the player has armor, their armor is reduced by 1. If the player does not
	 * have armor,
	 * their health is reduced by 1. If the player's health reaches 0, they are
	 * considered dead.
	 * If the shooter is a valid client in the server, they are rewarded for the
	 * kill.
	 *
	 * @param shooterID The ID of the player who shot the current player
	 */
	public void getShot(int shooterID) {
		if (armor > 0)
			armor--;
		else
			health--;

		if (health == 0) {
			// make sure the client still exists
			// they could have disconnected or died
			if (server.existsClient(shooterID)) {
				server.getClient(shooterID).rewardForKill();
			}
		}
	}

	/**
	 * Rewards the player for a kill by increasing their armor and kill count.
	 * If the player's armor is less than the maximum armor, it is increased by 1.
	 * The kill count is incremented by 1.
	 */
	public void rewardForKill() {
		// good job, you got a kill!
		if (armor < gc.MAX_ARMOR) {
			armor++;
		}
		kills++;
	}

	private final double targetSpeed = 2;

	private boolean waitingToRegen = false;
	private boolean inWater = false;
	private double oldX, oldY;

	/**
	 * Updates the state of the entity based on the elapsed time.
	 *
	 * @param deltaTime The time elapsed since the last update.
	 */
	public void update(double deltaTime) {
		if (shouldRemove())
			return;

		double dx = 0, dy = 0;
		double speed = targetSpeed * deltaTime;
		if (inWater)
			speed *= 0.6;
		if (up)
			dy -= speed;
		if (down)
			dy += speed;
		if (left)
			dx -= speed;
		if (right)
			dx += speed;
		if (dx != 0 && dy != 0) {
			dx /= Math.sqrt(2);
			dy /= Math.sqrt(2);
		}
		if (health <= 0) {
			kysURSELF();
		} else if (health < gc.MAX_HEALTH && health > 0 && !waitingToRegen) {
			checkHealth();
			waitingToRegen = true;
		}

		double newX = getX() + dx;
		double newY = getY() + dy;

		newX = Math.max(0, Math.min(newX, gc.WORLD_WIDTH));
		newY = Math.max(0, Math.min(newY, gc.WORLD_HEIGHT));

		oldX = getX();
		oldY = getY();
		setPosition(newX, newY);

		if (Math.abs(newX - lastTileUpdateX) >= gc.TILE_WIDTH
				|| Math.abs(newY - lastTileUpdateY) >= gc.TILE_HEIGHT) { // if the player has moved at
																			// least 1 tile
			// away from the last update
			lastTileUpdateX = newX;
			lastTileUpdateY = newY;
			handleVisibleTileUpdates(map);
		}

		inWater = false; // if we're still in water, will be set to true again
	}

	/**
	 * Checks the health of an entity and schedules a timer task to increment the
	 * health value.
	 * The timer task runs after a specified regeneration time.
	 */
	public void checkHealth() {
		Timer timer = new Timer();

		// set a timer to wait REGEN_TIME, then regen
		timer.schedule(new TimerTask() {
			public void run() {
				health++;
				waitingToRegen = false;
			}
		}, gc.REGEN_TIME);
	}

	public void sendPackets() {
		if (!ready || shouldRemove())
			return;

		send(new EntitiesPacket(entities));
	}

	// all the entities, including self, you can see
	private ArrayList<EntityInfo> entities = new ArrayList<>();

	/**
	 * Clears the list of entities.
	 */
	public void clearEntities() {
		entities = new ArrayList<>(); // DO NOT CHANGE THIS TO CLEAR, IT BREAKS AND I DO NOT KNOW WHY
	}

	public void addEntity(EntityInfo e) {
		entities.add(e);
	}

	/**
	 * Performs a collision with the given hitbox.
	 * If the hitbox is a WaterHitbox, sets the inWater flag to true.
	 * If the hitbox is a WallHitbox, calls the hitCrate method with the WallHitbox
	 * as parameter.
	 *
	 * @param h The hitbox to collide with
	 */
	public void smashInto(Hitbox h) {
		if (h instanceof WaterHitbox) {
			inWater = true;
		}
		if (h instanceof WallHitbox)
			hitCrate((WallHitbox) h);
	}

	/**
	 * Moves the player to the closest position on the wall hitbox.
	 *
	 * @param c The wall hitbox to collide with.
	 */
	private void hitCrate(WallHitbox c) {
		// "pop the player out" of the crate
		// find the closest point on the border of the crate, and move them there
		double x1 = c.getX1() - getRadius(), x2 = c.getX2() + getRadius(),
				y1 = c.getY1() - getRadius(), y2 = c.getY2() + getRadius();
		double x1dist = Math.abs(getX() - x1), x2dist = Math.abs(getX() - x2),
				y1dist = Math.abs(getY() - y1), y2dist = Math.abs(getY() - y2);
		double closest = Math.min(x1dist, Math.min(x2dist, Math.min(y1dist, y2dist)));
		if (closest == x1dist)
			setPosition(x1, getY());
		else if (closest == x2dist)
			setPosition(x2, getY());
		else if (closest == y1dist)
			setPosition(getX(), y1);
		else if (closest == y2dist)
			setPosition(getX(), y2);
	}

	public Hitbox getHitbox() {
		return this;
	}

	public HitboxType getHitboxType() {
		return HitboxType.PLAYER;
	}
}
