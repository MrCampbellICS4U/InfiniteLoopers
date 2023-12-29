package shared;

import java.net.Socket;
import java.io.IOException;
import java.io.EOFException;
import java.net.SocketException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;

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
			// disable nagle's algorithm to speed up sending packets
			socket.setTcpNoDelay(true);

			// ** NEED TO CREATE OUT BEFORE YOU CREATE IN
			// it appears that ObjectInputStreams are very social creatures
			// who will throw sissy fits (block the thread) until they have their ObjectOutputStream
			// so if both ObjectInputStreams are opened first, both the client and server will block
			// and then neither can get around to opening an ObjectOutputStream to unblock the other

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream())); // buffering might increase performance?
		} catch (SocketException e) {
			dest.handleException("SocketException when disabling Nagle's algorithm", e);
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
				UrPacketTo<Dest> origP = (UrPacketTo<Dest>)in.readObject();
				System.out.println(origP.getType());

				if (origP.getType().equals("PacketListTo")) {
					// packet list
					PacketListTo<Dest> pList = (PacketListTo<Dest>)origP;
					for (PacketTo<Dest> p : pList.getPackets()) handlePacket(p);
				} else {
					// normal single packet
					handlePacket((PacketTo<Dest>)origP);
				}
			}
		} catch (EOFException e) {
 			// this what's supposed to be thrown on disconnection
			dest.handleDisconnection(id, e);
		} catch (SocketException e) {
			// sometimes when disconnecting it throws this for some reason, with the message "Connection reset"
			dest.handleDisconnection(id, e);
		} catch (IOException e) {
			dest.handleException("IOException while reading packet", e);
		} catch (ClassNotFoundException e) {
			dest.handleException("Undefined packet type; something is very wrong with the packet system", e);
		} catch (ClassCastException e) {
			dest.handleException("What even...", e);
		}
	}

	private void handlePacket(PacketTo<Dest> origP) throws ClassNotFoundException {
		PacketTo<Dest> p = (PacketTo<Dest>)Class.forName("shared." + origP.getType()).cast(origP);
		p.handle(dest);
	}

	// the type of p isn't UrPacketTo<Dest>, since we are the Destination and we're sending it somewhere else
	public void send(UrPacketTo<?> p) {
		setInfo(p);
		if (p.getType().equals("PacketListTo")) {
			PacketListTo<?> pList = (PacketListTo<?>)p;
			for (PacketTo<?> pac : pList.getPackets()) setInfo(pac);
		}

		try {
			out.writeObject(p);
			out.flush();
		} catch (IOException e) {
			dest.handleException("Something went wrong when sending a packet", e);
		}
	}

	private void setInfo(UrPacketTo<?> p) {
		p.setType(p.getClass().getSimpleName());
		p.setID(id);
	}
}
