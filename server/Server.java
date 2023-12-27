package server;

import java.util.HashMap;
import java.io.IOException;
import java.net.ServerSocket;

import shared.*;

public class Server implements LastWish {
	public static void main(String[] args) {
		new Server();
	}

	private final int port = 2000;
	private HashMap<Integer, Client> clients = new HashMap<>(); // map from ids to clients
	Server() {
		System.out.println("Running server on port " + port);

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				int id = nextID();
				clients.put(id, new Client(serverSocket.accept(), this, id));
				sendToClient(id, new StartPacket());
				System.out.println("Client connected");
			}
		} catch (IOException e) {
			handleException("IOException when connecting client", e);
		}
	}

	private int id = 0;
	private int nextID() { return id++; }

	public Client getClient(int id) { return clients.get(id); }
	public void sendToClient(int id, PacketTo p) { getClient(id).send(p); }

	public void handleException(String message, Exception e) {
		System.out.println(message);
		e.printStackTrace();
	}
	public void handleDisconnection(int id, Exception e) {
		System.out.println("Client disconnected");
		getClient(id).close();
		clients.remove(id);
	}
}
