package shared;

import shared.Packet;
import server.Server;

public class Ping extends Packet<Server> {
	private long ms;
	public Ping() { ms = System.currentTimeMillis(); }
	void handle(Server s) { s.sendToClient(getID(), new Pong(ms)); }
}
