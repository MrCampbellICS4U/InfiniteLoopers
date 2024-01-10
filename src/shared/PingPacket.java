package shared;

import server.Server;

public class PingPacket extends PacketTo<Server> {
	private long start;
	public PingPacket() { start = System.currentTimeMillis(); }
	void handle(Server s) { s.sendToClient(getID(), new PongPacket(start)); }
}
