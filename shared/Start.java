package shared;

import shared.Packet;
import client.Client;

public class Start extends Packet<Client> {
	void handle(Client c) { c.start(getID()); }
}
