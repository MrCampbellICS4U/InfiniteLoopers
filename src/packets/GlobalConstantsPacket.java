package packets;

import client.Client;
import shared.GlobalConstants;

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
