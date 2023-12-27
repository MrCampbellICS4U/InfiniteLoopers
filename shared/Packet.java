package shared;

//public abstract class Packet implements java.io.Serializable {
//	String type;
//}

public class Packet implements java.io.Serializable {
	String type;
	long time;
	public Packet(String t, long tm) { type = t; time = tm; }
}
