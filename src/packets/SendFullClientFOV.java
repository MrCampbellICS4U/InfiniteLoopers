package packets;

import client.Client;
import world.Tiles.Tile;

/**
 * Represents a packet to send the full field of view (FOV) to a client.
 * This packet contains the information of the tiles that are visible to the client.
 *
 * @param tiles The 3D array of tiles representing the full field of view
 */
public class SendFullClientFOV extends PacketTo<Client> {
    Tile[][][] tiles;

    public SendFullClientFOV(Tile[][][] tiles) {
        this.tiles = tiles;
    }

    @Override
    void handle(Client c) {
        c.setNextVisibleTiles(tiles);
    }

}
