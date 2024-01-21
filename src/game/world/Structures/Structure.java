package game.world.Structures;

import game.world.Tiles.Tile;

public class Structure {
    Tile[][][] tiles;
    int x, y, z; // top left corner of the structues bounding box
    int width, height, depth; // dimensions of the bounding box
    int orientation; // 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees

    public Structure(int x, int y, int z, Tile[][][] tiles, int orientation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tiles = tiles;
        // rotate(tiles, orientation);
        this.depth = tiles.length;
        this.height = tiles[0].length;
        this.width = tiles[0][0].length;
        this.orientation = orientation;
    }

    public Structure(int x, int y, int z, int orientation, Tile[][][] tiles) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tiles = tiles;
        // rotate(tiles, orientation);
        this.depth = tiles.length;
        this.height = tiles[0].length;
        this.width = tiles[0][0].length;
        this.orientation = orientation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public Tile getTile(int x, int y, int z) {
        return tiles[z][y][x];
    }

    public void rotate(Tile[][][] tiles, int orientation) {
        // takes the array of tiles and rotates it clock-wise creating a new array with
        // maybe different dimensions
        // 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
        for (int z = 0; z < tiles.length; z++) {
            rotateLayer(tiles[z], orientation);
        }
        this.depth = tiles.length;
        this.width = tiles[0].length;
        this.height = tiles[0][0].length;

    }

    private void rotateLayer(Tile[][] layer, int orientation) {
        // 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
        // structure is [z][y][x]

    }

}
