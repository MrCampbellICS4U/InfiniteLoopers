package shared;

import client.Client;

public class PositionPacket extends PacketTo<Client> {
	private PlayerInfo player;
	public PositionPacket(int x, int y, double angle) { this.player = new PlayerInfo(x, y, angle); }
	void handle(Client c) { c.setPosition(player); }
}
