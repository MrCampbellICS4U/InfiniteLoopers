package packets;

import client.Client;
import shared.GlobalConstants;

/**
 * Represents a packet containing global constants to be sent to a client.
 * This packet is responsible for updating the global constants of the client.
 *
 * @param <Client> The type of client that will receive this packet.
 */
public class GlobalConstantsPacket extends PacketTo<Client> {
    GlobalConstants gc;

    public GlobalConstantsPacket(GlobalConstants gc) {
        this.gc = gc;

    }

    @Override
    void handle(Client c) {
        c.setGlobalConstants(this.gc);
    }

}
