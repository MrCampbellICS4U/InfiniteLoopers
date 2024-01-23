package packets;

import client.Client;
import world.Tiles.Tile;

/**
 * Represents a packet that updates a tile for a client.
 */
public class TileUpdate extends PacketTo<Client> {
    Tile tile;

    /**
     * Constructs a new TileUpdate object with the given Tile.
     *
     * @param tile The Tile object to be associated with this TileUpdate.
     */
    public TileUpdate(Tile tile) {
        this.tile = tile;
    }

    /**
     * Handles the update of a tile for a given client.
     *
     * @param c The client to handle the update for.
     */
    @Override
    void handle(Client c) {
        c.updateTile(tile);
    }
}
