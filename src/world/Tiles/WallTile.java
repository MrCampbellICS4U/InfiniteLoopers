package world.Tiles;

import server.WallHitbox;
import collision.Rectangle;

public class WallTile extends Tile {
	public Class<? extends Rectangle> getHitboxType() { return WallHitbox.class; }

    public WallTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "wall", state);
    }

    public WallTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

}
