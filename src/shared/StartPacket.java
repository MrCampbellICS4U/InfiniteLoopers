package shared;

import client.Client;

public class StartPacket extends PacketTo<Client> {
	int x, y;
	public StartPacket(int x, int y) { this.x = x; this.y = y; }
	void handle(Client c) { c.start(getID(), x, y); }
}
