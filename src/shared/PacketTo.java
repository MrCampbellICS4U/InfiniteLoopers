package shared;

public abstract class PacketTo<Dest> extends UrPacketTo<Dest> {
	// what this packet should do when it's received
	abstract void handle(Dest dest);
}
