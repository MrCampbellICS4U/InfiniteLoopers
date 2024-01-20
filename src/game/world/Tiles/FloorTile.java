package game.world.Tiles;

public class FloorTile extends Tile {
    public FloorTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "floor", state);
    }

    public FloorTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

}
