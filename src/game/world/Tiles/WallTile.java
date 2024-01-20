package game.world.Tiles;

public class WallTile extends Tile {
    public WallTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "wall", state);
    }

    public WallTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

}
