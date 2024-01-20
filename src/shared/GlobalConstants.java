package shared;

import java.util.Random;

public class GlobalConstants {
    public static final int TILE_WIDTH = 100;
    public static final int TILE_HEIGHT = 100;

    public static final int TILE_X_BUFFER = 2;
    public static final int TILE_Y_BUFFER = 2;

    public static final int DRAWING_AREA_WIDTH = 1300;
    public static final int DRAWING_AREA_HEIGHT = 800;

    public static final int WORLD_TILE_WIDTH = 20; // in tiles
    public static final int WORLD_TILE_HEIGHT = 20; // in tiles

    public static final int WORLD_WIDTH = WORLD_TILE_WIDTH * TILE_WIDTH; // in pixels
    public static final int WORLD_HEIGHT = WORLD_TILE_HEIGHT * TILE_HEIGHT; // in pixels

    public static final int CHUNK_WIDTH = TILE_WIDTH * 4;
    public static final int CHUNK_HEIGHT = TILE_HEIGHT * 4;

    public static final int PLAYER_WIDTH = 50;
    public static final int PLAYER_HEIGHT = 50;

    public static final int TPS = 120; // ticks per second
    public static final int FPS = 120; // frames per second

    public static final int SEED = new Random().nextInt();

    public static final int SERVER_PORT = 2000;
    public static final String SERVER_IP = "127.0.0.1";

    public static final int MAXHOTBAR = 3;
    public static final int MAXHEALTH = 3;
    public static final int MAXARMOR = 3;

}
