package shared.utilities;

import java.util.HashMap;

import game.world.Tiles.*;

public class LettersToTiles {
    // hashmap of letters to tiles objects, not initialized yet
    public static HashMap<String, Class> tilesMap = new HashMap<String, Class>();

    static {
        tilesMap.put("N", NullTile.class);
        tilesMap.put("A", AirTile.class);
        tilesMap.put("W", WaterTile.class);
        tilesMap.put("S", SandTile.class);
        tilesMap.put("G", GrassTile.class);
        tilesMap.put("C", CrateTile.class);
    }

    public static Tile[][][] converToTiles(String[][][] letterMap, int x, int y, int z) {
        Tile[][][] tiles = new Tile[letterMap.length][letterMap[0].length][letterMap[0][0].length];
        for (int z1 = 0; z1 < letterMap.length; z1++) {
            for (int y1 = 0; y1 < letterMap[0].length; y1++) {
                for (int x1 = 0; x1 < letterMap[0][0].length; x1++) {
                    try {
                        tiles[z1][y1][x1] = (Tile) tilesMap.get(letterMap[z1][y1][x1])
                                .getConstructor(int.class, int.class, int.class, int.class, String.class)
                                .newInstance(x + x1, y + y1, z + z1, 0, "default");
                        //System.out.print(letterMap[z1][y1][x1]);
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
