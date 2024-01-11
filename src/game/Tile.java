package game;

public class Tile implements java.io.Serializable {
    private int x, y, z, orientation; // orientation: 0 = up, 1 = right, 2 = down, 3 = left
    private String type, state; // type: "grass", "wall", "door", etc... state: "open", "closed", etc...

    Tile(int x, int y, int z, int orientation, String type, String state) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.state = state;
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

    public String toString() {
        return "Tile: " + x + ", " + y + ", " + z + ", " + orientation + ", " + type + ", " + state;
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
