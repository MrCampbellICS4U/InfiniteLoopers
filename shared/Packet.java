package shared;

//public abstract class Packet implements java.io.Serializable {
//	String type;
//}

public class Packet implements java.io.Serializable {
	String type;
	public Packet(String t) { type = t; }
}
