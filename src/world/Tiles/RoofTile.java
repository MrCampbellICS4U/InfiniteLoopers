package world.Tiles;

/**
 * Represents a roof tile in a tile-based game.
 * Extends the Tile class and provides constructors to initialize the properties
 * of the roof tile.
 *
 * @param x           The x-coordinate of the roof tile.
 * @param y           The y-coordinate of the roof tile.
 * @param z           The z-coordinate of the roof tile.
 * @param orientation The orientation of the roof tile.
 * @param state       The state of the roof tile.
 */
public class RoofTile extends Tile {
    public RoofTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "roof", state);
    }

    public RoofTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

}
