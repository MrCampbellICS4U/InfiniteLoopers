package shared;

import server.Server;

public class PingPacket extends PacketTo<Server> {
	private long ms;
	public PingPacket() { ms = System.currentTimeMillis(); }
	void handle(Server s) { s.sendToClient(getID(), new PongPacket(ms)); }
}
