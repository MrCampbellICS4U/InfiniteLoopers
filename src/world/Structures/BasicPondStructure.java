package world.Structures;

import shared.utilities.LettersToTiles;

/**
 * Represents a basic pond structure.
 * Extends the Structure class.
 *
 * @param x The x-coordinate of the structure.
 * @param y The y-coordinate of the structure.
 * @param z The z-coordinate of the structure.
 * @param orientation The orientation of the structure.
 */
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

        public static int chance = 1;

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
