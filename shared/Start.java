package shared;

import shared.Packet;
import client.Client;

public class Start extends Packet<Client> {
	public Start(int id) { this.id = id; }
	void handle(Client c) { c.start(id); }
}
