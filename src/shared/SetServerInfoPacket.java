package shared;

import client.Client;

public class SetServerInfoPacket extends PacketTo<Client> {
	private long start;
	private int tps;
	public SetServerInfoPacket(long start, int tps) { this.start = start; this.tps = tps; }
	void handle(Client c) { c.setServerInfo((int)(System.currentTimeMillis() - start), tps); }
}
