package shared;

import server.Server;

// see note in server/SClient.java
public class ReadyPacket extends PacketTo<Server> {
	void handle(Server s) { s.setClientReady(getID()); }
}
