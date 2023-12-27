package shared;

import shared.Packet;

public class Ping extends Packet {
	void handle() { System.out.println("Ping"); }
}
