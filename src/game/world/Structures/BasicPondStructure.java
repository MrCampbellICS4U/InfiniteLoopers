package game.world.Structures;

import shared.utilities.LettersToTiles;

public class BasicPondStructure extends Structure {
        /*
         * pond structure is a 6x7 bounding box thing
         * N = null tile, L = water tile, S = sand tile
         * [N, N, S, S, S, N]
         * [N, S, L, L, L, S]
         * [S, L, L, L, S, S]
         * [S, L, L, L, S, N]
         * [S, L, L, L, S, S]
         * [S, S, L, L, L, S]
         * [N, S, S, S, S, N]
         * 
         */

        public BasicPondStructure(int x, int y, int z, int orientation) {
                super(x, y, z, LettersToTiles.converToTiles(new String[][][] {
                                {
                                                { "N", "N", "S", "S", "S", "N" },
                                                { "N", "S", "L", "L", "L", "S" },
                                                { "S", "L", "L", "L", "S", "S" },
                                                { "S", "L", "L", "L", "S", "N" },
                                                { "S", "L", "L", "L", "S", "S" },
                                                { "S", "S", "L", "L", "L", "S" },
                                                { "N", "S", "S", "S", "S", "N" }
                                },
                                {
                                                { "N", "N", "N", "N", "N", "N" },
                                                { "N", "N", "N", "N", "N", "N" },
                                                { "N", "N", "N", "N", "N", "N" },
                                                { "N", "N", "N", "N", "N", "N" },
                                                { "N", "N", "N", "N", "N", "N" },
                                                { "N", "N", "N", "N", "N", "N" },
                                                { "N", "N", "N", "N", "N", "N" }
                                },
                                {
                                                { "N", "N", "N", "N", "N", "N" },
                                                { "N", "N", "N", "N", "N", "N" },
                                                { "N", "N", "N", "N", "N", "N" },
                                                { "N", "N", "N", "N", "N", "N" },
                                                { "N", "N", "N", "N", "N", "N" },
                                                { "N", "N", "N", "N", "N", "N" },
                                                { "N", "N", "N", "N", "N", "N" }
                                }
                },
                                x, y, z),
                                orientation);
        }
}
