package packets;

import client.Client;
import game.world.Tiles.Tile;

public class PartialFOVUpdate extends PacketTo<Client> {
    Tile[][][] tiles;

    public PartialFOVUpdate(Tile[][][] tiles) {
        this.tiles = tiles;
    }

    @Override
    void handle(Client c) {
        c.handlePartialFOVUpdate(tiles);
    }

}
