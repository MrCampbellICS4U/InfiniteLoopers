package world.Tiles;

public class AirTile extends Tile {
    public AirTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

    public AirTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "air", state);
    }
}