package shared;

import java.util.ArrayList;

// if packets don't arrive together, they make the game look jittery and out of sync
public class PacketListTo<Dest> extends UrPacketTo<Dest> {
	private ArrayList<PacketTo<Dest>> packets;
	public PacketListTo(ArrayList<PacketTo<Dest>> packets) { this.packets = packets; }
	public ArrayList<PacketTo<Dest>> getPackets() { return packets; }
}
