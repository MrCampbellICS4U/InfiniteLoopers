package world.Structures;

import world.Tiles.Tile;

public class Structure {
    Tile[][][] tiles;
    int x, y, z; // top left corner of the structues bounding box
    int width, height, depth; // dimensions of the bounding box
    int orientation; // 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees

    /**
     * Constructs a Structure object with the given parameters.
     *
     * @param x The x-coordinate of the structure
     * @param y The y-coordinate of the structure
     * @param z The z-coordinate of the structure
     * @param tiles A 3D array representing the tiles of the structure
     * @param orientation The orientation of the structure
     */
    public Structure(int x, int y, int z, Tile[][][] tiles, int orientation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tiles = tiles;
        rotate(tiles, orientation);
        this.depth = tiles.length;
        this.height = tiles[0].length;
        this.width = tiles[0][0].length;
        this.orientation = orientation;
    }

    /**
     * Constructs a Structure object with the given parameters.
     *
     * @param x The x-coordinate of the structure's position
     * @param y The y-coordinate of the structure's position
     * @param z The z-coordinate of the structure's position
     * @param orientation The orientation of the structure
     * @param tiles The tiles that make up the structure
     */
    public Structure(int x, int y, int z, int orientation, Tile[][][] tiles) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tiles = tiles;
        rotate(tiles, orientation);
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

    /**
     * Rotates a 3D array of tiles by a given orientation.
     *
     * @param tiles The 3D array of tiles to rotate
     * @param orientation The orientation to rotate the tiles by
     */
    public void rotate(Tile[][][] tiles, int orientation) {
        // takes the array of tiles and rotates it clock-wise creating a new array with
        // maybe different dimensions
        // 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
        for (int z = 0; z < tiles.length; z++) {
            tiles[z] = rotateLayer(tiles[z], orientation);
        }
        this.height = tiles[0].length;
        this.width = tiles[0][0].length;

    }

    /**
     * Rotates a layer of tiles by the specified orientation.
     *
     * @param layer The layer of tiles to rotate
     * @param orientation The number of times to rotate the layer
     * @return The rotated layer of tiles
     */
    private Tile[][] rotateLayer(Tile[][] layer, int orientation) {
        for (int i = 0; i < orientation; i++) {
            layer = rotateOnce(layer);
        }
        return layer;
    }

    /**
     * Rotates a 2D array of tiles once clockwise.
     *
     * @param layer The 2D array of tiles to rotate
     * @return The rotated 2D array of tiles
     */
    private Tile[][] rotateOnce(Tile[][] layer) {
        int oldH = layer.length;
        int oldW = layer[0].length;

        int newW = oldH;
        int newH = oldW;
        Tile[][] rotated = new Tile[newH][newW];

        for (int x = 0; x < oldW; x++) {
            for (int y = 0; y < oldH; y++) {
                Tile t = layer[y][x];
                int newY = x, newX = oldH - y - 1;
                rotated[x][oldH - y - 1] = t;
                t.setY(getY() + newY);
                t.setX(getX() + newX);
                t.setOrientation((t.getOrientation() + 1) % 4);
            }
        }

        return rotated;
    }
}
