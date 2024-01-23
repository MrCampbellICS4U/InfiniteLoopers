package world.Structures;

import shared.utilities.LettersToTiles;

public class SingleBush extends Structure {
    public static int chance = 10;

    public SingleBush(int x, int y, int z, int orientation) {
        super(x, y, z, orientation, LettersToTiles.converToTiles(new String[][][] {
                {
                        { "N" }
                },
                {
						{ "N" }
				},
                {
                        { "B" }
                }
        }, x, y, z));
        // 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
    }
}
