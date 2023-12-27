package shared;

import java.io.*;
import java.net.*;

public class PacketLord<State> extends Thread {
	State state;
	ObjectOutputStream out;
	ObjectInputStream in;
	public PacketLord(Socket socket, State state) {
		try {
		// ** NEED TO CREATE OUT BEFORE YOU CREATE IN
		// this is being done on both the client and the server, and an ObjectInputStream
		// will block until the corresponding ObjectOutputStream is opened
		// so if both ObjectInputStreams are opeend first, both the client and server will block
		// and then neither can get around to opening an ObjectOutputStream to unblock the other
		out = new ObjectOutputStream(socket.getOutputStream());

		in = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {}
		start();
	}
	public void run() {
		try {
		Packet p;
		while ((p = (Packet)in.readObject()) != null) {
			System.out.println("Received " + p.type + ", took " + (System.currentTimeMillis() - p.time) + " ms");
			//IncommingPacket inc = Class.forName(p.type).cast(p);
			//inc.handle(state);
		}

		} catch (Exception e) {}
	}
	public void send(Packet p) {
		System.out.println("sending " + p.type);
		try { out.writeObject(p); out.flush(); } catch (Exception e) {}
	}
}
