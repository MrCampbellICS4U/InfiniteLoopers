package world.Tiles;

import collision.Rectangle;
import server.WallHitbox;

public class CrateTile extends Tile {
	public Class<? extends Rectangle> getHitboxType() { return WallHitbox.class; }

    public CrateTile(int x, int y, int z, int id, String type, String state) {
        super(x, y, z, id, type, state);
    }

    public CrateTile(int x, int y, int z, int id, String state) {
        super(x, y, z, id, "crate", state);
    }
}
