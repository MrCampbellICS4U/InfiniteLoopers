package packets;

import client.Client;

public class StartPacket extends PacketTo<Client> {
	void handle(Client c) { c.start(getClientID()); }
}
