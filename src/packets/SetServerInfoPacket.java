package packets;

import client.Client;

public class SetServerInfoPacket extends PacketTo<Client> {
	private long start;
	private int tps;
	private float collisionChecksPerFrame;
	public SetServerInfoPacket(long start, int tps, float collisionChecksPerFrame) {
		this.start = start;
		this.tps = tps;
		this.collisionChecksPerFrame = collisionChecksPerFrame;
	}
	void handle(Client c) { c.setServerInfo((int)(System.currentTimeMillis() - start), tps, collisionChecksPerFrame); }
}
