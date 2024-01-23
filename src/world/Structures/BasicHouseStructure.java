package world.Structures;

import shared.utilities.LettersToTiles;

/**
 * Represents a basic house structure.
 * Extends the Structure class.
 * 
 * @param x The x-coordinate of the structure.
 * @param y The y-coordinate of the structure.
 * @param z The z-coordinate of the structure.
 * @param orientation The orientation of the structure.
 */
public class BasicHouseStructure extends Structure {
        public static int chance = 1;

        public BasicHouseStructure(int x, int y, int z, int orientation) {
                super(x, y, z, orientation, LettersToTiles.converToTiles(new String[][][] {
                                {
                                                { "F", "F", "F", "F", "F", "F" },
                                                { "F", "F", "F", "F", "F", "F" },
                                                { "F", "F", "F", "F", "F", "F" },
                                                { "F", "F", "F", "F", "F", "F" },
                                                { "F", "F", "F", "F", "F", "F" }
                                },
                                {
                                                { "Wl90", "Wleftright", "Wleftright", "Wleftright", "Wleftright",
                                                                "Wl180" },
                                                { "Wupdown", "A", "A", "A", "A", "Wupdown" },
                                                { "Wupdown", "A", "A", "A", "A", "Wupdown" },
                                                { "Wupdown", "A", "A", "A", "A", "Wupdown" },
                                                { "Wl", "Wendright", "A", "A", "Wendleft", "Wl270" }
                                },
                                {
                                                { "R90", "R90", "R90", "R270", "R270", "R270" },
                                                { "R90", "R90", "R90", "R270", "R270", "R270" },
                                                { "R90", "R90", "R90", "R270", "R270", "R270" },
                                                { "R90", "R90", "R90", "R270", "R270", "R270" },
                                                { "R90", "R90", "R90", "R270", "R270", "R270" },

                                }
                }, x, y, z));
                // 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
        }
}
