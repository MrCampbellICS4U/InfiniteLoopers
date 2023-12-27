package shared;

public abstract class Packet<State> implements java.io.Serializable {
	// what this packet should do when it's received
	abstract void handle(State state);


	// used by the PacketLord when receiving packets
	private String type;
	String getType() { return type; }
	void setType(String t) { type = t; }

	// server id of the client that this packet is coming from/going to
	private int id;
	public void setID(int id) { this.id = id; }
	public int getID() { return id; }
}
