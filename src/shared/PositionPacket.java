package shared;

import client.Client;

public class PositionPacket extends PacketTo<Client> {
	private PlayerInfo player;
	public PositionPacket(int x, int y) { this.player = new PlayerInfo(x, y); }
	void handle(Client c) { c.setPosition(player); }
}
