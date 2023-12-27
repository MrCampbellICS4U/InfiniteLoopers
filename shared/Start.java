package shared;

import client.Client;

public class Start extends PacketTo<Client> {
	void handle(Client c) { c.start(getID()); }
}
