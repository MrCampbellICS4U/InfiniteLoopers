package packets;

import server.Server;

// see note in server/SClient.java
/**
 * Represents a packet that indicates the client is ready.
 * This packet is sent to the server.
 */
public class ReadyPacket extends PacketTo<Server> {
	String name = "";
	public ReadyPacket(String name){
		this.name = name;
	}
		void handle(Server s) { s.setClientReady(getClientID(), name); }
}
