package world.Structures;

import world.Tiles.Tile;

public class Structure {
    Tile[][][] tiles;
    int x, y, z; // top left corner of the structues bounding box
    int width, height, depth; // dimensions of the bounding box
    int orientation; // 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees

    public Structure(int x, int y, int z, Tile[][][] tiles, int orientation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tiles = tiles;
        rotate(tiles, orientation);
        this.depth = tiles.length;
        this.height = tiles[0].length;
        this.width = tiles[0][0].length;
        this.orientation = orientation;
    }

    public Structure(int x, int y, int z, int orientation, Tile[][][] tiles) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tiles = tiles;
        rotate(tiles, orientation);
        this.depth = tiles.length;
        this.height = tiles[0].length;
        this.width = tiles[0][0].length;
        this.orientation = orientation;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public Tile getTile(int x, int y, int z) {
        return tiles[z][y][x];
    }

    public void rotate(Tile[][][] tiles, int orientation) {
        // takes the array of tiles and rotates it clock-wise creating a new array with
        // maybe different dimensions
        // 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
        for (int z = 0; z < tiles.length; z++) {
            tiles[z] = rotateLayer(tiles[z], orientation);
        }
        this.height = tiles[0].length;
        this.width = tiles[0][0].length;

    }

    private Tile[][] rotateLayer(Tile[][] layer, int orientation) {
		for (int i = 0; i < orientation; i++) {
			layer = rotateOnce(layer);
		}
		return layer;
    }

	private Tile[][] rotateOnce(Tile[][] layer) {
		int oldH = layer.length;
		int oldW = layer[0].length;

		int newW = oldH;
		int newH = oldW;
		Tile[][] rotated = new Tile[newH][newW];

		for (int x = 0; x < oldW; x++) {
			for (int y = 0; y < oldH; y++) {
				Tile t = layer[y][x];
				int newY = x, newX = oldH-y-1;
				rotated[x][oldH-y-1] = t;
				t.setY(getY() + newY);
				t.setX(getX() + newX);
				t.setOrientation((t.getOrientation() + 1) % 4);
			}
		}

		return rotated;
	}
}
