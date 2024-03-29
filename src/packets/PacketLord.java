package packets;

import java.net.Socket;
import java.io.IOException;
import java.io.EOFException;
import java.net.SocketException;

import shared.LastWish;

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

	/**
	 * Returns the ID of the object.
	 *
	 * @return The ID of the object.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Sets the ID of the object.
	 *
	 * @param id The ID to set
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * Constructs a new PacketLord object with the given socket and destination.
	 *
	 * @param socket The socket to communicate with
	 * @param dest   The destination to handle exceptions
	 */
	public PacketLord(Socket socket, Dest dest) {
		this.socket = socket;
		this.dest = dest;
		try {
			// disable nagle's algorithm to speed up sending packets
			socket.setTcpNoDelay(true);

			// ** NEED TO CREATE OUT BEFORE YOU CREATE IN
			// it appears that ObjectInputStreams are very social creatures who will throw hissy fits (block the thread) until they have their ObjectOutputStream so if both ObjectInputStreams are opened first, both the client and server will block and then neither can get around to opening an ObjectOutputStream to unblock the other

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream())); // buffering might increase
																							// performance?
		} catch (SocketException e) {
			dest.handleException("SocketException when disabling Nagle's algorithm", e);
		} catch (IOException e) {
			dest.handleException("IOException when opening PacketLord", e);
		}

		start();
	}

	// this is synchronized so that if multiple threads try to send packets, the
	// packets don't get mixed together
	// which would cause the receiver to just spontaneously decide to die as it
	// tries to read pied packets
	// the type of p isn't PacketTo<Dest>, since we are the Destination and we're
	// sending it somewhere else
	public synchronized void send(PacketTo<?> p) {
		try {
			p.setID(id);
			out.writeObject(p);
			out.flush();
		} catch (SocketException e) {
			// this could mean that a client disconnected
			if (e.getMessage().equals("Socket closed")) {
				dest.handleDisconnection(id, e);
			} else
				dest.handleException("Socket exception when sending a packet", e);
		} catch (IOException e) {
			dest.handleException("IO exception when sending a packet", e);
		}
	}

	/**
	 * Closes the connection and releases any resources associated with it.
	 * If the connection is already closed, this method has no effect.
	 * 
	 * @throws IOException if an I/O error occurs while closing the connection
	 */
	public void close() {
		try {
			// we need to check if it's already closed because of java's multiple ways of signalling that the socket has closed
			if (!socket.isClosed()) {
				in.close();
				out.close();
				socket.close();
			}
		} catch (IOException e) {
			dest.handleException("IOException when closing PacketLord", e);
		}
	}

	// start receiving packets!
	public void run() {
		try {
			while (true) {
				PacketTo<Dest> p = (PacketTo<Dest>) in.readObject();
				p.handle(dest);
			}
		} catch (EOFException e) {
			// this what's supposed to be thrown on disconnection
			dest.handleDisconnection(id, e);
		} catch (SocketException e) {
			// sometimes when disconnecting it throws this for some reason, with the message
			// "Connection reset"
			// also see https://stackoverflow.com/a/10241044 ... this is a nightmare
			dest.handleDisconnection(id, e);
		} catch (IOException e) {
			// this could mean that a client disconnected while sending a packet
			if (e.getMessage().equals("Stream closed")) {
				dest.handleDisconnection(id, e);
			} else
				dest.handleException("IOException while reading packet", e);
		} catch (ClassNotFoundException e) {
			dest.handleException("Undefined packet type; something is very wrong with the packet system", e);
		}
	}

}
