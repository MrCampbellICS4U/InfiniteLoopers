package shared;

import shared.Packet;
import server.Server;

public class Ping extends Packet<Server> {
	private long ms;
	public Ping(int id) { this.id = id; ms = System.currentTimeMillis(); }
	void handle(Server s) { s.sendToClient(id, new Pong(id, ms)); }
}
