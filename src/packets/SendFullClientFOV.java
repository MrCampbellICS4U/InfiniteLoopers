package packets;

import client.Client;
import game.world.Tiles.Tile;

public class SendFullClientFOV extends PacketTo<Client> {
    Tile[][][] tiles;

    @Override
    void handle(Client c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handle'");
    }

}
