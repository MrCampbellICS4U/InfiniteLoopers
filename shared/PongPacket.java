package shared;

import client.Client;

public class PongPacket extends PacketTo<Client> {
	private long ms;
	public PongPacket(long ms) { this.ms = ms; }
	void handle(Client c) { c.handlePing((int)(System.currentTimeMillis() - ms)); }
}
