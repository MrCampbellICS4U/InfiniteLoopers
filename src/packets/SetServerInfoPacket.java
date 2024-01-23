package packets;

import client.Client;

/**
 * Represents a packet to set server information for a client.
 * This packet is sent from the server to the client.
 */
public class SetServerInfoPacket extends PacketTo<Client> {
	private long start;
	private int tps;
	private double collisionChecksPerFrame;

	public SetServerInfoPacket(long start, int tps, double collisionChecksPerFrame) {
		this.start = start;
		this.tps = tps;
		this.collisionChecksPerFrame = collisionChecksPerFrame;
	}

	/**
	 * Updates the server information for a client.
	 *
	 * @param c                       The client to update.
	 * @param start                   The start time of the server in milliseconds.
	 * @param tps                     The ticks per second of the server.
	 * @param collisionChecksPerFrame The number of collision checks per frame.
	 */
	void handle(Client c) {
		c.setServerInfo((int) (System.currentTimeMillis() - start), tps, collisionChecksPerFrame);
	}
}
