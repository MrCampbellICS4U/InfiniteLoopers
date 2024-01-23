package world.Tiles;

/**
 * Represents a null tile object that extends the Tile class.
 * This class is used to represent a tile with no specific type or state.
 *
 * @param x The x-coordinate of the tile
 * @param y The y-coordinate of the tile
 * @param z The z-coordinate of the tile
 * @param orientation The orientation of the tile
 * @param type The type of the tile (default: "null")
 * @param state The state of the tile
 */
public class NullTile extends Tile {
    public NullTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

    public NullTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "null", state);
    }
}
