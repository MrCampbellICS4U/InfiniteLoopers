package packets;

import java.lang.reflect.Array;
import java.util.ArrayList;

import client.Client;
import game.world.Tiles.Tile;

public class PartialFOVUpdate extends PacketTo<Client> {
    ArrayList<Tile> tiles;

    public PartialFOVUpdate(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    @Override
    void handle(Client c) {
        c.handlePartialFOVUpdate(tiles);
    }

}
