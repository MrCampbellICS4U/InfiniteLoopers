package shared;

import shared.*;
import server.Server;

public class InputPacket extends PacketTo<Server> {
	private Input i;
	private InputState is;
	public InputPacket(Input i, InputState is) { this.i = i; this.is = is; }
	void handle(Server s) { s.handleInput(getClientID(), i, is); }
}
