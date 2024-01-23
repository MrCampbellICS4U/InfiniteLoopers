package world.Structures;

import shared.utilities.LettersToTiles;

/**
 * Represents a single crate structure.
 * Inherits from the Structure class.
 *
 * @param x           The x-coordinate of the crate's position
 * @param y           The y-coordinate of the crate's position
 * @param z           The z-coordinate of the crate's position
 * @param orientation The orientation of the crate
 */
public class SingleCrate extends Structure {
        public static int chance = 10;

        public SingleCrate(int x, int y, int z, int orientation) {
                super(x, y, z, orientation, LettersToTiles.converToTiles(new String[][][] {
                                {
                                                { "N" }
                                },
                                {
                                                { "C" }
                                },
                                {
                                                { "N" }
                                }
                }, x, y, z));
                // 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
        }
}
