package packets;

import server.Server;

public class GetServerInfoPacket extends PacketTo<Server> {
	private long start;

	/**
	 * Constructs a new instance of the GetServerInfoPacket.
	 * This packet is used to request server information from the server.
	 * The start time is set to the current system time in milliseconds.
	 */
	public GetServerInfoPacket() {
		start = System.currentTimeMillis();
	}

	/**
	 * Sends a SetServerInfoPacket to the client with the specified server
	 * information.
	 *
	 * @param s The server instance
	 */
	void handle(Server s) {
		s.sendToClient(getClientID(), new SetServerInfoPacket(start, s.getTPS(), s.getCollisionChecksPerFrame()));
	}
}
