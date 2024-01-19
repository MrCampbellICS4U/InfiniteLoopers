package game.world.Tiles;

public class SandTile extends Tile {
    public SandTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

    public SandTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "sand", state);
    }
}
