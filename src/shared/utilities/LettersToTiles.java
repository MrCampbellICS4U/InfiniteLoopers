package shared.utilities;

import java.util.HashMap;

import game.world.Tiles.*;

public class LettersToTiles {
    // hashmap of letters to tiles objects, not initialized yet
    public static HashMap<String, Class> tilesMap = new HashMap<String, Class>();

    public LettersToTiles() {
        tilesMap.put("N", NullTile.class);
        tilesMap.put("A", AirTile.class);
        tilesMap.put("W", WaterTile.class);
        tilesMap.put("S", SandTile.class);
        tilesMap.put("G", GrassTile.class);
    }

    public static Tile[][][] converToTiles(String[][][] letterMap) {
        Tile[][][] tiles = new Tile[letterMap.length][letterMap[0].length][letterMap[0][0].length];
        for (int i = 0; i < letterMap.length; i++) {
            for (int j = 0; j < letterMap[0].length; j++) {
                for (int k = 0; k < letterMap[0][0].length; k++) {
                    try {
                        tiles[i][j][k] = (Tile) tilesMap.get(letterMap[i][j][k])
                                .getConstructor(int.class, int.class, int.class, int.class, String.class)
                                .newInstance(i, j, k, 0, "default");
                    } catch (Exception e) {
                        System.out.println("Error converting letters to tiles");
                        e.printStackTrace();
                    }
                }
            }
        }
        return tiles;
    }

}
