package shared;

import java.util.ArrayList;

import world.Tiles.Tile;

public class ConvertToArrayList {
    /**
     * Converts an array of Tile objects into an ArrayList of Tile objects.
     *
     * @param array The array of Tile objects to be converted
     * @return An ArrayList containing the Tile objects from the array
     */
    public static ArrayList<Tile> convert(Tile[] array) {
        ArrayList<Tile> list = new ArrayList<Tile>();
        for (Tile t : array) {
            list.add(t);
        }
        return list;
    }

    /**
     * Converts a 2D array of Tile objects into an ArrayList of Tile objects.
     *
     * @param array The 2D array of Tile objects to be converted
     * @return An ArrayList containing all the Tile objects from the 2D array
     */
    public static ArrayList<Tile> convert(Tile[][] array) {
        ArrayList<Tile> list = new ArrayList<Tile>();
        for (Tile[] o : array) {
            for (Tile o2 : o) {
                list.add(o2);
            }
        }
        return list;
    }

    /**
     * Converts a 3-dimensional array of Tile objects into an ArrayList of Tile objects.
     *
     * @param array The 3-dimensional array of Tile objects to be converted
     * @return An ArrayList containing all the Tile objects from the array
     */
    public static ArrayList<Tile> convert(Tile[][][] array) {
        ArrayList<Tile> list = new ArrayList<Tile>();
        for (Tile[][] t : array) {
            for (Tile[] t2 : t) {
                for (Tile t3 : t2) {
                    list.add(t3);
                }
            }
        }
        return list;
    }
}
