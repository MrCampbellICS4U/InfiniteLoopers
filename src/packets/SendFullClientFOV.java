package packets;

import client.Client;
import game.world.Tiles.Tile;

public class SendFullClientFOV extends PacketTo<Client> {
    Tile[][][] tiles;

    public SendFullClientFOV(Tile[][][] tiles) {
        this.tiles = tiles;
    }

    @Override
    void handle(Client c) {
        // c.setVisibleTiles(tiles);
    }

}
