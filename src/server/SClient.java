package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import game.world.Tiles.Tile;
import game.world.Tiles.AirTile;
import shared.*;
import packets.*;
import client.Client;

// clients from the server's perspective
class SClient extends Circle {
	private double lastx, lasty;
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
		// converting the pixel location to tile location and round tonearest tile size
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

	public PlayerInfo getInfo() { return new PlayerInfo((int)getX(), (int)getY(), angle, health, armor, new String[0]); }

	public String hotBar[] = new String[3];
	private final int MAXHEALTH = 3;
	private int health = MAXHEALTH; // 3 hearts
	private final int MAXARMOR = 3;
	private final int MAXHOTBAR = 3;
	private int armor = 0;

	PacketLord<Server> pl;
	private final int id;
	public void send(PacketTo<Client> p) { pl.send(p); }
	public void remove() { pl.close(); super.remove(); }
	SClient(Socket socket, Server state, int id, Chunker c) {
		super(5000, 5000, 25, c);

		this.id = id;
		pl = new PacketLord<Server>(socket, state);
		pl.setID(id);
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
		}
	}

	// todo implement
	private void attack() {
		System.out.printf("Client %d unleashed a devastating attack!\n", id);
		//health--;
		//armor++;
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

	private final int speed = 5;

	public void updatePlayer(Tile[][][] map) {
		double dx = 0, dy = 0;
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

		double newX = getX() + dx;
		double newY = getY() + dy;

		newX = Math.max(0, Math.min(newX, GlobalConstants.WORLD_WIDTH));
		newY = Math.max(0, Math.min(newY, GlobalConstants.WORLD_HEIGHT));

		setPosition(newX, newY);

		if (Math.abs(newX - lastx) >= GlobalConstants.TILE_WIDTH
				|| Math.abs(newY - lasty) >= GlobalConstants.TILE_HEIGHT) { // if the player has moved at least 1 tile
																			// away from the last update
			lastx = newX;
			lasty = newY;
			handleVisibleTileUpdates(map);
		}
	}

	public void sendPackets() {
		if (!ready)
			return;

		send(new MePacket(getInfo()));
		send(new OtherPlayersPacket(otherPlayers));
	}

	// all the other players this one can see
	private ArrayList<PlayerInfo> otherPlayers;

	public void clearOtherPlayers() {
		otherPlayers = new ArrayList<>();
	}

	public void addOtherPlayer(PlayerInfo player) {
		otherPlayers.add(player);
	}
}
