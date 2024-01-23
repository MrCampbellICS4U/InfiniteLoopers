package world.Tiles;

import collision.Rectangle;
import server.WaterHitbox;

/**
 * Represents a water tile in the game world.
 * Extends the Tile class.
 */
public class WaterTile extends Tile {
	public Class<? extends Rectangle> getHitboxType() { return WaterHitbox.class; }

    public WaterTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

    public WaterTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "water", state);
    }
}
