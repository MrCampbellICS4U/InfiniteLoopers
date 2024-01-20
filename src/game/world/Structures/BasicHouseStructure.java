package game.world.Structures;

import game.world.Tiles.*;
import shared.utilities.LettersToTiles;

public class BasicHouseStructure extends Structure {
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
                        { "Wl90", "Wleftright", "Wleftright", "Wleftright", "Wleftright", "Wl180" },
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
