package client;

import shared.*;
import java.io.*;
import java.net.*;

class Client {
	public static void main(String[] args) {
		new Client("127.0.0.1", 2000);
	}

	Client(String ip, int port) {
		try {
		Socket socket = new Socket(ip, port);
		PacketLord pl = new PacketLord(socket, this);

		pl.send(new Packet("ping", System.currentTimeMillis()));
		} catch (Exception e) {}
	}
}
