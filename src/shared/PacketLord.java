package shared;

import java.net.Socket;
import java.util.Queue;
import java.io.IOException;
import java.io.EOFException;
import java.util.ArrayDeque;
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

		startListening();
		start();
	}


	// a queue is needed to handle sending packets
	// otherwise, if multiple threads try to send a packet at the same time,
	// it will send the second in the middle of sending the first.
	// on the receiving end, the packets will be pied and it will error

	// the type of p isn't PacketTo<Dest>, since we are the Destination and we're sending it somewhere else
	private volatile Queue<PacketTo<?>> packetQueue = new ArrayDeque<>();
	public void send(PacketTo<?> p) {
		packetQueue.add(p);
	}
	private void startListening() {
		new Thread(() -> {
			while (true) {
				if (!packetQueue.isEmpty()) {
					actuallySend(packetQueue.remove());
				}
			}
		}).start();
	}
	// DO NOT USE THIS FUNCTION! TO SEND PACKETS, USE public void send(PacketTo<?> p)
	private void actuallySend(PacketTo<?> p) {
		p.setType(p.getClass().getSimpleName());
		p.setID(id);
		try {
			out.writeObject(p);
			out.flush();
		} catch (IOException e) {
			dest.handleException("Something went wrong when sending a packet", e);
		}
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
				PacketTo<Dest> p = (PacketTo<Dest>)in.readObject();
				PacketTo<Dest> castP = (PacketTo<Dest>)Class.forName("shared." + p.getType()).cast(p);
				castP.handle(dest);
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
		}
	}

}
