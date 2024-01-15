package shared;

import java.util.ArrayList;

import game.world.Tiles.Tile;

public class ConvertToArrayList {
    public static ArrayList<Tile> convert(Tile[] array) {
        ArrayList<Tile> list = new ArrayList<Tile>();
        for (Tile t : array) {
            list.add(t);
        }
        return list;
    }

    public static ArrayList<Tile> convert(Tile[][] array) {
        ArrayList<Tile> list = new ArrayList<Tile>();
        for (Tile[] o : array) {
            for (Tile o2 : o) {
                list.add(o2);
            }
        }
        return list;
    }

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
