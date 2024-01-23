package world;

import java.util.HashMap;
import java.util.Random;

import world.Structures.*;
import world.Tiles.*;

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

    public Structure pickStructure(int x, int y, int z, int orientation) {
        Structure structure = null;
        HashMap<Class, Integer> structureTypes = getStructureTypes();
        int totalChances = 0;
        for (int chance : structureTypes.values()) {
            totalChances += chance;
        }
        int randomIndex = 0;

        int randomNum = new Random().nextInt(totalChances);
        for (Class structureType : structureTypes.keySet()) {
            randomNum -= structureTypes.get(structureType);
            if (randomNum <= 0) {
                try {
                    // get the constructor for the structure
                    java.lang.reflect.Constructor constructor = structureType
                            .getConstructor(int.class, int.class, int.class, int.class);
                    // create a new instance of the structure
                    structure = (Structure) constructor.newInstance(x, y, z, orientation);
                } catch (Exception e) {
                    System.out.println("Error generating structure");
                    e.printStackTrace();
                }
                break;
            }
        }

        return structure;
    }

    private HashMap<Class, Integer> getStructureTypes() {
        return new HashMap<Class, Integer>() {
            {
                put(BasicHouseStructure.class, BasicHouseStructure.chance);
                put(BasicPondStructure.class, BasicPondStructure.chance);
                put(CratePileStructure.class, CratePileStructure.chance);
                put(HovelStructure.class, HovelStructure.chance);
                put(SingleCrate.class, SingleCrate.chance);
                put(SingleBush.class, SingleBush.chance);
            }
        };
    }

    public Tile[][][] generateWorld() {
        // make random object
        Random rand = new Random(this.seed);

        Tile[][][] newMapTiles = makeNullTileArray(width, height, depth);

        // generate the map
        for (int z = 0; z < depth; z++) { // for each layer
            for (int y = 0; y < height; y++) { // for each row
                for (int x = 0; x < width; x++) { // for each column
                    // generate random structures
                    if (newMapTiles[x][y][z] != null && !newMapTiles[x][y][z].getType().equals("null")) {
                        continue;
                    }
                    if (rand.nextInt(100) < 5 && z == 0) { // 5% chance of generating a structure
                        // generate a random structure
                        Class structureType = pickStructure(x, y, z, rand.nextInt(4)).getClass();
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

		int bogRadius = 10;
		int bogCentreX = rand.nextInt(width);
		int bogCentreY = rand.nextInt(height);
        // replace nulls with grass if on layer 0 else air
        for (int z = 0; z < depth; z++) { // for each layer
            for (int y = 0; y < height; y++) { // for each row
                for (int x = 0; x < width; x++) { // for each column
                    if (newMapTiles[x][y][z] == null || newMapTiles[x][y][z].getType().equals("null")) {
                        if (z == 0) {
							double bogDist = Math.min(Math.hypot(x-bogCentreX, y-bogCentreY), bogRadius);
							double bogPercent = 1-bogDist/bogRadius;
							bogPercent = 1 - Math.pow(1 - bogPercent, 3); // https://easings.net/#easeOutCubic
							String type = rand.nextInt(100) < bogPercent*100 ? "water" : "grass";
                            newMapTiles[x][y][z] = Tile.getTile(x, y, z, 0, type, "default");
                        } else {
                            newMapTiles[x][y][z] = Tile.getTile(x, y, z, 0, "air", "default");
                        }
                    }
                }
            }
        }

        return newMapTiles;
    }
}
