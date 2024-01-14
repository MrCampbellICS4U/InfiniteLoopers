package packets;

import java.io.Serializable;

public abstract class PacketTo<Dest> implements Serializable {
	// what this packet should do when it's received
	abstract void handle(Dest dest);


	// server id of the client that this packet is coming from/going to
	private int id;
	public void setID(int id) { this.id = id; }

	// returns the id of the client sending/receiving this packet
	public int getClientID() { return id; }
}
