package world.Tiles;

/**
 * Represents a bush tile on a game map.
 * Extends the Tile class and provides additional functionality specific to bush tiles.
 *
 * @param x The x-coordinate of the bush tile
 * @param y The y-coordinate of the bush tile
 * @param z The z-coordinate of the bush tile
 * @param id The unique identifier of the bush tile
 * @param type The type of the bush tile
 * @param state The state of the bush tile
 */
public class BushTile extends Tile {
    public BushTile(int x, int y, int z, int id, String type, String state) {
        super(x, y, z, id, type, state);
    }

    public BushTile(int x, int y, int z, int id, String state) {
        super(x, y, z, id, "bush", state);
    }
}
