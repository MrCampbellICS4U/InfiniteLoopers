package world.Tiles;

/**
 * Represents an air tile in a game world.
 * 
 * This class extends the Tile class and provides constructors to create an air tile
 * with specified coordinates, orientation, type, and state.
 * 
 * @param x The x-coordinate of the air tile
 * @param y The y-coordinate of the air tile
 * @param z The z-coordinate of the air tile
 * @param orientation The orientation of the air tile
 * @param type The type of the air tile
 * @param state The state of the air tile
 */
public class AirTile extends Tile {
    public AirTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

    public AirTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "air", state);
    }
}
