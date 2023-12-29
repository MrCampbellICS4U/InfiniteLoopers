package shared;

// the ur-packet: the original packet which begot all others
public abstract class UrPacketTo<Dest> implements java.io.Serializable {
	// used by the PacketLord when receiving packets
	private String type;
	String getType() { return type; }
	void setType(String type) { this.type = type; }

	// server id of the client that this packet is coming from/going to
	private int id;
	public void setID(int id) { this.id = id; }
	public int getID() { return id; }
}
