package packets;

import java.lang.reflect.Array;
import java.util.ArrayList;

import client.Client;
import game.world.Tiles.Tile;

public class PartialFOVUpdate extends PacketTo<Client> {
    Tile[][][] tiles;

    public PartialFOVUpdate(Tile[][][] tiles) {
        this.tiles = tiles;
    }

    public PartialFOVUpdate(ArrayList<Tile> tiles) {
        this.tiles = new Tile[1][1][tiles.size()];
        for (int i = 0; i < tiles.size(); i++) {
            this.tiles[0][0][i] = tiles.get(i);
        }
    }

    @Override
    void handle(Client c) {
        c.handlePartialFOVUpdate(tiles);
    }

}
