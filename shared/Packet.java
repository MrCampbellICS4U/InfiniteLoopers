package shared;

public abstract class Packet implements java.io.Serializable {
	private String type;
	String getType() { return type; }
	void setType(String t) { type = t; }
	abstract void handle();
}
