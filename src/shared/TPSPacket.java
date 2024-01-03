package shared;

import client.Client;

public class TPSPacket extends PacketTo<Client> {
	private int tps;
	public TPSPacket(int tps) { this.tps = tps; }
	void handle(Client c) { c.setTPS(tps); }
}
