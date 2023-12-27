package shared;

import server.Server;

public class Ping extends PacketTo<Server> {
	private long ms;
	public Ping() { ms = System.currentTimeMillis(); }
	void handle(Server s) { s.sendToClient(getID(), new Pong(ms)); }
}
