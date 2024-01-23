package world.Tiles;

/**
 * Represents a roof tile in a tile-based game.
 * Extends the Tile class and provides constructors to initialize the properties of the roof tile.
 *
 * @param x The x-coordinate of the roof tile.
 * @param y The y-coordinate of the roof tile.
 * @param z The z-coordinate of the roof tile.
 * @param orientation The orientation of the roof tile.
 * @param state The state of the roof tile.
 */
import collision.Rectangle;
import server.WallHitbox;

/**
 * Represents a crate tile in the game.
 * Extends the Tile class.
 */
public class CrateTile extends Tile {
	public Class<? extends Rectangle> getHitboxType() { return WallHitbox.class; }

    public CrateTile(int x, int y, int z, int id, String type, String state) {
        super(x, y, z, id, type, state);
    }

    public CrateTile(int x, int y, int z, int id, String state) {
        super(x, y, z, id, "crate", state);
    }
}
