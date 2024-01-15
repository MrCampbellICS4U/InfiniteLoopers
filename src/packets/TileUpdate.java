package packets;

import client.Client;
import game.world.Tiles.Tile;

public class TileUpdate extends PacketTo<Client> {
    Tile tile;

    public TileUpdate(Tile tile) {
        this.tile = tile;
    }

    @Override
    void handle(Client c) {
        c.updateTile(tile);
    }
}
