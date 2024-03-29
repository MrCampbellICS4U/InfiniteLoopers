package shared.utilities;

import java.util.HashMap;

import world.Tiles.*;

public class LettersToTiles {
    // hashmap of letters to tiles objects, not initialized yet
    public static HashMap<String, Class> tilesMap = new HashMap<String, Class>();

    /**
     * Initializes the tiles map with the corresponding tile classes.
     * The keys represent the tile codes, and the values represent the tile classes.
     * The tile codes and classes are as follows:
     * - "N": NullTile
     * - "A": AirTile
     * - "L": WaterTile
     * - "S": SandTile
     * - "G": GrassTile
     * - "C": CrateTile
     * - "F": FloorTile
     * - "W": WallTile
     * - "R": RoofTile
     * - "B": BushTile
     */
    static {
        tilesMap.put("N", NullTile.class);
        tilesMap.put("A", AirTile.class);
        tilesMap.put("L", WaterTile.class);
        tilesMap.put("S", SandTile.class);
        tilesMap.put("G", GrassTile.class);
        tilesMap.put("C", CrateTile.class);
        tilesMap.put("F", FloorTile.class);
        tilesMap.put("W", WallTile.class);
        tilesMap.put("R", RoofTile.class);
        tilesMap.put("B", BushTile.class);
    }

    /**
     * Converts a 3D array of letter strings into a 3D array of Tile objects.
     *
     * @param letterMap The 3D array of letter strings representing the map
     * @param x The starting x-coordinate for the tiles
     * @param y The starting y-coordinate for the tiles
     * @param z The starting z-coordinate for the tiles
     * @return The 3D array of Tile objects representing the map
     */
    public static Tile[][][] converToTiles(String[][][] letterMap, int x, int y, int z) {
        Tile[][][] tiles = new Tile[letterMap.length][letterMap[0].length][letterMap[0][0].length];
        for (int z1 = 0; z1 < letterMap.length; z1++) {
            for (int y1 = 0; y1 < letterMap[0].length; y1++) {
                for (int x1 = 0; x1 < letterMap[0][0].length; x1++) {
                    try {
                        tiles[z1][y1][x1] = (Tile) tilesMap.get(letterMap[z1][y1][x1].substring(0, 1).toUpperCase())
                                .getConstructor(int.class, int.class, int.class, int.class, String.class)
                                .newInstance(x + x1, y + y1, z + z1, 0,
                                        letterMap[z1][y1][x1].substring(1) != null
                                                && letterMap[z1][y1][x1].substring(1) != ""
                                                        ? letterMap[z1][y1][x1].substring(1)
                                                        : "default");
                        // System.out.print(letterMap[z1][y1][x1]);
                    } catch (Exception e) {
                        System.out.println("Error converting letters to tiles");
                        e.printStackTrace();
                    }
                }
                //System.out.println();
            }
        }
        return tiles;
    }

    // public static Tile[][][] converToTiles(String[][][] letterMap) {
    // tilesMap.put("N", NullTile.class);
    // tilesMap.put("A", AirTile.class);
    // tilesMap.put("W", WaterTile.class);
    // tilesMap.put("S", SandTile.class);
    // tilesMap.put("G", GrassTile.class);
    // tilesMap.put("C", CrateTile.class);

    // Tile[][][] tiles = new
    // Tile[letterMap.length][letterMap[0].length][letterMap[0][0].length];
    // for (int i = 0; i < letterMap.length; i++) {
    // for (int j = 0; j < letterMap[0].length; j++) {
    // for (int k = 0; k < letterMap[0][0].length; k++) {
    // try {
    // tiles[i][j][k] = (Tile) tilesMap.get(letterMap[i][j][k])
    // .getConstructor(int.class, int.class, int.class, int.class, String.class)
    // .newInstance(i, j, k, 0, "default");
    // } catch (Exception e) {
    // System.out.println("Error converting letters to tiles");
    // e.printStackTrace();
    // }
    // }
    // }
    // }
    // return tiles;
    // }

}
