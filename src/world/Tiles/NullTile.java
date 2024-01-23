package world.Tiles;

public class NullTile extends Tile {
    public NullTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

    public NullTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "null", state);
    }
}
