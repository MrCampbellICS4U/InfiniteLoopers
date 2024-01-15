package game.world.Tiles;

import java.util.HashMap;

public class GrassTile extends Tile {
    public GrassTile(int x, int y, int z, int orientation, String type, String state) {
        super(x, y, z, orientation, type, state);
    }

    public GrassTile(int x, int y, int z, int orientation, String state) {
        super(x, y, z, orientation, "grass", state);
    }

}
