package packets;

import shared.*;
import server.Server;

/**
 * Represents an input packet that is sent to the server.
 * This packet contains information about the input and its state.
 * It is used to handle input on the server side.
 *
 * @param <Server> The type of server that will handle this input packet.
 */
public class InputPacket extends PacketTo<Server> {
	private Input i;
	// things like pressing a movement key need a state (for if the key was pressed or released)
	// but things like attacking don't, so `is` isn't used by all packets
	private InputState is;
	public InputPacket(Input i) { this.i = i; }
	public InputPacket(Input i, InputState is) { this.i = i; this.is = is; }
	void handle(Server s) { s.handleInput(getClientID(), i, is); }
}
