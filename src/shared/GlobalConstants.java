package shared;

import java.util.Random;

/**
 * A class that holds global constants used throughout the application.
 * These constants define various properties such as tile dimensions, drawing area dimensions,
 * world dimensions, player dimensions, server settings, game settings, and more.
 */
public class GlobalConstants implements java.io.Serializable {
    public int TILE_WIDTH = 100;
    public int TILE_HEIGHT = 100;

    public int TILE_X_BUFFER = 2;
    public int TILE_Y_BUFFER = 2;

    public int DRAWING_AREA_WIDTH = 1300;
    public int DRAWING_AREA_HEIGHT = 800;

    public int WORLD_TILE_WIDTH = 35; // in tiles
    public int WORLD_TILE_HEIGHT = 35; // in tiles

    public int WORLD_WIDTH = WORLD_TILE_WIDTH * TILE_WIDTH; // in pixels
    public int WORLD_HEIGHT = WORLD_TILE_HEIGHT * TILE_HEIGHT; // in pixels

    public int CEILING_DISAPPEARING_DISTANCE = 300; // in pixels
    public int CHUNK_WIDTH = TILE_WIDTH * 4;
    public int CHUNK_HEIGHT = TILE_HEIGHT * 4;

    public int PLAYER_WIDTH = 50;
    public int PLAYER_HEIGHT = 50;

    public int TPS = 120; // ticks per second
    public int FPS = 60; // frames per second

    public boolean RANDOM_SEED = true;
    public int SEED = 71;

    public int SERVER_PORT = 2000;
    public String SERVER_IP = "127.0.0.1";

    public int MAX_HOTBAR = 3;
    public int MAX_HEALTH = 5; // 5 hearts
    public int MAX_ARMOR = 5;

    public int REGEN_TIME = 7000; // ms

    public int KILLS_TO_WIN = 5;
    public int BULLET_DESPAWN_TIME = 800; // ms
    public int BULLET_SPEED = 10; // px/tick, if it's a lot bigger than 12 (radius*2), bullet collisions
                                  // might start bugging
    public int SHOT_DELAY = 400; // ms

    // can clients press P to kill themselves?
    public boolean CAN_SUICIDE = true;

	public int BOG_RADIUS = 7;
}
