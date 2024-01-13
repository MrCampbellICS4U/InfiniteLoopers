package server;

import java.net.Socket;
import java.util.ArrayList;

import game.world.Tiles.Tile;
import shared.*;
import packets.*;

// clients from the server's perspective
class SClient extends PacketLord<Server> {
	private double xx = 5000, yy = 5000;
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
		for (int x1 = 0; x1 < visibleTiles.length; x1++)
			for (int y1 = 0; y1 < visibleTiles[0].length; y1++)
				for (int z = 0; z < visibleTiles[0][0].length; z++)
					visibleTiles[x1][y1][z] = map[topLeftX + x1][topLeftY + y1][z];

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
		Tile[][][] oldVisibleTiles = getVisibleTiles();
		Tile[][][] newVisibleTiles = calculateVisibleTiles(map);
		Tile[][][] tilesToSend = new Tile[newVisibleTiles.length][newVisibleTiles[0].length][newVisibleTiles[0][0].length];

		// if the player has no tiles at all
		if (oldVisibleTiles == null) {
			setVisibleTiles(newVisibleTiles);
			send(new SendFullClientFOV(newVisibleTiles));
			return;
		} else {
			// if the player has tiles, check if they need updating and if they do only send
			// over the ones that need updating
			boolean needsUpdate = false;
			for (int x = 0; x < newVisibleTiles.length; x++) {
				for (int y = 0; y < newVisibleTiles[0].length; y++) {
					for (int z = 0; z < newVisibleTiles[0][0].length; z++) {
						if (newVisibleTiles[x][y][z] == null || oldVisibleTiles[x][y][z] == null)
							continue;
						if (!newVisibleTiles[x][y][z].equals(oldVisibleTiles[x][y][z])) {
							needsUpdate = true;
							tilesToSend[x][y][z] = newVisibleTiles[x][y][z];
						}
					}
				}
			}
			if (needsUpdate) {
				// pring out the outgoing tiles in a list style
				System.out.println("Tiles to send:");
				for (int x = 0; x < tilesToSend.length; x++) {
					for (int y = 0; y < tilesToSend[0].length; y++) {
						for (int z = 0; z < tilesToSend[0][0].length; z++) {
							if (tilesToSend[x][y][z] == null)
								System.out.print("null ");
							else
								System.out.print(tilesToSend[x][y][z].getType() + " ");
						}
						System.out.println();
					}
					System.out.println();
				}
				setVisibleTiles(newVisibleTiles);
				send(new PartialFOVUpdate(tilesToSend));
			}

		}
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

	public void updatePlayer() {
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
