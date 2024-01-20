package server;

import java.net.Socket;
import java.util.*;

import game.world.Tiles.Tile;
import game.world.Tiles.AirTile;
import shared.*;
import packets.*;
import client.*;
import entities.*;
import collision.*;
// clients from the server's perspective
class SClient extends Circle implements Entity {
	private double lastTileUpdateX, lastTileUpdateY;
	private Tile[][][] visibleTiles;

	public Tile[][][] getVisibleTiles() {
		return this.visibleTiles;
	}

	public Tile[][][] setVisibleTiles(Tile[][][] tiles) {
		return this.visibleTiles = tiles;
	}

	public Tile[][][] calculateVisibleTiles(Tile[][][] map) {
		Tile[][][] visibleTiles = new Tile[GlobalConstants.DRAWING_AREA_WIDTH / GlobalConstants.TILE_WIDTH
				+ 2 * GlobalConstants.TILE_X_BUFFER][GlobalConstants.DRAWING_AREA_HEIGHT / GlobalConstants.TILE_HEIGHT
						+ 2 * GlobalConstants.TILE_Y_BUFFER][3];
		int x = (int)getX();
		int y = (int)getY();

		// calculating the top left corner of the visible tiles base on the screen size
		// 13 tiles wide and 8 tiles tall but we have a buffer of 1 tile in each
		// direction
		// converting the pixel location to tile location and round to nearest tile size
		int topLeftX = (int) (x - GlobalConstants.DRAWING_AREA_WIDTH / 2) / GlobalConstants.TILE_WIDTH
				- GlobalConstants.TILE_X_BUFFER;
		int topLeftY = (int) (y - GlobalConstants.DRAWING_AREA_HEIGHT / 2) / GlobalConstants.TILE_HEIGHT
				- GlobalConstants.TILE_Y_BUFFER;

		// loop through the visible tiles and set them to the corresponding tiles in the
		// map
		for (int x1 = 0; x1 < visibleTiles.length; x1++) {
			for (int y1 = 0; y1 < visibleTiles[0].length; y1++) {
				for (int z = 0; z < visibleTiles[0][0].length; z++) {
					int xIndex = topLeftX + x1;
					int yIndex = topLeftY + y1;
					if (xIndex < 0 || GlobalConstants.WORLD_TILE_WIDTH <= xIndex || yIndex < 0
							|| GlobalConstants.WORLD_TILE_HEIGHT <= yIndex) {
						// tile is out of bounds of the world, send an air tile
						visibleTiles[x1][y1][z] = new AirTile(xIndex, yIndex, z, 0, "default");
					}
					else visibleTiles[x1][y1][z] = map[xIndex][yIndex][z];
				}
			}
		}
		return visibleTiles;
	}

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

	public double setAngle(double angle) { return this.angle = angle; }
	public double getAngle() { return angle; }

	public PlayerInfo getInfo() {
		return new PlayerInfo((int)getX(), (int)getY(), id, (int)getRadius(), angle, health, armor, new String[0]);
	}

	private final int MAXHOTBAR = GlobalConstants.MAXHOTBAR;
	private final int MAXHEALTH = GlobalConstants.MAXHEALTH;
	private int health = MAXHEALTH; // 3 hearts
	private final int MAXARMOR = 3;

	public String hotBar[] = new String[MAXHOTBAR];
	private int armor = 0;

	PacketLord<Server> pl;
	private final int id;
	public int getID() { return id; }

	public void send(PacketTo<Client> p) { pl.send(p); }

	private boolean shouldRemove = false;
	public void remove() { pl.close(); shouldRemove = true; }
	public boolean shouldRemove() { return shouldRemove; }

