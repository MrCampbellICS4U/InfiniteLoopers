package world.Tiles;

import java.io.File;
import java.util.HashMap;

import collision.Rectangle;

public class Tile implements java.io.Serializable {
    private int x, y, z, orientation; // orientation: 0 = up, 1 = right, 2 = down, 3 = left
    private String type, state; // type: "grass", "wall", "door", etc... state: "open", "closed", etc...
    protected HashMap<String, String> statesMap = new HashMap<String, String>(); // "open" => image file location,
                                                                                 // "closed" => image file location,
                                                                                 // etc...

    public Class<? extends Rectangle> getHitboxType() {
        return null;
    }

    public Tile(int x, int y, int z, int orientation, String type, String state) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.orientation = orientation;
        this.type = type;
        this.state = state;
        this.statesMap = generateStatesMap();
    }

	public boolean isEmpty() { return type.equals("grass") || type.equals("water"); }

    public HashMap<String, String> generateStatesMap() {
        return this.statesMap = new HashMap<String, String>() {
            {
                for (String state : new File(
                        "res/game/world/Tiles/" + type.substring(0, 1).toUpperCase() + type.substring(1)).list()) {
                    put(state.substring(0, state.lastIndexOf('.')), "src/game/world/Tiles/"
                            + type.substring(0, 1).toUpperCase() + type.substring(1) + "/" + state);
                }
            }
        };
    }

    // returns a tile based on the type string
    public static Tile getTile(int x, int y, int z, int orientation, String type, String state) {
        Tile tile = null;
        try {
            Class<?> tileClass = Class
                    .forName("world.Tiles." + type.substring(0, 1).toUpperCase() +
                            type.substring(1) + "Tile");
            tile = (Tile) tileClass.getConstructor(int.class, int.class, int.class,
                    int.class, String.class,
                    String.class).newInstance(x, y, z, orientation, type, state);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();

        }
        return tile;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            Tile tile = (Tile) obj;
            return tile.getX() == this.getX() && tile.getY() == this.getY() && tile.getZ() == this.getZ()
                    && tile.getOrientation() == this.getOrientation() && tile.getType().equals(this.getType())
                    && tile.getState().equals(this.getState());
        }
        return false;
    }

    @Override
    public String toString() {
        return type + "_" + state + " at " + x + ", " + y + ", " + z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getOrientation() {
        return orientation;
    }

    public String getType() {
        return type;
    }

    public String getState() {
        return state;
    }

    public HashMap<String, String> getStatesMap() {
        return statesMap;
    }

    public int setX(int x) {
        return this.x = x;
    }

    public int setY(int y) {
        return this.y = y;
    }

    public int setZ(int z) {
        return this.z = z;
    }

    public int setOrientation(int orientation) {
        return this.orientation = orientation;
    }

    public String setType(String type) {
        return this.type = type;
    }

    public String setState(String state) {
        return this.state = state;
    }

    public HashMap<String, String> setStatesMap(HashMap<String, String> statesMap) {
        return this.statesMap = statesMap;
    }

    public void addState(String state, String imageFileLocation) {
        this.statesMap.put(state, imageFileLocation);
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(z);
        out.writeInt(orientation);
        out.writeObject(type);
        out.writeObject(state);
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        x = in.readInt();
        y = in.readInt();
        z = in.readInt();
        orientation = in.readInt();
        type = (String) in.readObject();
        state = (String) in.readObject();
    }
}
