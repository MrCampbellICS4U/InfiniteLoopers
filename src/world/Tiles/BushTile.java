package world.Tiles;

public class BushTile extends Tile {
    public BushTile(int x, int y, int z, int id, String type, String state) {
        super(x, y, z, id, type, state);
    }

    public BushTile(int x, int y, int z, int id, String state) {
        super(x, y, z, id, "bush", state);
    }
}
