package packets;

import java.lang.reflect.Array;
import java.util.ArrayList;

import client.Client;
import world.Tiles.Tile;

/**
 * Represents a packet that contains a partial field of view (FOV) update.
 * This packet is sent to the client.
 */
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
