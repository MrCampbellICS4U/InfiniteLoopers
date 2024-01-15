package server;

import java.net.Socket;
import java.util.ArrayList;

import game.world.Tiles.Tile;
import shared.*;
import packets.*;
import client.Client;

// clients from the server's perspective
class SClient extends Circle {
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
		ArrayList<Tile> oldVisibleTiles = ConvertToArrayList
				.convert(getVisibleTiles() == null ? calculateVisibleTiles(map) : getVisibleTiles());
		ArrayList<Tile> newVisibleTiles = ConvertToArrayList.convert(calculateVisibleTiles(map));

		// if the player has no tiles at all
		// if (oldVisibleTiles == null) {
		/*
		 * setVisibleTiles(newVisibleTiles);
		 * send(new SendFullClientFOV(newVisibleTiles));
		 * return;
		 */
		// go through every tile and if in the new tiles, and if it is not in the old
		// tiles, add it to the list of tiles
		// to send, also remove it from the new tiles list and the old to not loop
		// through the same tiles again
		for (int i = 0; i < newVisibleTiles.size(); i++) {
			Tile newTile = newVisibleTiles.get(i);
			for (int j = 0; j < oldVisibleTiles.size(); j++) {
				Tile oldTile = oldVisibleTiles.get(j);
				if (newTile.equals(oldTile)) {
					newVisibleTiles.remove(i);
					oldVisibleTiles.remove(j);
					i--;
					break;
				} else if (j == oldVisibleTiles.size() - 1) {
					send(new TileUpdate(newTile));
					newVisibleTiles.remove(i);
					i--;
					break;
				}
			}
		}

		// if there are no tiles to send, don't send anything
		return;
		// }

		/*
		 * boolean needsUpdate = false;
		 * for (int x = 0; x < newVisibleTiles.length; x++) {
		 * for (int y = 0; y < newVisibleTiles[0].length; y++) {
		 * for (int z = 0; z < newVisibleTiles[0][0].length; z++) {
		 * if (newVisibleTiles[x][y][z] == null || oldVisibleTiles[x][y][z] == null)
		 * continue;
		 * if (!newVisibleTiles[x][y][z].equals(oldVisibleTiles[x][y][z])) {
		 * // System.out.println("Tile at " + x + ", " + y + ", " + z +
		 * " needs updating");
		 * needsUpdate = true;
		 * tilesToSend[x][y][z] = newVisibleTiles[x][y][z];
		 * }
		 * }
		 * }
		 * }
		 * if (needsUpdate) {
		 * // pring out the outgoing tiles in a list style
		 * // System.out.println("Tiles to send:");
		 * // for (int x = 0; x < tilesToSend.length; x++) {
		 * // for (int y = 0; y < tilesToSend[0].length; y++) {
		 * // for (int z = 0; z < tilesToSend[0][0].length; z++) {
		 * // if (tilesToSend[x][y][z] == null)
		 * // System.out.print("null ");
		 * // else
		 * // System.out.print(tilesToSend[x][y][z].getType() + " ");
		 * // }
		 * // System.out.println();
		 * // }
		 * // System.out.println();
		 * // }
		 * setVisibleTiles(newVisibleTiles);
		 * send(new PartialFOVUpdate(tilesToSend));
		 * }
		 */
	}

	private double angle;

	public double setAngle(double angle) { return this.angle = angle; }
	public double getAngle() { return angle; }
	
	public PlayerInfo getInfo() { return new PlayerInfo((int)getX(), (int)getY(), angle, health, armor); }
	
	private final int MAXHEALTH = 3;
	private int health = MAXHEALTH; // 3 hearts
	private final int MAXARMOR = 3;
	private int armor = 1;

	PacketLord<Server> pl;
	private final int id;
	public void send(PacketTo<Client> p) { pl.send(p); }
	public void close() { pl.close(); }
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

		double newX = getX() + dx;
		double newY = getY() + dy;
		
		newX = Math.max(0, Math.min(newX, GlobalConstants.WORLD_WIDTH));
		newY = Math.max(0, Math.min(newY, GlobalConstants.WORLD_HEIGHT));
		
		setPosition(newX, newY);
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
