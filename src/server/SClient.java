package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import game.world.Tiles.Tile;
import game.world.Tiles.AirTile;
import shared.*;
import packets.*;

// clients from the server's perspective
class SClient extends PacketLord<Server> {
	private double xx = 5000, yy = 5000;
	private double lastxx, lastyy;
	private Tile[][][] visibleTiles;

	public int getX() {
		return (int) xx;
	}

	public int getY() {
		return (int) yy;
	}

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
		int x = getX();
		int y = getY();

		// calculating the top left corner of the visible tiles base on the screen size
		// 13 tiles wide and 8 tiles tall but we have a buffer of 1 tile in each
		// direction
		// converting the pixel location to tile location and round tonearest tile size
		int topLeftX = (int) (x - GlobalConstants.DRAWING_AREA_WIDTH / 2) / GlobalConstants.TILE_WIDTH
				- GlobalConstants.TILE_X_BUFFER;
		int topLeftY = (int) (y - GlobalConstants.DRAWING_AREA_HEIGHT / 2) / GlobalConstants.TILE_HEIGHT
				- GlobalConstants.TILE_Y_BUFFER;

		// System.out.println("topLeftX: " + topLeftX);
		// System.out.println("topLeftY: " + topLeftY);

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
					visibleTiles[x1][y1][z] = map[xIndex][yIndex][z];
				}
			}
		}

		// for (int x1 = 0; x1 < visibleTiles.length; x1++)
		// for (int y1 = 0; y1 < visibleTiles[0].length; y1++)
		// for (int z = 0; z < visibleTiles[0][0].length; z++)
		// if (visibleTiles[x1][y1][z] != null)
		// System.out.println(visibleTiles[x1][y1][z].getType() + " " +
		// visibleTiles[x1][y1][z].getState());
		// else
		// System.out.println("null");
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

	public double setAngle(double angle) {
		return this.angle = angle;
	}

	public double getAngle() {
		return angle;
	}

	public PlayerInfo getInfo() {
		return new PlayerInfo(getX(), getY(), angle);
	}

	SClient(Socket socket, Server state, int id) {
		super(socket, state);
		setID(id);
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
		System.out.printf("Client %d unleashed a devastating attack!\n", getID());
	}

	// todo implement
	private void reload() {
		System.out.printf("Client %d attempts to reload\n", getID());
	}

	// todo implement
	private void useItem() {
		System.out.printf("Client %d tries to use an item\n", getID());
	}

	// todo implement
	private void dropItem() {
		System.out.printf("Client %d drops something on the ground\n", getID());
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

		xx += dx;
		yy += dy;

		if (xx < 0)
			xx = 0;
		if (yy < 0)
			yy = 0;
		if (xx > GlobalConstants.WORLD_WIDTH)
			xx = GlobalConstants.WORLD_WIDTH;
		if (yy > GlobalConstants.WORLD_HEIGHT)
			yy = GlobalConstants.WORLD_HEIGHT;

		if (Math.abs(xx - lastxx) >= GlobalConstants.TILE_WIDTH
				|| Math.abs(yy - lastyy) >= GlobalConstants.TILE_HEIGHT) { // if the player has moved at least 1 tile
																			// away from the last update
			lastxx = xx;
			lastyy = yy;
			handleVisibleTileUpdates(map);
		}

	}

	public void sendPackets() {
		if (!ready)
			return;

		send(new MePacket(getX(), getY(), getAngle()));
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
