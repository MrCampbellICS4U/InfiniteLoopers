package shared;

import client.Client;

public class PositionPacket extends PacketTo<Client> {
	private int x, y;
	public PositionPacket(int x, int y) { this.x = x; this.y = y; }
	void handle(Client c) { c.handlePosition(x, y); }
}
