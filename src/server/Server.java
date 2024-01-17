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
import collision.*;
import entities.Renderable;

public class Server implements LastWish, ActionListener {
	public static void main(String[] args) {
		new Server();
	}

	private final int port = 2000;
	private HashMap<Integer, SClient> clients = new HashMap<>(); // map from ids to clients
	private Tile[][][] map;

	private Chunker chunker = new Chunker(GlobalConstants.CHUNK_WIDTH, GlobalConstants.CHUNK_HEIGHT, GlobalConstants.WORLD_WIDTH, GlobalConstants.WORLD_HEIGHT);
	Server() {
		Timer tickTimer = new Timer(1000 / GlobalConstants.TPS, this);
		tickTimer.setActionCommand("tick");
		tickTimer.start();

		Timer secTimer = new Timer(1000, this);
		secTimer.setActionCommand("secUpdate");
		secTimer.start();

		map = new WorldGenerator(GlobalConstants.WORLD_TILE_WIDTH, GlobalConstants.WORLD_TILE_HEIGHT, 3)
				.generateWorld();

		System.out.println("Running server on port " + port);
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				int id = nextID();
				SClient client = new SClient(serverSocket.accept(), this, id, chunker, map);
				clients.put(id, client);
				renderables.add(client);
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
	private double collisionChecksPerFrame = 0;
	public double getCollisionChecksPerFrame() { return collisionChecksPerFrame; }

	private ArrayList<Renderable> renderables = new ArrayList<>();
	public void addRenderable(Renderable r) { renderables.add(r); }

	void tick() {
		tick++;
		for (int i = 0; i < renderables.size(); i++) {
			renderables.get(i).update();
		}

		collisionChecks += chunker.checkCollisions();

		// send all entities to all entities
		for (SClient c : clients.values())
			c.clearEntities();

		for (SClient c : clients.values()) {
			for (Renderable r : renderables) {
				c.addEntity(r.getInfo());
			}
		}

		for (SClient c : clients.values())
			c.sendPackets();
	}

	// gets called once a second
	void secUpdate() {
		tps = tick;
		tick = 0;

		collisionChecksPerFrame = collisionChecks / (double)tps;
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
		SClient c = getClient(id);
		c.remove();
		renderables.remove(c);
		clients.remove(id);
	}
}
