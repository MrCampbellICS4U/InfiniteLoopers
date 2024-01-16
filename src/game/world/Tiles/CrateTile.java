package game.world.Tiles;

public class CrateTile extends Tile {
    public CrateTile(int x, int y, int z, int id, String type, String state) {
        super(x, y, z, id, type, state);
    }

    public CrateTile(int x, int y, int z, int id, String state) {
        super(x, y, z, id, "crate", state);
    }
}