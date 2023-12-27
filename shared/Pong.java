package shared;

import shared.Packet;
import client.Client;

public class Pong extends Packet<Client> {
	private long ms;
	public Pong(int id, long ms) { this.id = id; this.ms = ms; }
	void handle(Client c) { c.handlePing((int)(System.currentTimeMillis() - ms)); }
}
