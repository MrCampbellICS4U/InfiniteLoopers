package shared;

import client.Client;

public class Pong extends PacketTo<Client> {
	private long ms;
	public Pong(long ms) { this.ms = ms; }
	void handle(Client c) { c.handlePing((int)(System.currentTimeMillis() - ms)); }
}
