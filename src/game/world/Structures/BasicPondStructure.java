package game.world.Structures;

import game.world.Tiles.Tile;
import game.world.Tiles.WaterTile;
import shared.utilities.LettersToTiles;

public class BasicPondStructure extends Structure {
    /*
     * pond structure is a 6x7 bounding box thing
     * N = null tile, W = water tile, S = sand tile
     * [N, N, S, S, S, N]
     * [N, S, W, W, W, S]
     * [S, W, W, W, S, S]
     * [S, W, W, W, S, N]
     * [S, W, W, W, S, S]
     * [S, S, W, W, W, S]
     * [N, S, S, S, S, N]
     * 
     */
    String[][][] letterMap = new String[][][] { { { "N", "N", "S", "S", "S", "N" },
            { "N", "S", "W", "W", "W", "S" },
            { "S", "W", "W", "W", "S", "S" },
            { "S", "W", "W", "W", "S", "N" },
            { "S", "W", "W", "W", "S", "S" },
            { "S", "S", "W", "W", "W", "S" },
            { "N", "S", "S", "S", "S", "N" } },
            { { "A", "A", "A", "A", "A", "A" },
                    { "A", "A", "A", "A", "A", "A" },
                    { "A", "A", "A", "A", "A", "A" },
                    { "A", "A", "A", "A", "A", "A" },
                    { "A", "A", "A", "A", "A", "A" },
                    { "A", "A", "A", "A", "A", "A" },
                    { "A", "A", "A", "A", "A", "A" } },
            { { "A", "A", "A", "A", "A", "A" },
                    { "A", "A", "A", "A", "A", "A" },
                    { "A", "A", "A", "A", "A", "A" },
                    { "A", "A", "A", "A", "A", "A" },
                    { "A", "A", "A", "A", "A", "A" },
                    { "A", "A", "A", "A", "A", "A" },
                    { "A", "A", "A", "A", "A", "A" } } };

    Tile[][][] tiles = LettersToTiles.converToTiles(letterMap);

    public BasicPondStructure(int x, int y, int z, int orientation) {
        super(x, y, z, LettersToTiles.converToTiles(new String[][][] { { { "N", "N", "S", "S", "S", "N" },
                { "N", "S", "W", "W", "W", "S" },
                { "S", "W", "W", "W", "S", "S" },
                { "S", "W", "W", "W", "S", "N" },
                { "S", "W", "W", "W", "S", "S" },
                { "S", "S", "W", "W", "W", "S" },
                { "N", "S", "S", "S", "S", "N" } },
                { { "A", "A", "A", "A", "A", "A" },
                        { "A", "A", "A", "A", "A", "A" },
                        { "A", "A", "A", "A", "A", "A" },
                        { "A", "A", "A", "A", "A", "A" },
                        { "A", "A", "A", "A", "A", "A" },
                        { "A", "A", "A", "A", "A", "A" },
                        { "A", "A", "A", "A", "A", "A" } },
                { { "A", "A", "A", "A", "A", "A" },
                        { "A", "A", "A", "A", "A", "A" },
                        { "A", "A", "A", "A", "A", "A" },
                        { "A", "A", "A", "A", "A", "A" },
                        { "A", "A", "A", "A", "A", "A" },
                        { "A", "A", "A", "A", "A", "A" },
                        { "A", "A", "A", "A", "A", "A" } } }),
                orientation);
    }
}
