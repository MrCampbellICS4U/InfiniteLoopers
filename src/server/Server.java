package server;

import java.awt.event.*;
import javax.swing.Timer;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.net.ServerSocket;

import shared.*;
import client.Client;

public class Server implements LastWish, ActionListener {
	public static void main(String[] args) {
		new Server();
	}

	private final int port = 2000;
	private HashMap<Integer, SClient> clients = new HashMap<>(); // map from ids to clients
	Server() {
		Timer tickTimer = new Timer(1000/60, this);
		tickTimer.setActionCommand("tick");
		tickTimer.start();

		Timer secTimer = new Timer(1000, this);
		secTimer.setActionCommand("secUpdate");
		secTimer.start();

		System.out.println("Running server on port " + port);
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				int id = nextID();
				SClient client = new SClient(serverSocket.accept(), this, id);
				clients.put(id, client);
				sendToClient(id, new StartPacket());
				System.out.printf("Client with id %d connected\n", id);
			}
		} catch (IOException e) {
			handleException("IOException when connecting client", e);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("tick")) tick();
		if (e.getActionCommand().equals("secUpdate")) secUpdate();
	}

	private int tps = 0;
	private int tick = 0;
	public int getTPS() { return tps; }
	void tick() {
		tick++;
		for (SClient c : clients.values()) c.update();

		// send all players to all other players
		for (SClient c : clients.values()) c.clearOtherPlayers();
		ArrayList<SClient> clientsList = new ArrayList<>(clients.values());
		for (int i = 0; i < clientsList.size(); i++) {
			SClient c1 = clientsList.get(i);
			PlayerInfo p1 = new PlayerInfo(c1.getX(), c1.getY());
			for (int j = i+1; j < clientsList.size(); j++) {
				SClient c2 = clientsList.get(j);
				PlayerInfo p2 = new PlayerInfo(c2.getX(), c2.getY());

				c1.addOtherPlayer(p2);
				c2.addOtherPlayer(p1);
			}
		}

		for (SClient c : clients.values()) c.sendPackets();
	}

	// gets called once a second
	void secUpdate() {
		tps = tick;
		tick = 0;
	}

	private int id = 0;
	private int nextID() { return id++; }

	public SClient getClient(int id) { return clients.get(id); }
	public void sendToClient(int id, PacketTo<Client> p) { getClient(id).send(p); }

	public void setClientReady(int id) { getClient(id).setReady(); }

	public void handleInput(int id, Input i, InputState is) { getClient(id).handleInput(i, is); }

	public void handleException(String message, Exception e) {
		System.out.println(message);
		e.printStackTrace();
	}
	public void handleDisconnection(int id, Exception e) {
		System.out.printf("Client with id %d disconnected\n", id);
		getClient(id).close();
		clients.remove(id);
	}
}
