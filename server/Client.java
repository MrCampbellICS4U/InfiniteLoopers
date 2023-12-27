package server;

import shared.*;
import java.io.*;
import java.net.*;

class Client extends Thread {
	private Socket socket;

	Client(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		PacketLord pl = new PacketLord(socket, this);
		pl.send("bruh to client");
	}
}
