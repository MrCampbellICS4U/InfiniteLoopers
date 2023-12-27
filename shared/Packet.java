package shared;

public abstract class Packet<State> implements java.io.Serializable {
	private String type;
	String getType() { return type; }
	void setType(String t) { type = t; }

	abstract void handle(State state);

	int id;
}
