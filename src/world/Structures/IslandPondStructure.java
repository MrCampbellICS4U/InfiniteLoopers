package world.Structures;

import shared.utilities.LettersToTiles;

public class IslandPondStructure extends Structure {
		// the chance is higher than normal because being so big already pulls down the chance a bit
		// since there is less chance there will be enough space
        public static int chance = 4;

        public IslandPondStructure(int x, int y, int z, int orientation) {
                super(x, y, z, LettersToTiles.converToTiles(new String[][][] {
                                {
                                	{ "N", "N", "N", "S", "S", "S", "S", "S", "S", "N", "N" },
                                	{ "N", "S", "S", "L", "L", "S", "S", "L", "L", "S", "N" },
                                	{ "N", "S", "L", "L", "L", "L", "L", "L", "L", "S", "S" },
                                	{ "S", "L", "L", "L", "L", "L", "L", "L", "L", "L", "S" },
                                	{ "S", "L", "L", "L", "S", "S", "L", "L", "L", "L", "S" },
                                	{ "S", "L", "L", "L", "S", "S", "L", "L", "L", "L", "S" },
                                	{ "S", "L", "L", "L", "L", "L", "L", "L", "L", "S", "S" },
                                	{ "S", "L", "L", "L", "L", "L", "L", "L", "L", "S", "N" },
                                	{ "S", "S", "S", "L", "L", "L", "L", "L", "S", "S", "N" },
                                	{ "N", "S", "S", "S", "S", "L", "L", "S", "S", "N", "N" },
                                	{ "N", "N", "N", "N", "S", "S", "S", "N", "N", "N", "N" },
                                },
                                {
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                },
                                {
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                	{ "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N" },
                                }
                },
                                x, y, z),
                                orientation);
        }
}
