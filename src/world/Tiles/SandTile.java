package world.Tiles;

/**
 * Represents a sand tile in a game world.
 * Extends the Tile class and provides constructors to initialize the properties
 * of the sand tile.
 *
 * @param x           The x-coordinate of the tile
 * @param y           The y-coordinate of the tile
 * @param z           The z-coordinate of the tile
 * @param orientation The orientation of the tile
 * @param type        The type of the tile
 * @param state       The state of the tile
 */
public class SandTile extends Tile {
    public SandTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

    public SandTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "sand", state);
    }
}
