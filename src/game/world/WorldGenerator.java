package game.world;

import java.util.Random;

import game.world.Structures.*;
import game.world.Tiles.*;

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

    public WorldGenerator(int width, int height, long seed) {
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

    private Tile[][][] makeNullTileArray(int width, int height, int depth) {
        Tile[][][] nullTileArray = new Tile[width][height][depth];
        for (int z = 0; z < depth; z++) { // for each layer
            for (int y = 0; y < height; y++) { // for each row
                for (int x = 0; x < width; x++) { // for each column
                    nullTileArray[x][y][z] = Tile.getTile(x, y, z, 0, "null", "default");
                }
            }
        }
        return nullTileArray;
    }

    private boolean isThereSpace(Structure structure, Tile[][][] mapTiles) {
        // check if there is enough room for the structure
        if (structure.getX() + structure.getWidth() > width || structure.getY() + structure.getHeight() > height
                || structure.getZ() + structure.getDepth() > depth) {
            // not enough room, skip this structure
            return false;
        }
        // check if there is space for the structure
        for (int z1 = 0; z1 < structure.getDepth(); z1++) {
            for (int y1 = 0; y1 < structure.getHeight(); y1++) {
                for (int x1 = 0; x1 < structure.getWidth(); x1++) {
                    if (mapTiles[structure.getX() + x1][structure.getY() + y1][structure.getZ() + z1] != null
                            && !mapTiles[structure.getX() + x1][structure.getY() + y1][structure.getZ() + z1]
                                    .getType().equals("null")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private Class[] getStructureTypes() {
        return new Class[] {
        	BasicPondStructure.class,
        	BasicHouseStructure.class,
        	CratePileStructure.class,
        	HovelStructure.class
        };
    }

    public Tile[][][] generateWorld() {
        // make random object
        Random rand = new Random(this.seed);

        Tile[][][] newMapTiles = makeNullTileArray(width, height, depth);
        Class[] structureTypes = getStructureTypes();

        // generate the map
        for (int z = 0; z < depth; z++) { // for each layer
            for (int y = 0; y < height; y++) { // for each row
                for (int x = 0; x < width; x++) { // for each column
                    // generate random structures
                    if (newMapTiles[x][y][z] != null && !newMapTiles[x][y][z].getType().equals("null")) {
                        continue;
                    }
                    if (rand.nextInt(1000) < 50 && z == 0) { // 5% chance of generating a structure
                        // generate a random structure
                        int structureTypeIndex = rand.nextInt(structureTypes.length);
                        Class structureType = structureTypes[structureTypeIndex];
                        try {
                            // get the constructor for the structure
                            java.lang.reflect.Constructor constructor = structureType
                                    .getConstructor(int.class, int.class, int.class, int.class);
                            // create a new instance of the structure
                            Structure structure = (Structure) constructor.newInstance(x, y, z, rand.nextInt(4));

                            // check if there is enough room for the structure
                            if (!isThereSpace(structure, newMapTiles)) {
                                continue;
                            }

                            // add the structure to the map
                            for (int z1 = 0; z1 < structure.getDepth(); z1++) {
                                for (int y1 = 0; y1 < structure.getHeight(); y1++) {
                                    for (int x1 = 0; x1 < structure.getWidth(); x1++) {
                                        newMapTiles[structure.getX() + x1][structure.getY() + y1][structure.getZ()
                                                + z1] = structure.getTile(x1, y1, z1);
                                    }
                                }
                            }

                        } catch (Exception e) {
                            System.out.println("Error generating structure");
                            e.printStackTrace();
                        }
                    } else if (rand.nextInt(100) < 2 && z == 1 && (newMapTiles[x][y][0].getType().equals("null")
                            || newMapTiles[x][y][0].getType().equals("grass"))) {
                        // 2% chance of generating a crate
                        newMapTiles[x][y][z] = Tile.getTile(x, y, z, 0, "crate", "default");
                    } else if (rand.nextInt(100) < 2 && z == 2 && (newMapTiles[x][y][0].getType().equals("null")
                            || newMapTiles[x][y][0].getType().equals("grass"))) {
                        // 2% chance of generating a bush
                        newMapTiles[x][y][z] = Tile.getTile(x, y, z, 0, "bush", "default");
                    } else if (z == 0) { // 85% chance of generating a tile
                        newMapTiles[x][y][z] = Tile.getTile(x, y, z, 0, "grass", "default");
                    }
                }
            }
        }
        int numSandTiles2 = 0;

        for (int z = 0; z < depth; z++) { // for each layer
            for (int y = 0; y < height; y++) { // for each row
                for (int x = 0; x < width; x++) { // for each column
                    if (newMapTiles[x][y][z] == null)
                        continue;
                    if (newMapTiles[x][y][z].getType().equals("sand")) {
                        numSandTiles2++;
                    }
                }
            }
        }

        // replace nulls with grass if on layer 0 else air
        for (int z = 0; z < depth; z++) { // for each layer
            for (int y = 0; y < height; y++) { // for each row
                for (int x = 0; x < width; x++) { // for each column
                    if (newMapTiles[x][y][z] == null || newMapTiles[x][y][z].getType().equals("null")) {
                        if (z == 0) {
                            newMapTiles[x][y][z] = Tile.getTile(x, y, z, 0, "grass", "default");
                        } else {
                            newMapTiles[x][y][z] = Tile.getTile(x, y, z, 0, "air", "default");
                        }
                    }
                }
            }
        }

        /*
         * for (int y = 0; y < height; y++) { // for each row
         * for (int x = 0; x < width; x++) { // for each column
         * if (newMapTiles[x][y][0] == null)
         * continue;
         * System.out.print(newMapTiles[x][y][1].getType() + " ");
         * }
         * System.out.println();
         * }
         */

        int numSandTiles = 0;

        for (int z = 0; z < depth; z++) { // for each layer
            for (int y = 0; y < height; y++) { // for each row
                for (int x = 0; x < width; x++) { // for each column
                    if (newMapTiles[x][y][z].getType().equals("sand")) {
                        numSandTiles++;
                    }
                }
            }
        }

        // System.out.println("Number of null tiles: " + numSandTiles);
        // System.out.println("Number of null tiles after: " + numSandTiles2);

        return newMapTiles;
    }
}
