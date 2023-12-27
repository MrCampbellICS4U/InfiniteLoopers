package server;

import java.util.HashMap;
import java.io.IOException;
import java.net.ServerSocket;

import shared.*;

public class Server implements LastWish {
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
			handleException("IOException when connecting client", e);
		}
	}

	int id = 0;
	private int nextID() { return id++; }

	public Client getClient(int id) { return clients.get(id); }
	public void sendToClient(int id, PacketTo p) { clients.get(id).send(p); }

	public void handleException(String message, Exception e) {
		System.out.println(message);
		if (!e.getClass().getName().equals("java.io.EOFException")) {
			// don't print the stack trace if the connection was closed
			// since that isn't usually an error (but java still uses an exception to signal it :rage:)
			e.printStackTrace();
		}
	}
}
