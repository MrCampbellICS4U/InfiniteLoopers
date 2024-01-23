package world.Structures;

import shared.utilities.LettersToTiles;

public class HovelStructure extends Structure {
    public static int chance = 2;
    public HovelStructure(int x, int y, int z, int orientation) {
        super(x, y, z, orientation, LettersToTiles.converToTiles(new String[][][] {
                {
                        { "F", "F", "F", "F" },
                        { "F", "F", "F", "F" },
                        { "F", "F", "F", "F" },
                        { "F", "F", "F", "F" },
                        { "F", "F", "F", "F" },
                },
                {
                        { "Wendup", "A", "Wendleft", "Wl180" },
                        { "Wupdown", "A", "A", "Wupdown" },
                        { "Wupdown", "A", "A", "Wupdown" },
                        { "Wupdown", "A", "A", "Wupdown" },
                        { "Wl", "Wendright", "A", "Wenddown" },
                },
                {
                        { "R90", "R90", "R270", "R270" },
                        { "R90", "R90", "R270", "R270" },
                        { "R90", "R90", "R270", "R270" },
                        { "R90", "R90", "R270", "R270" },
                        { "R90", "R90", "R270", "R270" },
                }
        }, x, y, z));
        // 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
    }
}
