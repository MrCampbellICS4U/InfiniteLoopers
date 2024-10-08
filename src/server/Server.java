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
	JTextField seedField, wTextField, hTextField, healthTextField, bulletSpamText, bulletSpeed, killsText, regenTime,
			structureDensityText, tpsText, serverPortText, bogSizeText, maxArmourText, bulletDespawnTimeText,
			cansuicideText, maxUsernameLengthText, canHoldToShootText;
	JButton startServer;

	public static void main(String[] args) {
		new Server();
	}

	private final int port = 2000;
	private HashMap<Integer, SClient> clients = new HashMap<>(); // map from ids to clients
	private Tile[][][] map;
	public GlobalConstants gc = new GlobalConstants();
	public int seed, worldHeight, worldWidth, health, bulDelay, bulSpeed, regen, killsWin, structureDensity, maxTPS,
			serverPort, bogSize, maxArmour, bulletDespawnTime, maxlen_username;
	public boolean cansuicide, canHoldToShoot;
	private Chunker chunker;
	private long lastTickTime = System.currentTimeMillis();

	/**
	 * Initializes the server user interface and sets up the necessary components.
	 */
	Server() {
		serverUI = new JFrame("Server UI");
		serverUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverDrawing = new JPanel();
		serverDrawing.setPreferredSize(new Dimension(500, 650));
		seedField = new JTextField("Enter Seed Here" + " | Default: " + gc.SEED, 35);
		wTextField = new JTextField("Enter Width Here" + " | Default: " + gc.WORLD_TILE_WIDTH, 35);
		hTextField = new JTextField("Enter Height Here" + " | Default: " + gc.WORLD_TILE_HEIGHT, 35);
		healthTextField = new JTextField("Enter Starting Health" + " | Default: " + gc.MAX_HEALTH, 35);
		bulletSpamText = new JTextField("Bullet Delay Speed (Ms)" + " | Default: " + gc.SHOT_DELAY, 35);
		bulletSpeed = new JTextField("Bullet Speed" + " | Default: " + gc.BULLET_SPEED, 35);
		regenTime = new JTextField("Regeneration Time (Seconds)" + " | Default: " + gc.REGEN_TIME, 35);
		killsText = new JTextField("Kills in a row" + " | Default: " + gc.KILLS_TO_WIN, 35);
		structureDensityText = new JTextField("Structure Density (1-100)" + " | Default: " + gc.STRUCTURE_DENSITY, 35);
		tpsText = new JTextField("Ticks Per Second" + " | Default: " + gc.TPS, 35);
		serverPortText = new JTextField("Server Port" + " | Default: " + gc.SERVER_PORT, 35);
		bogSizeText = new JTextField("Bog Size" + " | Default: " + gc.BOG_SIZE, 35);
		maxArmourText = new JTextField("Max Armour" + " | Default: " + gc.MAX_ARMOR, 35);
		bulletDespawnTimeText = new JTextField("Bullet Despawn Time" + " | Default: " + gc.BULLET_DESPAWN_TIME, 35);
		cansuicideText = new JTextField(
				"Players can commit suicide (t/f | true/false)" + " | Default: " + gc.CAN_SUICIDE, 35);
		maxUsernameLengthText = new JTextField(
				"Username max character length" + " | Default: " + gc.MAX_USERNAME_LENGTH, 35);
		canHoldToShootText = new JTextField(
				"Players can hold to shoot (t/f | true/false)" + " | Default: " + gc.CAN_HOLD_TO_SHOOT, 35);

		startServer = new JButton("Start");
		startServer.setActionCommand("start");
		startServer.addActionListener(this);
		serverDrawing.add(new JLabel("Unedited text fields will be set to their default value"));
		serverDrawing.add(serverPortText);
		serverDrawing.add(seedField);
		serverDrawing.add(tpsText);
		serverDrawing.add(wTextField);
		serverDrawing.add(hTextField);
		serverDrawing.add(healthTextField);
		serverDrawing.add(maxArmourText);
		serverDrawing.add(regenTime);
		serverDrawing.add(killsText);
		serverDrawing.add(cansuicideText);
		serverDrawing.add(bulletSpamText);
		serverDrawing.add(bulletSpeed);
		serverDrawing.add(bulletDespawnTimeText);
		serverDrawing.add(structureDensityText);
		serverDrawing.add(bogSizeText);
		serverDrawing.add(maxUsernameLengthText);
		serverDrawing.add(canHoldToShootText);

		serverDrawing.add(startServer);
		serverUI.add(serverDrawing);
		serverUI.pack();
		serverUI.setLocationRelativeTo(null);
		serverUI.setResizable(false);

		serverUI.setVisible(true);

	}

	/**
	 * Starts the server with the specified configurations and waits for client
	 * connections.
	 * 
	 * The server initializes the game settings based on the provided parameters,
	 * generates the game world,
	 * sets up timers for game ticks and second updates, and listens for client
	 * connections on the specified port.
	 * 
	 * @throws IOException if an I/O error occurs when connecting a client
	 */
	public void startServer() {
		gc.SEED = seed;
		gc.WORLD_TILE_HEIGHT = worldHeight;
		gc.WORLD_TILE_WIDTH = worldWidth;
		gc.REGEN_TIME = regen * 1000;
		gc.MAX_HEALTH = health;
		gc.SHOT_DELAY = bulDelay;
		gc.BULLET_SPEED = bulSpeed;
		gc.KILLS_TO_WIN = killsWin;
		gc.STRUCTURE_DENSITY = structureDensity;
		gc.TPS = maxTPS;
		gc.SERVER_PORT = serverPort;
		gc.BOG_SIZE = bogSize;
		gc.MAX_ARMOR = maxArmour;
		gc.BULLET_DESPAWN_TIME = bulletDespawnTime;
		gc.CAN_SUICIDE = cansuicide;
		gc.MAX_USERNAME_LENGTH = maxlen_username;
		gc.CAN_HOLD_TO_SHOOT = canHoldToShoot;

		gc.WORLD_HEIGHT = worldHeight * gc.TILE_HEIGHT;
		gc.WORLD_WIDTH = worldWidth * gc.TILE_WIDTH;
		gc.BOG_RADIUS = (worldWidth + worldHeight) / 2 / gc.BOG_SIZE;

		this.chunker = new Chunker(gc.CHUNK_WIDTH, gc.CHUNK_HEIGHT,
				gc.WORLD_WIDTH, gc.WORLD_HEIGHT);

		map = new WorldGenerator(gc.WORLD_TILE_WIDTH, gc.WORLD_TILE_HEIGHT,
				gc.RANDOM_SEED ? (int) (Math.random() * 100000) : gc.SEED, gc.BOG_RADIUS, gc.STRUCTURE_DENSITY)
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

	/**
	 * Adds hitboxes to the map based on the tiles in the map.
	 * Each tile in the map is checked for a hitbox type. If a hitbox type is found,
	 * a hitbox object is instantiated and added to the map.
	 * The hitbox object is created using the hitbox class's constructor, passing in
	 * the appropriate parameters.
	 * If an exception occurs during the instantiation of the hitbox object, an
	 * error message is printed.
	 */
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

	/**
	 * Performs an action based on the event triggered.
	 *
	 * @param e The ActionEvent object representing the event triggered.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("start")) {
			serverUI.setVisible(false);
			try {
				seed = Integer.parseInt(seedField.getText());
			} catch (Exception a) {
				seed = gc.SEED;
			}
			try {
				worldHeight = Integer.parseInt(hTextField.getText());
			} catch (Exception a) {
				worldHeight = gc.WORLD_TILE_HEIGHT;
			}
			try {
				worldWidth = Integer.parseInt(wTextField.getText());
			} catch (Exception a) {
				worldWidth = gc.WORLD_TILE_WIDTH;
			}
			try {
				health = Integer.parseInt(healthTextField.getText());
			} catch (Exception a) {
				health = gc.MAX_HEALTH;
			}
			try {
				bulDelay = Integer.parseInt(bulletSpamText.getText());
			} catch (Exception a) {
				bulDelay = gc.SHOT_DELAY;
			}
			try {
				bulSpeed = Integer.parseInt(bulletSpeed.getText());
			} catch (Exception a) {
				bulSpeed = gc.BULLET_SPEED;
			}
			try {
				regen = Integer.parseInt(regenTime.getText());
			} catch (Exception a) {
				regen = gc.REGEN_TIME / 1000;
			}
			try {
				killsWin = Integer.parseInt(killsText.getText());
			} catch (Exception a) {
				killsWin = gc.KILLS_TO_WIN;
			}
			try {
				structureDensity = Integer.parseInt(structureDensityText.getText());
			} catch (Exception a) {
				structureDensity = gc.STRUCTURE_DENSITY;
			}
			try {
				maxTPS = Integer.parseInt(tpsText.getText());
			} catch (Exception a) {
				maxTPS = gc.TPS;
			}
			try {
				serverPort = Integer.parseInt(serverPortText.getText());
			} catch (Exception a) {
				serverPort = gc.SERVER_PORT;
			}
			try {
				bogSize = Integer.parseInt(bogSizeText.getText());
			} catch (Exception a) {
				bogSize = gc.BOG_SIZE;
			}
			try {
				maxArmour = Integer.parseInt(maxArmourText.getText());
			} catch (Exception a) {
				maxArmour = gc.MAX_ARMOR;
			}
			try {
				maxlen_username = Integer.parseInt(maxUsernameLengthText.getText());
			} catch (Exception a) {
				maxlen_username = gc.MAX_USERNAME_LENGTH;
			}
			try {
				bulletDespawnTime = Integer.parseInt(bulletDespawnTimeText.getText());
			} catch (Exception a) {
				bulletDespawnTime = gc.BULLET_DESPAWN_TIME;
			}
			try {
				String text = cansuicideText.getText();
				if (text.toLowerCase().equals("t") || text.toLowerCase().equals("true"))
					cansuicide = true;
				else if (text.toLowerCase().equals("f") || text.toLowerCase().equals("false"))
					cansuicide = false;
				else
					cansuicide = gc.CAN_SUICIDE;
			} catch (Exception a) {
				cansuicide = gc.CAN_SUICIDE;
			}
			try {

				String text = canHoldToShootText.getText();
				if (text.toLowerCase().equals("t") || text.toLowerCase().equals("true"))
					canHoldToShoot = true;
				else if (text.toLowerCase().equals("f") || text.toLowerCase().equals("false"))
					canHoldToShoot = false;
				else
					canHoldToShoot = gc.CAN_HOLD_TO_SHOOT;
			} catch (Exception a) {
				canHoldToShoot = gc.CAN_HOLD_TO_SHOOT;
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

	/**
	 * Performs a tick of the game loop, updating entities and sending packets to
	 * clients.
	 * This method is called at a regular interval to keep the game state
	 * synchronized.
	 */
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

	public boolean existsClient(int id) {
		return clients.containsKey(id);
	}

	/**
	 * Retrieves a client by their ID.
	 *
	 * @param id The ID of the client to retrieve.
	 * @return The client with the specified ID.
	 * @throws RuntimeException if no client with the specified ID is found.
	 */
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

	/**
	 * Sends a packet to the client with the specified ID.
	 *
	 * @param id The ID of the client to send the packet to.
	 * @param p  The packet to send.
	 */
	public void sendToClient(int id, PacketTo<Client> p) {
		getClient(id).send(p);
	}

	/**
	 * Sets the client with the specified ID as ready and sends a
	 * GlobalConstantsPacket to the client.
	 *
	 * @param id   The ID of the client
	 * @param name The name of the client
	 */
	public void setClientReady(int id, String name) {
		getClient(id).setReady(name);
		getClient(id).send(new GlobalConstantsPacket(
				this.gc));
	}

	/**
	 * Handles the input for a specific client identified by their ID.
	 *
	 * @param id The ID of the client
	 * @param i  The input to handle
	 * @param is The state of the input
	 */
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

	/**
	 * Handles the disconnection of a client with the given id.
	 *
	 * @param id The id of the disconnected client
	 * @param e  The exception that caused the disconnection, if any
	 */
	public void handleDisconnection(int id, Exception e) {
		System.out.printf("Client with id %d disconnected\n", id);
		SClient c = getClient(id);
		c.remove();
	}
}
