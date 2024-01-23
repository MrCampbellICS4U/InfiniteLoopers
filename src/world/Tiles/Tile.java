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

    /**
     * Returns the type of hitbox associated with this object.
     *
     * @return The class representing the type of hitbox, or null if there is no
     *         hitbox.
     */
    public Class<? extends Rectangle> getHitboxType() {
        return null;
    }

    /**
     * Constructs a new Tile object with the specified coordinates, orientation,
     * type, and state.
     *
     * @param x           The x-coordinate of the tile
     * @param y           The y-coordinate of the tile
     * @param z           The z-coordinate of the tile
     * @param orientation The orientation of the tile
     * @param type        The type of the tile
     * @param state       The state of the tile
     */
    public Tile(int x, int y, int z, int orientation, String type, String state) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.orientation = orientation;
        this.type = type;
        this.state = state;
        this.statesMap = generateStatesMap();
    }

    public boolean isEmpty() {
        return type.equals("grass") || type.equals("water");
    }

    /**
     * Generates a map of states for a specific type of game world tiles.
     *
     * @return A HashMap containing the states as keys and their corresponding file
     *         paths as values.
     */
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
    /**
     * Retrieves a Tile object based on the provided parameters.
     *
     * @param x           The x-coordinate of the tile
     * @param y           The y-coordinate of the tile
     * @param z           The z-coordinate of the tile
     * @param orientation The orientation of the tile
     * @param type        The type of the tile
     * @param state       The state of the tile
     * @return The Tile object created based on the provided parameters
     */
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

    /**
     * Checks if this Tile object is equal to another object.
     *
     * @param obj The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
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

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object in the format: type_state at x,
     *         y, z
     */
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

    /**
     * Writes the state of the object to the ObjectOutputStream.
     *
     * @param out The ObjectOutputStream to write the object state to
     * @throws IOException If an I/O error occurs while writing the object state
     */
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(z);
        out.writeInt(orientation);
        out.writeObject(type);
        out.writeObject(state);
    }

    /**
     * Custom deserialization method for reading the object from an
     * ObjectInputStream.
     *
     * @param in The ObjectInputStream used for reading the object.
     * @throws IOException            If there is an error reading from the input
     *                                stream.
     * @throws ClassNotFoundException If the class of the serialized object cannot
     *                                be found.
     */
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        x = in.readInt();
        y = in.readInt();
        z = in.readInt();
        orientation = in.readInt();
        type = (String) in.readObject();
        state = (String) in.readObject();
    }
}
