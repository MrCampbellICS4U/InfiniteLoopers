package shared;

import java.util.ArrayList;

import game.world.Tiles.Tile;

public class ConvertToArrayList {
    public static ArrayList<Object> convert(Tile[] array) {
        ArrayList<Object> list = new ArrayList<Object>();
        for (Object o : array) {
            list.add(o);
        }
        return list;
    }

    static ArrayList<Object> convert(Tile[][] array) {
        ArrayList<Object> list = new ArrayList<Object>();
        for (Object[] o : array) {
            for (Object o2 : o) {
                list.add(o2);
            }
        }
        return list;
    }

    static ArrayList<Object> convert(Tile[][][] array) {
        ArrayList<Object> list = new ArrayList<Object>();
        for (Object[][] o : array) {
            for (Object[] o2 : o) {
                for (Object o3 : o2) {
                    list.add(o3);
                }
            }
        }
        return list;
    }

    static ArrayList<Object> convert(Tile[][][][] array) {
        ArrayList<Object> list = new ArrayList<Object>();
        for (Object[][][] o : array) {
            for (Object[][] o2 : o) {
                for (Object[] o3 : o2) {
                    for (Object o4 : o3) {
                        list.add(o4);
                    }
                }
            }
        }
        return list;
    }
}
