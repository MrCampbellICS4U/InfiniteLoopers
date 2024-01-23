package world.Tiles;

import java.util.HashMap;

/**
 * Represents a grass tile in a game world.
 * Extends the Tile class and provides constructors to initialize the properties of the grass tile.
 *
 * @param x The x-coordinate of the grass tile
 * @param y The y-coordinate of the grass tile
 * @param z The z-coordinate of the grass tile
 * @param orientation The orientation of the grass tile
 * @param type The type of the grass tile
 * @param state The state of the grass tile
 */
public class GrassTile extends Tile {
    public GrassTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

    public GrassTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "grass", state);
    }

}
