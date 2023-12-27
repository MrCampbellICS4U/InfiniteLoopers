package shared;

import java.io.*;
import java.net.*;

public class PacketLord<State> extends Thread {
	State state;
	//ObjectOutputStream out;
	BufferedReader in;
	PrintWriter out;
	public PacketLord(Socket socket, State state) {
		try {
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		//out = new ObjectOutputStream(socket.getOutputStream());
		out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {}
		start();
	}
	public void run() {
		try {
		//Packet p;
		//while (p = (Packet)in.readObject()) {
		//	IncommingPacket inc = Class.forName(p.type).cast(p);
		//	inc.handle(state);
		//}

		String s;
		while ((s = in.readLine()) != null) {
			System.out.println("Received " + s);
		}
		} catch (IOException e) {}
	}
	//void send(Packet p) {
	public void send(String s) {
		//out.writeObject(p);
		out.println(s);
		System.out.println("sending " + s);
	}
}
