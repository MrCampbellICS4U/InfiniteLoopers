package game.world;

import java.util.Random;

import game.world.Tiles.Tile;

/**
 * The WorldGenerator class is responsible for generating the game world.
 * Creates a 3d array of tiles, and fills it with tiles.
 * Map is based off a seed, which is a string of characters and or numbers.
 */
public class WorldGenerator {
    private Tile[][][] mapTiles;
    private int width, height, depth = 3; // depth is the number of layers in the map, this should always be 3 (ground,
                                          // walls, and ceiling)
    private long seed;

    WorldGenerator(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        mapTiles = new Tile[width][height][depth];
    }

    public Tile[][][] getMap() {
        return mapTiles;
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

    public long getSeed() {
        return seed;
    }

    public int setWidth(int width) {
        return this.width = width;
    }

    public int setHeight(int height) {
        return this.height = height;
    }

    public int setDepth(int depth) {
        return this.depth = depth;
    }

    public long setSeed(long seed) {
        return this.seed = seed;
    }

    public Tile getTile(int x, int y, int z) {
        return mapTiles[x][y][z];
    }

    public Tile setTile(int x, int y, int z, Tile tile) {
        return mapTiles[x][y][z] = tile;
    }

    public Tile[][][] generateWorld() {
        // make random object
        Random rand = new Random(this.seed);

        // generate the map
        for (int z = 0; z < depth; z++) { // for each layer
            for (int y = 0; y < height; y++) { // for each row
                for (int x = 0; x < width; x++) { // for each column
                    // tile types, GrassTile for now
                    // TODO: add more tile types
                    if (z == 0) {
                        mapTiles[x][y][z] = Tile.getTile(x, y, z, 0, "grass", "default");
                    }
                }
            }
        }

        return mapTiles;
    }
}
