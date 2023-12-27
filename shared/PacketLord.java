package shared;

import java.net.Socket;
import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
			// this is being done on both the client and the server, and an ObjectInputStream
			// will block until the corresponding ObjectOutputStream is opened
			// so if both ObjectInputStreams are opeend first, both the client and server will block
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
