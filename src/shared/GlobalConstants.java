package shared;

import java.util.Random;

public class GlobalConstants implements java.io.Serializable {
    public int TILE_WIDTH = 100;
    public int TILE_HEIGHT = 100;

    public int TILE_X_BUFFER = 2;
    public int TILE_Y_BUFFER = 2;

    public int DRAWING_AREA_WIDTH = 1300;
    public int DRAWING_AREA_HEIGHT = 800;

    public int WORLD_TILE_WIDTH = 15; // in tiles
    public int WORLD_TILE_HEIGHT = 15; // in tiles

    public int WORLD_WIDTH = WORLD_TILE_WIDTH * TILE_WIDTH; // in pixels
    public int WORLD_HEIGHT = WORLD_TILE_HEIGHT * TILE_HEIGHT; // in pixels

    public int CEILING_DISAPPEARING_DISTANCE = 300; // in pixels
    public int CHUNK_WIDTH = TILE_WIDTH * 4;
    public int CHUNK_HEIGHT = TILE_HEIGHT * 4;

    public int PLAYER_WIDTH = 50;
    public int PLAYER_HEIGHT = 50;

    public int TPS = 120; // ticks per second
    public int FPS = 60; // frames per second

    public int SEED = 71;

    public int SERVER_PORT = 2000;
    public String SERVER_IP = "127.0.0.1";

    public int MAX_HOTBAR = 3;
    public int MAX_HEALTH = 5; // 4 hearts
    public int MAX_ARMOR = Integer.MAX_VALUE;

    public int REGEN_TIME = 7000; // ms

    public int BULLET_DESPAWN_TIME = 800; // ms
    public int BULLET_SPEED = 10; // px/tick, if it's a lot bigger than 12 (radius*2), bullet collisions
                                  // might start bugging
    public int SHOT_DELAY = 170; // ms

    // can clients press P to kill themselves?
    public boolean CAN_SUICIDE = true;

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.writeInt(TILE_WIDTH);
        out.writeInt(TILE_HEIGHT);
        out.writeInt(TILE_X_BUFFER);
        out.writeInt(TILE_Y_BUFFER);
        out.writeInt(DRAWING_AREA_WIDTH);
        out.writeInt(DRAWING_AREA_HEIGHT);
        out.writeInt(WORLD_TILE_WIDTH);
        out.writeInt(WORLD_TILE_HEIGHT);
        out.writeInt(WORLD_WIDTH);
        out.writeInt(WORLD_HEIGHT);
        out.writeInt(CEILING_DISAPPEARING_DISTANCE);
        out.writeInt(CHUNK_WIDTH);
        out.writeInt(CHUNK_HEIGHT);
        out.writeInt(PLAYER_WIDTH);
        out.writeInt(PLAYER_HEIGHT);
        out.writeInt(TPS);
        out.writeInt(FPS);
        out.writeInt(SEED);
        out.writeInt(SERVER_PORT);
        out.writeUTF(SERVER_IP);
        out.writeInt(MAX_HOTBAR);
        out.writeInt(MAX_HEALTH);
        out.writeInt(REGEN_TIME);
        out.writeInt(BULLET_DESPAWN_TIME);
        out.writeInt(BULLET_SPEED);
        out.writeInt(SHOT_DELAY);
        out.writeBoolean(CAN_SUICIDE);
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        TILE_WIDTH = in.readInt();
        TILE_HEIGHT = in.readInt();
        TILE_X_BUFFER = in.readInt();
        TILE_Y_BUFFER = in.readInt();
        DRAWING_AREA_WIDTH = in.readInt();
        DRAWING_AREA_HEIGHT = in.readInt();
        WORLD_TILE_WIDTH = in.readInt();
        WORLD_TILE_HEIGHT = in.readInt();
        WORLD_WIDTH = in.readInt();
        WORLD_HEIGHT = in.readInt();
        CEILING_DISAPPEARING_DISTANCE = in.readInt();
        CHUNK_WIDTH = in.readInt();
        CHUNK_HEIGHT = in.readInt();
        PLAYER_WIDTH = in.readInt();
        PLAYER_HEIGHT = in.readInt();
        TPS = in.readInt();
        FPS = in.readInt();
        SEED = in.readInt();
        SERVER_PORT = in.readInt();
        SERVER_IP = in.readUTF();
        MAX_HOTBAR = in.readInt();
        MAX_HEALTH = in.readInt();
        REGEN_TIME = in.readInt();
        BULLET_DESPAWN_TIME = in.readInt();
        BULLET_SPEED = in.readInt();
        SHOT_DELAY = in.readInt();
        CAN_SUICIDE = in.readBoolean();
    }
}//
