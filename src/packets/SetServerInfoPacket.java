package packets;

import client.Client;

public class SetServerInfoPacket extends PacketTo<Client> {
	private long start;
	private int tps;
	private double collisionChecksPerFrame;
	public SetServerInfoPacket(long start, int tps, double collisionChecksPerFrame) {
		this.start = start;
		this.tps = tps;
		this.collisionChecksPerFrame = collisionChecksPerFrame;
	}
	void handle(Client c) { c.setServerInfo((int)(System.currentTimeMillis() - start), tps, collisionChecksPerFrame); }
}
