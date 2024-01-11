package shared;

import server.Server;

public class GetServerInfoPacket extends PacketTo<Server> {
	private long start;
	public GetServerInfoPacket() { start = System.currentTimeMillis(); }
	void handle(Server s) { s.sendToClient(getClientID(), new SetServerInfoPacket(start, s.getTPS())); }
}
