package game.world.Structures;

import game.world.Tiles.Tile;

public class Structure {
    Tile[][][] tiles;
    int x, y, z; // top left corner of the structues bounding box

    public Structure(int x, int y, int z, Tile[][][] tiles, int orientation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tiles = tiles;
        rotate(tiles, orientation);
    }

    private void rotate(int orientation) {
        // takes the array of tiles and rotates it clock-wise creating a new array with
        // maybe different dimensions
        // 0 = 0 degrees, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
        if (orientation == 0)
            return;
        // it rotates the array 90 degrees clockwise about its center

    }

    public static void rotate(Tile[][][] tiles, int orientation) {
        for (int z = 0; z < tiles.length; z++) {
            rotateLayer(tiles[z], orientation);
        }
    }

    private static void rotateLayer(Tile[][] layer, int orientation) {
        int n = layer.length;
        for (int i = 0; i < n / 2; i++) {
            for (int j = i; j < n - i - 1; j++) {
                Tile temp = layer[i][j];
                int newX = j, newY = i;

                for (int k = 0; k < orientation; k++) {
                    int tempX = newX;
                    newX = newY;
                    newY = n - 1 - tempX;
                }

                temp.setX(newX);
                temp.setY(newY);
                temp.setOrientation((temp.getOrientation() + orientation) % 4);

                // Perform rotation of tiles
                layer[i][j] = layer[newY][n - 1 - newX];
                layer[newY][n - 1 - newX] = layer[n - 1 - newX][n - 1 - newY];
                layer[n - 1 - newX][n - 1 - newY] = layer[n - 1 - newY][i];
                layer[n - 1 - newY][i] = temp;
            }
        }
    }

}
