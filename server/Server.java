package server;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import shared.*;

public class Server {
	public static void main(String[] args) {
		new Server();
	}

	final int port = 2000;
	HashMap<Integer, Client> clients = new HashMap<>();
	Server() {
		System.out.println("Running server on port " + port);

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				int id = nextID();
				Client c = new Client(serverSocket.accept(), this, id);
				clients.put(id, c);
				c.send(new Start());
				System.out.println("Client connected");
			}
		} catch (IOException e) {
			System.out.println("IOException:");
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	int id = 0;
	private int nextID() { return id++; }

	public Client getClient(int id) { return clients.get(id); }
}
