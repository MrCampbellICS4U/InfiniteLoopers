package server;

import java.net.Socket;
import java.util.ArrayList;

import game.world.Tiles.Tile;
import game.world.Tiles.AirTile;
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
		if (visibleTiles == null) {
			setVisibleTiles(calculateVisibleTiles(map));
			send(new SendFullClientFOV(visibleTiles));
			return;
		}
		ArrayList<Tile> oldVisibleTiles = ConvertToArrayList.convert(getVisibleTiles());
		ArrayList<Tile> newVisibleTiles = ConvertToArrayList.convert(calculateVisibleTiles(map));

		System.out.println("Old visible tiles:");
		for (Tile tile : oldVisibleTiles) {
			System.out.print(tile.getType() + " " + tile.getState() + ", ");
		}

		System.out.println("New visible tiles:");
		for (Tile tile : newVisibleTiles) {
			System.out.print(tile.getType() + " " + tile.getState() + ", ");
		}

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
				if (newTile.equals(oldTile)) { // if the tiles are the same, remove them from
					// the list
					newVisibleTiles.remove(i);
					oldVisibleTiles.remove(j);
					i--;
					break;
				} else if (j == oldVisibleTiles.size() - 1) { // if the tile is not in the
					// old tiles, add it to the
					// list to send
					send(new TileUpdate(oldTile));
					newVisibleTiles.remove(i);
					i--;
					break;
				} else if (newTile.getX() == oldTile.getX() && newTile.getY() == oldTile.getY()
						&& newTile.getZ() == oldTile.getZ()) { // if the tiles are not the same but
					// have the same
					// coords, update the tile
					send(new TileUpdate(oldTile));
					newVisibleTiles.remove(i);
					oldVisibleTiles.remove(j);
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
