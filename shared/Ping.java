package shared;

import server.Server;
import server.Client;

public class Ping extends Packet<Server> {
	private long ms;
	public Ping() { ms = System.currentTimeMillis(); }
	void handle(Server s) { s.getClient(getID()).send(new Pong(ms)); }
}
