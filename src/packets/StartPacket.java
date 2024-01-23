package packets;

import client.Client;

/**
 * Represents a packet that is sent to the client to initiate a start action.
 * This packet is handled by the client to perform the start action.
 */
public class StartPacket extends PacketTo<Client> {
	void handle(Client c) {
		c.start(getClientID());
	}
}
