package shared;

import java.net.Socket;
import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// legends speak of a primeval being: the PACKETLORD
// this creatures, shrouded in mystery, have the power to open CONNECTIONS
// which allow them to communicate with each other, sending and receiving messages called PACKETS

// Dest is the type of some external state that we use when receiving packets
public class PacketLord<Dest extends LastWish> extends Thread {
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Dest dest;

	private int id;
	public void setID(int id) { this.id = id; }

	public PacketLord(Socket socket, Dest dest) {
		this.socket = socket;
		this.dest = dest;
		try {
			// ** NEED TO CREATE OUT BEFORE YOU CREATE IN
			// it appears that ObjectInputStreams are very social creatures
			// who will throw sissy fits (block the thread) until they have their ObjectOutputStream
			// so if both ObjectInputStreams are opened first, both the client and server will block
			// and then neither can get around to opening an ObjectOutputStream to unblock the other
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			dest.handleException("IOException when opening PacketLord", e);
		}
		start();
	}

	public void close() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			dest.handleException("IOException when closing PacketLord", e);
		}
	}

	// start receiving packets!
	public void run() {
		try {
			while (true) {
				PacketTo<Dest> p = (PacketTo)in.readObject();
				PacketTo<Dest> castP = (PacketTo)Class.forName("shared." + p.getType()).cast(p);
				castP.handle(dest);
			}
		} catch (EOFException e) {
			dest.handleDisconnection(id, e);
		} catch (IOException e) {
			dest.handleException("IOException while reading packet", e);
		} catch (ClassNotFoundException e) {
			dest.handleException("Undefined packet type; something is very wrong with the packet system", e);
		}
	}

	// the type of p isn't PacketTo<Dest>, since we are the Destination and we're sending it somewhere else
	public void send(PacketTo p) {
		p.setType(p.getClass().getSimpleName());
		p.setID(id);
		try {
			out.writeObject(p);
			out.flush();
		} catch (IOException e) {
			dest.handleException("Something went wrong when sending a packet", e);
		}
	}
}
