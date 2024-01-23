package world.Tiles;

/**
 * Represents a floor tile in a game.
 * Extends the Tile class and provides additional functionality specific to floor tiles.
 *
 * @param x The x-coordinate of the floor tile
 * @param y The y-coordinate of the floor tile
 * @param z The z-coordinate of the floor tile
 * @param orientation The orientation of the floor tile
 * @param state The state of the floor tile
 */
public class FloorTile extends Tile {
    public FloorTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "floor", state);
    }

    public FloorTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

}