	Tile[][][] map;
	private Server server;
	private Chunker chunker;
	private long nextShot; // the time of the soonest next shot
	SClient(Socket socket, Server server, int id, Chunker c, Tile[][][] map) {
		super((int)(Math.random() * GlobalConstants.WORLD_WIDTH),
			(int)(Math.random() * GlobalConstants.WORLD_HEIGHT), 25, c);

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

	public void setReady() {
		ready = true;
	}

	private boolean up, down, left, right;

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
			case Dead -> kysURSELF();

		}
	}
	int counter = 0;
	private void kysURSELF(){
		health = 0;
		counter++;
		if(counter > 10){
			pl.close();
		}
	}

	final int gunLength = 35;
	private void attack() {
		long time = System.currentTimeMillis();
		if (time > nextShot) {
			new Bullet(getX() + (Math.cos(angle)*gunLength), getY() + (Math.sin(angle)*gunLength), 6, angle, 10, id, chunker, server);
			nextShot = time + 300; // 200 ms delay
		};
	}


	// todo implement
	private void reload() {
		System.out.printf("Client %d attempts to reload\n", id);
	}

	// todo implement
	private void useItem() {
		System.out.printf("Client %d tries to use an item\n", id);
	}

	// todo implement
	private void dropItem() {
		System.out.printf("Client %d drops something on the ground\n", id);
	}

	
	public void getShot() {
		health--;
	}

	private final double targetSpeed = 2;

	private boolean checking = false; // for regen and health or something
	private boolean inWater = false;
	private double oldX, oldY;

	public void update(double deltaTime) {
		if (shouldRemove()) return;

		double dx = 0, dy = 0;
		double speed = targetSpeed * deltaTime;
		if (inWater) speed *= 0.6;
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
		if (health <= 0){
			kysURSELF();
		}
		else if (health < 3 && health > 0 && !checking){
			checkHealth();
			checking = true;
		}

		double newX = getX() + dx;
		double newY = getY() + dy;

		newX = Math.max(0, Math.min(newX, GlobalConstants.WORLD_WIDTH));
		newY = Math.max(0, Math.min(newY, GlobalConstants.WORLD_HEIGHT));

		oldX = getX();
		oldY = getY();
		setPosition(newX, newY);

		if (Math.abs(newX - lastTileUpdateX) >= GlobalConstants.TILE_WIDTH
				|| Math.abs(newY - lastTileUpdateY) >= GlobalConstants.TILE_HEIGHT) { // if the player has moved at least 1 tile
																			// away from the last update
			lastTileUpdateX = newX;
			lastTileUpdateY = newY;
			handleVisibleTileUpdates(map);
		}

		inWater = false; // if we're still in water, will be set to true again
	}
	public void checkHealth(){
		Timer timer = new Timer();
		int seconds = 6;

		timer.schedule(new TimerTask() {
			int count = seconds;

			@Override
			public void run() {
				if (count == 0) {
					health++;
					checking = !checking;
					timer.cancel(); // Stop the timer
				}
				count--;
			}
		}, 0, 1000); // Run the task every 1000 milliseconds (1 second)
	}
	public void sendPackets() {
		if (!ready || shouldRemove())
			return;

		send(new EntitiesPacket(entities));
	}

	// all the entities, including self, you can see
	private ArrayList<EntityInfo> entities = new ArrayList<>();

	public void clearEntities() {
		entities = new ArrayList<>(); // DO NOT CHANGE THIS TO CLEAR, IT BREAKS AND I DO NOT KNOW WHY
	}

	public void addEntity(EntityInfo e) {
		entities.add(e);
	}

	public void smashInto(Hitbox h) {
		if (h instanceof WaterHitbox) {
			inWater = true;
		}
		if (h instanceof CrateHitbox) hitCrate((CrateHitbox)h);
	}

	// this assumes that we are newly intersecting the bounding box
	private void hitCrate(CrateHitbox c) {
		double x1 = c.getX1(), x2 = c.getX2(), y1 = c.getY1(), y2 = c.getY2();

		boolean canCollideWithLeft = getX() - oldX > 0;
		boolean canCollideWithRight = getX() - oldX < 0;
		boolean canCollideWithTop = getY() - oldY > 0;
		boolean canCollideWithBottom = getY() - oldY < 0;

		double dx = 0, dy = 0;
		if (canCollideWithTop && lineCollision(x1, y1, x2, y1)) {
			// hit the top side
			dy = (y1 - getRadius()) - getY();
		}
		if (canCollideWithBottom && lineCollision(x1, y2, x2, y2)) {
			// hit the bottom side
			dy = (y2 + getRadius()) - getY();
		}
		if (canCollideWithLeft && lineCollision(x1, y1, x1, y2)) {
			// hit the left side
			dx = (x1 - getRadius()) - getX();
		}
		if (canCollideWithRight && lineCollision(x2, y1, x2, y2)) {
			// hit the right side
			dx = (x2 + getRadius()) - getX();
		}
		setPosition(getX() + dx, getY() + dy);
	}

	public Hitbox getHitbox() { return this; }
}
