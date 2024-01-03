package shared;

import client.Client;

public class PongPacket extends PacketTo<Client> {
	private long start;
	public PongPacket(long start) { this.start = start; }
	void handle(Client c) { c.setPing((int)(System.currentTimeMillis() - start)); }
}
