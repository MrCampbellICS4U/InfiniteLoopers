package server;

import java.awt.Dimension;
import java.awt.event.*;
import java.awt.*;
import collision.Rectangle;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.*;

import shared.*;
import packets.*;
import client.Client;
import world.WorldGenerator;
import world.Tiles.Tile;
import collision.*;
import entities.Entity;

public class Server implements LastWish, ActionListener {
	JFrame serverUI;
	JPanel serverDrawing;
	JTextField seedField, wTextField, hTextField;
	JButton startServer;

	public static void main(String[] args) {
		new Server();
	}

	private final int port = 2000;
	private HashMap<Integer, SClient> clients = new HashMap<>(); // map from ids to clients
	private Tile[][][] map;
	public GlobalConstants gc;
	public int seed, worldHeight, worldWidth;
	private Chunker chunker;
	private long lastTickTime = System.currentTimeMillis();

	Server() {
		serverUI = new JFrame("Server UI");
		serverUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverDrawing = new JPanel();
		serverDrawing.setPreferredSize(new Dimension(500, 500));
		seedField = new JTextField("Enter Seed Here", 25);
		wTextField = new JTextField("Enter Width Here", 25);
		hTextField = new JTextField("Enter Height Here", 25);
		startServer = new JButton("Start");
		startServer.setActionCommand("start");
		startServer.addActionListener(this);
		serverDrawing.add(seedField);
		serverDrawing.add(wTextField);
		serverDrawing.add(hTextField);
		serverDrawing.add(startServer);

		serverUI.add(serverDrawing);
		serverUI.pack();
		serverUI.setLocationRelativeTo(null);
		serverUI.setResizable(false);

		serverUI.setVisible(true);

	}

	public void startServer() {
		this.gc = new GlobalConstants();

		gc.SEED = seed;
		gc.WORLD_TILE_HEIGHT = worldHeight;
		gc.WORLD_TILE_WIDTH = worldWidth;

		gc.WORLD_HEIGHT = worldHeight * gc.TILE_HEIGHT;
		gc.WORLD_WIDTH = worldWidth * gc.TILE_WIDTH;
		this.chunker = new Chunker(gc.CHUNK_WIDTH, gc.CHUNK_HEIGHT,
				gc.WORLD_WIDTH, gc.WORLD_HEIGHT);

		map = new WorldGenerator(gc.WORLD_TILE_WIDTH, gc.WORLD_TILE_HEIGHT,
				gc.RANDOM_SEED ? (int) (Math.random() * 100000) : gc.SEED, gc.BOG_RADIUS)
				.generateWorld();
		addHitboxesToMap();

		Timer tickTimer = new Timer(1000 / gc.TPS, this);
		tickTimer.setActionCommand("tick");
		tickTimer.start();

		Timer secTimer = new Timer(1000, this);
		secTimer.setActionCommand("secUpdate");
		secTimer.start();

		System.out.println("Running server on port " + port);
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				int id = nextID();
				SClient client = new SClient(serverSocket.accept(), this, id, chunker, map, this.gc);
				addClient(client);
				client.send(new StartPacket());

				System.out.printf("Client with id %d connected\n", id);
			}
		} catch (IOException e) {
			handleException("IOException when connecting client", e);
		}
	}

	void addHitboxesToMap() {
		for (Tile[][] slice : map) {
			for (Tile[] column : slice) {
				for (Tile t : column) {
					Class<? extends Rectangle> hitboxClass = t.getHitboxType();
					if (hitboxClass == null)
						continue; // tile has no hitbox

					int w = gc.TILE_WIDTH, h = gc.TILE_HEIGHT;
					int x = t.getX() * w, y = t.getY() * h;
					try {
						hitboxClass
								.getConstructor(double.class, double.class, double.class, double.class, Chunker.class)
								.newInstance(x + w / 2, y + h / 2, w, h, chunker);
					} catch (Exception e) {
						System.out.println("Exception when instantiation hitbox");
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("start")) {
			try {
				seed = Integer.parseInt(seedField.getText());
				worldHeight = Integer.parseInt(hTextField.getText());
				worldWidth = Integer.parseInt(wTextField.getText());
			} catch (Exception error) {
				seed = 2345;
				worldHeight = 35;
				worldWidth = 35;
			}

			new Thread(() -> startServer()).start();
		}
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

	public double getCollisionChecksPerFrame() {
		return collisionChecksPerFrame;
	}

	private ArrayList<Entity> entities = new ArrayList<>();

	private ArrayList<Entity> entitiesToAdd = new ArrayList<>();

	public void addEntity(Entity e) {
		entitiesToAdd.add(e);
	}

	private ArrayList<SClient> clientsToAdd = new ArrayList<>();

	public void addClient(SClient c) {
		clientsToAdd.add(c);
	}

	void tick() {
		long currentTime = System.currentTimeMillis();
		double deltaTime = (double) (currentTime - lastTickTime) / (1 / (double) gc.TPS * 1000);
		lastTickTime = currentTime;

		tick++;

		// add all new entities and clients
		for (int i = 0; i < clientsToAdd.size(); i++) {
			SClient c = clientsToAdd.get(i);
			clients.put(c.getID(), c);
		}
		clientsToAdd.clear();
		for (int i = 0; i < entitiesToAdd.size(); i++) {
			entities.add(entitiesToAdd.get(i));
		}
		entitiesToAdd.clear();

		// update/remove entities and clients
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e.shouldRemove()) {
				entities.remove(i);
				chunker.removeHitbox(e.getHitbox());
				if (e instanceof SClient) {
					SClient c = (SClient) e;
					clients.remove(c.getID());
				}
				i--; // otherwise we would skip an entity when going to the next loop
				continue;
			}

			entities.get(i).update(deltaTime);
		}

		// update all collisions
		collisionChecks += chunker.checkCollisions();

		// send all entities to all clients
		for (SClient c : clients.values()) {
			c.clearEntities();
			for (Entity r : entities) {
				c.addEntity(r.getInfo());
			}
		}

		for (SClient c : clients.values()) {
			c.sendPackets();
		}
	}

	// gets called once a second
	void secUpdate() {
		tps = tick;
		tick = 0;

		collisionChecksPerFrame = collisionChecks / (double) tps;
		collisionChecks = 0;
	}

	private int id = 0;

	private int nextID() {
		return id++;
	}

	public SClient getClient(int id) {
		SClient c = clients.get(id);
		if (c != null)
			return c;

		// the case where the client hasn't been added yet
		for (SClient unaddedClient : clientsToAdd) {
			if (unaddedClient.getID() == id)
				return unaddedClient;
		}

		throw new RuntimeException("Could not find client with id " + id);
	}

	public void sendToClient(int id, PacketTo<Client> p) {
		getClient(id).send(p);
	}

	public void setClientReady(int id, String name) {
		getClient(id).setReady(name);
		getClient(id).send(new GlobalConstantsPacket(
				this.gc));
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
	}
}
