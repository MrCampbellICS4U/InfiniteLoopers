package shared;

import java.io.*;
import java.net.*;

import shared.Ping;

public class PacketLord extends Thread {
	Socket socket;
	ObjectOutputStream out;
	ObjectInputStream in;
	public PacketLord(Socket socket) {
		try {
		this.socket = socket;
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
	public void close() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("IOException when closing a PacketLord: " + e.getMessage());
			System.exit(1);
		}
	}
	public void run() {
		Packet p;
		try {
			while ((p = (Packet)in.readObject()) != null) {
				System.out.println("Received " + p.getType());
				p = (Packet)Class.forName("shared."+p.getType()).cast(p);
				p.handle();
			}
		} catch (Exception e) {
			System.out.println("Unknown packet type bozo");
			e.printStackTrace();
		}
	}
	public void send(Packet p) {
		p.setType(p.getClass().getSimpleName());
		System.out.println("Sending " + p.getType());
		try { out.writeObject(p); out.flush(); } catch (Exception e) { e.printStackTrace(); }
	}
}
