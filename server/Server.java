package server;

import java.io.*;
import java.net.*;

import server.*;
import shared.*;

class Server {
	public static void main(String[] args) {
		new Server();
	}

	final int port = 2000;
	Server() {
		System.out.println("Running server on port " + port);

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) new PacketLord(serverSocket.accept(), this).send("bruh to client");
		} catch (IOException e) {
			System.out.println("IOException:");
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}