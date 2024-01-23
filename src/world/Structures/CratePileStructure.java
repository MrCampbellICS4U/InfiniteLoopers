package world.Structures;

import shared.utilities.LettersToTiles;

public class CratePileStructure extends Structure {
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
