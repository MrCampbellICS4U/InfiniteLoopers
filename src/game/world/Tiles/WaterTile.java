package game.world.Tiles;

import collision.Rectangle;
import server.WaterHitbox;

public class WaterTile extends Tile {
	public Class<? extends Rectangle> getHitbox() { return WaterHitbox.class; }

    public WaterTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

    public WaterTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "water", state);
    }
}
