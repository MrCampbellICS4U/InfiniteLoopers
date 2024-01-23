package world.Tiles;

public class RoofTile extends Tile {
    public RoofTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "roof", state);
    }

    public RoofTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

}
