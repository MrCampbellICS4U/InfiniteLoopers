package world.Structures;

import shared.utilities.LettersToTiles;

/**
 * Represents a Crate Pile structure in the game.
 * 
 * The Crate Pile structure is a type of structure that consists of a 3x3 grid of tiles.
 * Each tile can be either "N" (empty) or "C" (crate).
 * The structure has two phases, which are randomly selected upon creation.
 * 
 * The structure has a chance value that determines the likelihood of it appearing in the game.
 * 
 * @param x The x-coordinate of the structure's position
 * @param y The y-coordinate of the structure's position
 * @param z The z-coordinate of the structure's position
 * @param orientation The orientation of the structure
 */
public class CratePileStructure extends Structure {
    public static int chance = 1;

	private static String[][][] phases = {
		{
			{ "N", "C", "N" },
			{ "N", "N", "C" },
			{ "C", "C", "C" },
		},
		{
			{ "C", "N", "C" },
			{ "N", "C", "C" },
			{ "N", "C", "N" },
		},
	};
    public CratePileStructure(int x, int y, int z, int orientation) {
        super(x, y, z, orientation, LettersToTiles.converToTiles(new String[][][] {
                {
                        { "N", "N", "N" },
                        { "N", "N", "N" },
                        { "N", "N", "N" }
                },
                phases[(int)(Math.random() * 2)],
                {
                        { "N", "N", "N" },
                        { "N", "N", "N" },
                        { "N", "N", "N" }
                }
        }, x, y, z));
        // 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
    }
}
