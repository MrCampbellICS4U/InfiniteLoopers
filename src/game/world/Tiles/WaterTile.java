package game.world.Tiles;

public class WaterTile extends Tile {
    public WaterTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

    public WaterTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "water", state);
    }
}
