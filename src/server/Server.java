package server;

import java.awt.event.*;
import javax.swing.Timer;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.net.ServerSocket;

import shared.*;
import packets.*;
import client.Client;
import game.world.WorldGenerator;
import game.world.Tiles.Tile;

public class Server implements LastWish, ActionListener {
	public static void main(String[] args) {
		new Server();
	}

	private final int port = 2000;
	private HashMap<Integer, SClient> clients = new HashMap<>(); // map from ids to clients
	private Tile[][][] map;

	private Chunker chunker = new Chunker(GlobalConstants.CHUNK_WIDTH, GlobalConstants.CHUNK_HEIGHT, GlobalConstants.WORLD_WIDTH, GlobalConstants.WORLD_HEIGHT);
	Server() {
		Timer tickTimer = new Timer(1000 / 60, this);
		tickTimer.setActionCommand("tick");
		tickTimer.start();

		Timer secTimer = new Timer(1000, this);
		secTimer.setActionCommand("secUpdate");
		secTimer.start();

		map = new WorldGenerator(100, 100, 3).generateWorld();
		// // print the map for debug
		// for (int i = 0; i < map.length; i++) {
		// System.out.println("Layer " + i);
		// for (int j = 0; j < map[0].length; j++) {
		// for (int k = 0; k < map[0][0].length; k++) {
		// if (map[i][j][k] == null)
		// System.out.print("null ");
		// else
		// System.out.print(map[i][j][k].getType() + " ");
		// }
		// System.out.println();
		// }
		// System.out.println();
		// }

		System.out.println("Running server on port " + port);
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				int id = nextID();
				SClient client = new SClient(serverSocket.accept(), this, id, chunker);
				clients.put(id, client);
				sendToClient(id, new StartPacket());
				System.out.printf("Client with id %d connected\n", id);
			}
		} catch (IOException e) {
			handleException("IOException when connecting client", e);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("tick"))
			tick();
		if (e.getActionCommand().equals("secUpdate"))
			secUpdate();
	}

	private int tps = 0;
	private int tick = 0;

	public int getTPS() {
		return tps;
	}

	private int collisionChecks = 0;
	private float collisionChecksPerFrame = 0;
	public float getCollisionChecksPerFrame() { return collisionChecksPerFrame; }

	void tick() {
		tick++;
		for (SClient c : clients.values()) {
			c.updatePlayer();
			// send the client their visible tiles
			//c.handleVisibleTileUpdates(map);
		}

		collisionChecks += chunker.checkCollisions();

		// send all players to all other players
		for (SClient c : clients.values())
			c.clearOtherPlayers();
		ArrayList<SClient> clientsList = new ArrayList<>(clients.values());
		for (int i = 0; i < clientsList.size(); i++) {
			SClient c1 = clientsList.get(i);
			for (int j = i + 1; j < clientsList.size(); j++) {
				SClient c2 = clientsList.get(j);
				c1.addOtherPlayer(c2.getInfo());
				c2.addOtherPlayer(c1.getInfo());
			}
		}

		for (SClient c : clients.values())
			c.sendPackets();
	}

	// gets called once a second
	void secUpdate() {
		tps = tick;
		tick = 0;

		collisionChecksPerFrame = collisionChecks / (float)tps;
		collisionChecks = 0;
	}

	private int id = 0;

	private int nextID() {
		return id++;
	}

	public SClient getClient(int id) {
		return clients.get(id);
	}

	public void sendToClient(int id, PacketTo<Client> p) {
		getClient(id).send(p);
	}

	public void setClientReady(int id) {
		getClient(id).setReady();
	}

	public void handleInput(int id, Input i, InputState is) {
		getClient(id).handleInput(i, is);
	}

	public void setPlayerRotation(int id, double angle) {
		getClient(id).setAngle(angle);
	}

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
