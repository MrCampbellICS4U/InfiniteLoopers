package client;

import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;

import java.net.Socket;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.UnknownHostException;
import javax.imageio.*;
import java.awt.image.*;

import shared.*;
import packets.*;
import server.Server;
import world.Tiles.Tile;
import entities.*;

public class Client implements LastWish, ActionListener {
	/**
	 * The entry point of the application.
	 *
	 * @param args The command line arguments
	 */
	public static void main(String[] args) {
		new Client();
	}

	JFrame window, mainMenu, settingsMenu;

	GlobalConstants gc = new GlobalConstants();

	DrawingPanel main;
	DrawingPanel2 settingsPanel;
	public static MapDrawing map;
	BufferedImage menuPNG, settingsPNG, akImage, bImage;
	JButton play, settings, back, resetButton, showControls;
	RoundJTextField ipAddress, portNum, enterName;
	String playerName = "I Forgor";
	String defaultName = "Enter Name Here";
	int W = gc.DRAWING_AREA_WIDTH;
	int H = gc.DRAWING_AREA_HEIGHT;
	String ip = gc.SERVER_IP;
	int port = gc.SERVER_PORT;

	private ArrayList<Tile> visibleTiles = new ArrayList<>();
	private ArrayList<Tile> nextVisibleTiles = new ArrayList<>();

	Canvas canvas;

	public Canvas getCanvas() {
		return canvas;
	}

	/**
	 * Initializes the client and sets up the game window, canvas, map, and menu
	 * images.
	 * Also sets up the key and mouse listeners, window properties, and loads
	 * necessary images.
	 */
	Client() {
		window = new JFrame("Sarvivarz");
		window.addKeyListener(new GameKeyListener(this));
		window.addMouseListener(new GameMouseListener(this));
		window.addMouseMotionListener(new GameMouseListener(this));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setFocusTraversalKeysEnabled(false); // allow us to detect tab
		canvas = new Canvas(this);
		map = new MapDrawing(this, me);
		canvas.setPreferredSize(new Dimension(W, H));
		window.add(canvas);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setResizable(false);

		menuPNG = Canvas.loadImage("res/Menus/Main/image.png");
		settingsPNG = Canvas.loadImage("res/Menus/Settings/settingsImage.png");
		akImage = Canvas.loadImage("res/game/Guns/ak.png");
		bImage = Canvas.loadImage("res/game/Guns/bullets.png");

		setupSettingsMenu();
		setupMainMenu();
	}

	/**
	 * Retrieves the image of the bullet.
	 *
	 * @return The BufferedImage object representing the bullet image.
	 */
	public BufferedImage getBulletImage() {
		return bImage;
	}

	/**
	 * Handles an exception by printing the stack trace and exiting the program.
	 *
	 * @param message A message describing the exception.
	 * @param e       The exception that occurred.
	 */
	public void handleException(String message, Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stackTrace = sw.toString();

		System.exit(1);
	}

	/**
	 * Handles the disconnection of a player.
	 *
	 * @param id The ID of the player
	 * @param e  The exception that caused the disconnection
	 */
	public void handleDisconnection(int id, Exception e) {
		me.health = 0;
		// handleException("Could not connect to server", e);
	}

	private PacketLord<Client> pl;

	boolean ready = false; // gets set to true when we get our id

	/**
	 * Sends a packet to the server if the client is ready and the player is alive.
	 *
	 * @param p The packet to send
	 */
	public void send(PacketTo<Server> p) {
		if (!ready)
			return; // not ready to send yet
		if (me != null && (me.health == 0))
			return; // don't send packets when you're dead (and the socket is closed)

		pl.send(p);
	}

	Timer tickTimer, secTimer;

	/**
	 * Starts a game by establishing a connection to the server using the provided
	 * IP address and port.
	 * Initializes the necessary timers for game updates.
	 *
	 * @param ip   The IP address of the server to connect to.
	 * @param port The port number of the server to connect to.
	 */
	private void startGame(String ip, int port) {

		try {
			Socket socket = new Socket(ip, port);
			pl = new PacketLord<Client>(socket, this);
		} catch (UnknownHostException e) {
			handleException("Could not connect to server", e);
		} catch (java.io.EOFException e) {
			System.out.println("GG");
		} catch (IOException e) {
			handleException("Could not connect to server", e);
		}

		tickTimer = new Timer(1000 / gc.FPS, this);
		tickTimer.setActionCommand("tick");
		tickTimer.start();

		secTimer = new Timer(1000, this);
		secTimer.setActionCommand("secUpdate");
		secTimer.start();
	}

	/**
	 * Performs various actions based on the event triggered.
	 *
	 * @param e The ActionEvent object representing the event triggered.
	 */
	public void actionPerformed(ActionEvent e) {
		playerName = enterName.getText();
		if (playerName.equals("Enter Name Here") || playerName.equals("")) {
			playerName = "Dunce";
		} else {
			playerName = enterName.getText();
			defaultName = playerName;
		}

		if (e.getActionCommand().equals("tick"))
			tick();
		if (e.getActionCommand().equals("secUpdate"))
			secUpdate();
		if (e.getActionCommand().equals("respawn")) {
			window.setVisible(false);
			me.health = 3;
			mainMenu.setVisible(true);
		}
		if (settingsMenu.isVisible()) {
			String actionCom = e.getActionCommand();
			String ipInput = ipAddress.getText();
			if (actionCom.equals("controls")) {
				JOptionPane.showInternalMessageDialog(null,
						"CONTROLS:\n      WASD to Move\n      Space to shoot\n      Tab or M to Toggle Map\n      Press F to Toggle Names\n      Press P for fun!\n\n\n            If you get 5 kills in a row you win the game. Enjoy Our Game!",
						"About The Game", JOptionPane.INFORMATION_MESSAGE);
			}

			try {
				if (ipInput.equals("")) {
					ip = "127.0.0.1";
				} else {
					ip = ipInput;
				}
				if ((portNum.getText()).equals("")) {
					port = 2000;
				} else {
					port = Integer.parseInt(portNum.getText());
				}

				if (actionCom.equals("leave")) {
					mainMenu.setVisible(true);
					mainMenu.setLocationRelativeTo(null);
					settingsMenu.setVisible(false);
				}
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(null,
						"Please enter a valid ip and port number. \nIP Format: 00.000.00.0\nPort Format: 0000",
						"Error, Get Smarter",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		if (mainMenu.isVisible()) {
			String action = e.getActionCommand();
			if (action.equals("play")) {
				mainMenu.setVisible(false);
				window.setVisible(true);
				window.requestFocus();
				startGame(ip, port);
			} else if (action.equals("settings")) {
				mainMenu.setVisible(false);
				settingsMenu.setVisible(true);
				settingsMenu.setLocationRelativeTo(null);
			}
		}
	}

	private PlayerInfo me;

	/**
	 * Retrieves the information of the current player.
	 *
	 * @return The PlayerInfo object representing the current player.
	 */
	public PlayerInfo getMe() {
		return me;
	}

	private int fps, frame, ping, tps;
	private double collisionChecksPerFrame;

	/**
	 * Returns the frames per second (FPS) value.
	 *
	 * @return The frames per second value.
	 */
	public int getFPS() {
		return fps;
	}

	/**
	 * Returns the current ping value.
	 *
	 * @return The ping value.
	 */
	public int getPing() {
		return ping;
	}

	/**
	 * Returns the current TPS (Ticks Per Second) value.
	 *
	 * @return The TPS value.
	 */
	public int getTPS() {
		return tps;
	}

	/**
	 * Returns the number of collision checks performed per frame.
	 *
	 * @return The number of collision checks per frame.
	 */
	public double getCollisionChecksPerFrame() {
		return collisionChecksPerFrame;
	}

	/**
	 * Updates the game state and performs necessary actions for each tick.
	 * This method increments the frame count, updates the visible tiles, and
	 * repaints the canvas and map.
	 * If the player's health is 0, it stops the tick timer and the second timer,
	 * sets the game state to not ready,
	 * and adds a reset button for respawning. If the player's kills reach the
	 * winning threshold, it stops the tick timer
	 * and the second timer, and sets the game state to not ready.
	 */
	void tick() {
		frame++;
		setVisibleTiles(getNextVisibleTiles());
		canvas.repaint();
		map.repaint();

		PlayerInfo me = this.getMe();
		if (me == null)
			return;
		if (me.health == 0) {
			// you died L
			ready = false; // don't send any packets
			tickTimer.stop();
			secTimer.stop();
			resetButton = new JButton();
			resetButton.setActionCommand("respawn");
			resetButton.addActionListener(this);
			resetButton.setOpaque(false);
			resetButton.setContentAreaFilled(false);
			resetButton.setBorderPainted(false);
			resetButton.setBounds(gc.DRAWING_AREA_WIDTH / 2 - 200, gc.DRAWING_AREA_HEIGHT - 100, 400, 50);
			canvas.add(resetButton);
			return;
		}
		if (me.kills >= gc.KILLS_TO_WIN) {
			ready = false; // don't send any packets
			tickTimer.stop();
			secTimer.stop();
			return;
		}

		frame++;
		setVisibleTiles(getNextVisibleTiles());
		canvas.repaint();
		map.repaint();
	}

	// colours for the map
	private final Color UNEXPLORED_COLOUR = Color.GRAY.darker().darker().darker();
	private final Color CRATE_COLOUR = new Color(184, 110, 26);
	private final Color ROOF_COLOUR = Color.RED.darker();
	private final Color SAND_COLOUR = new Color(255, 252, 158);
	private final Color WATER_COLOUR = new Color(43, 149, 255);
	private final Color BUSH_COLOUR = Color.GREEN.darker();
	private int[][] exploredMap;

	private BufferedImage mapImage;

	/**
	 * Retrieves the image of the map.
	 * 
	 * @return The BufferedImage representing the map image.
	 */
	public BufferedImage getMapImage() {
		return mapImage;
	}

	private Graphics mapGraphics;

	/**
	 * Sets the color of a tile on the map based on its type.
	 * 
	 * @param t The tile to set the color for.
	 */
	public void setMapColour(Tile t) {
		int x = t.getX();
		int y = t.getY();
		if (t.getZ() < exploredMap[x][y])
			return;

		exploredMap[x][y] = t.getZ();

		Color c = switch (t.getType()) {
			case "roof" -> ROOF_COLOUR;
			case "water" -> WATER_COLOUR;
			case "crate" -> CRATE_COLOUR;
			case "grass" -> Color.GREEN;
			case "sand" -> SAND_COLOUR;
			case "bush" -> BUSH_COLOUR;
			default -> UNEXPLORED_COLOUR;
		};

		mapGraphics.setColor(c);
		mapGraphics.fillRect(x, y, 1, 1);
	}

	/**
	 * Sets the global constants and initializes the necessary variables and
	 * objects.
	 *
	 * @param gc The GlobalConstants object containing the global constants
	 */
	public void setGlobalConstants(GlobalConstants gc) {
		this.gc = gc;
		this.W = gc.DRAWING_AREA_WIDTH;
		this.H = gc.DRAWING_AREA_HEIGHT;
		this.canvas.gc = gc;
		this.map.gc = gc;
		exploredMap = new int[gc.WORLD_TILE_WIDTH][gc.WORLD_TILE_HEIGHT];
		mapImage = new BufferedImage(gc.WORLD_TILE_WIDTH, gc.WORLD_TILE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		mapGraphics = mapImage.getGraphics();
	}

	// gets called once a second
	void secUpdate() {
		fps = frame;
		frame = 0;
		send(new GetServerInfoPacket());
	}

	private int id;

	/**
	 * Returns the ID of the object.
	 *
	 * @return The ID of the object.
	 */
	public int getID() {
		return id;
	}

	public boolean drawName = true;

	/**
	 * Toggles the state of the drawName variable.
	 *
	 * @return The new value of the drawName variable after toggling.
	 */
	public boolean toggleName() {
		return drawName = !drawName;
	}

	public boolean showStats = false;

	/**
	 * Toggles the display of statistics.
	 * If the statistics are currently shown, they will be hidden.
	 * If the statistics are currently hidden, they will be shown.
	 */
	public void toggleStats() {
		showStats = !showStats;
	}

	// server acknowledged connection, we can start sending packets
	// before this, we don't know our id
	public void start(int id) {
		pl.setID(id);
		this.id = id;
		ready = true;
		send(new ReadyPacket(playerName)); // acknowledge that we're ready (see note in server/SClient.java)
		System.out.println("Connected!");
	}

	Font comic = new Font("Comic Sans MS", Font.PLAIN, 17);

	public Font getFont() {
		return comic;
	}

	private ArrayList<EntityInfo> entities = new ArrayList<>();

	public ArrayList<EntityInfo> getEntities() {
		return entities;
	}

	/**
	 * Sets the server information including ping, TPS (Ticks Per Second), and
	 * collision checks per frame.
	 *
	 * @param ping                    The ping value of the server
	 * @param tps                     The TPS (Ticks Per Second) value of the server
	 * @param collisionChecksPerFrame The number of collision checks per frame
	 */
	public void setServerInfo(int ping, int tps, double collisionChecksPerFrame) {
		this.ping = ping;
		this.tps = tps;
		this.collisionChecksPerFrame = collisionChecksPerFrame;
	}

	/**
	 * Sets the player information for the current instance.
	 *
	 * @param me The player information to set
	 */
	public void setMe(PlayerInfo me) {
		this.me = me;
	}

	/**
	 * Sets the list of entities for this object and updates the "me" player if
	 * found.
	 *
	 * @param entities The list of entities to set
	 */
	public void setEntities(ArrayList<EntityInfo> entities) {
		this.entities = entities;
		for (EntityInfo e : entities) {
			if (e instanceof PlayerInfo) {
				PlayerInfo p = (PlayerInfo) e;
				if (p.id == id)
					setMe((PlayerInfo) p);
			}
		}
	}

	/**
	 * Handles the mouse movement event by calculating the relative mouse position,
	 * determining the angle of rotation, and sending a client player rotation
	 * packet.
	 *
	 * @param mouseX The x-coordinate of the mouse position
	 * @param mouseY The y-coordinate of the mouse position
	 */
	void handleMouseMovement(int mouseX, int mouseY) {
		// don't let the mouse go outside the window
		mouseX = Math.max(0, Math.min(mouseX, window.getWidth()));
		mouseY = Math.max(0, Math.min(mouseY, window.getHeight()));
		int relMouseX = mouseX - window.getWidth() / 2;
		int relMouseY = mouseY - window.getHeight() / 2;
		double angle = Math.atan2(relMouseY, relMouseX);
		send(new ClientPlayerRotationPacket(angle));
	}

	public static boolean mapOpen = false;

	/**
	 * Toggles the visibility of the map.
	 * If the map is currently open, it will be closed. If it is closed, it will be
	 * opened.
	 * The visibility of the map is controlled by the "mapOpen" boolean variable.
	 * When the map is open, it will be set to visible. When the map is closed, it
	 * will be set to invisible.
	 */
	public void toggleMap() {
		mapOpen = !mapOpen;
		map.setVisible(mapOpen);
	}

	/**
	 * Sets the visible tiles to the given terrain.
	 *
	 * @param terrain The list of tiles representing the terrain
	 * @return The updated list of visible tiles
	 */
	public ArrayList<Tile> setVisibleTiles(ArrayList<Tile> terrain) {
		return this.visibleTiles = (ArrayList<Tile>) terrain.clone();
	}

	/**
	 * Sets the visible tiles based on the given terrain.
	 *
	 * @param terrain The 3D array of tiles representing the terrain.
	 * @return The list of visible tiles.
	 */
	public ArrayList<Tile> setVisibleTiles(Tile[][][] terrain) {
		return this.visibleTiles = (ArrayList<Tile>) ConvertToArrayList.convert(terrain).clone();
	}

	/**
	 * Sets the next visible tiles based on the given terrain.
	 *
	 * @param terrain The list of tiles representing the terrain.
	 * @return The updated list of next visible tiles.
	 */
	public ArrayList<Tile> setNextVisibleTiles(ArrayList<Tile> terrain) {
		return this.nextVisibleTiles = (ArrayList<Tile>) terrain.clone();
	}

	/**
	 * Sets the next visible tiles based on the given terrain.
	 *
	 * @param terrain The 3D array representing the terrain.
	 * @return The list of next visible tiles.
	 */
	public ArrayList<Tile> setNextVisibleTiles(Tile[][][] terrain) {
		return this.nextVisibleTiles = (ArrayList<Tile>) ConvertToArrayList.convert(terrain).clone();
	}

	/**
	 * Returns the list of visible tiles.
	 *
	 * @return The list of visible tiles.
	 */
	public ArrayList<Tile> getVisibleTiles() {
		return this.visibleTiles;
	}

	/**
	 * Returns the list of next visible tiles.
	 *
	 * @return The list of next visible tiles.
	 */
	public ArrayList<Tile> getNextVisibleTiles() {
		return this.nextVisibleTiles;
	}

	/**
	 * Updates the tile with the given new tile information.
	 * If the tile already exists in the list of next visible tiles, it will be
	 * replaced with the new tile.
	 * If the tile does not exist in the list, it will be added to the list.
	 *
	 * @param newTile The new tile information to update.
	 */
	public void updateTile(Tile newTile) {
		// find the coords of the tile in the visibleTiles arraylist and replace it
		// if the tile is not in the arraylist, add it
		for (int i = 0; i < nextVisibleTiles.size(); i++) {
			Tile currentTile = nextVisibleTiles.get(i);
			if (currentTile.getX() == newTile.getX() && currentTile.getY() == newTile.getY()
					&& currentTile.getZ() == newTile.getZ()) {
				nextVisibleTiles.set(i, newTile);
				return;
			}
		}
		nextVisibleTiles.add(newTile);
	}

	/**
	 * Handles a partial field of view (FOV) update by updating the given list of
	 * tiles.
	 *
	 * @param tiles The list of tiles to update
	 */
	public void handlePartialFOVUpdate(ArrayList<Tile> tiles) {
		for (Tile tile : tiles) {
			updateTile(tile);
		}
	}

	public BufferedImage getImage() {
		return akImage;
	}

	/**
	 * Sets up the main menu GUI for the Sarvivarz game.
	 * The main menu consists of a JFrame window with buttons for play and settings,
	 * as well as a text field for entering a name.
	 */
	void setupMainMenu() {
		mainMenu = new JFrame("Sarvivarz");
		mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainMenu.setResizable(false);
		main = new DrawingPanel();
		main.setPreferredSize(new Dimension(W, H));
		main.setLayout(null);

		play = new JButton();
		settings = new JButton();
		JButton temporary = new JButton();
		play.setActionCommand("play");
		play.addActionListener(this);

		settings.setActionCommand("settings");
		settings.addActionListener(this);
		double buttonWidth = W * 0.2;
		double buttonHeight = H * 0.15;
		double playLocationWidth = W * 0.58;
		double settingsLocationWidth = W * 0.2;
		double settingsWidth = W * 0.12;
		double buttonLocationHeight = H * 0.8;
		play.setOpaque(false);
		play.setContentAreaFilled(false);
		play.setBorderPainted(false);
		play.setBounds((int) playLocationWidth, (int) buttonLocationHeight, (int) buttonWidth, (int) buttonHeight);

		settings.setOpaque(false);
		settings.setBorderPainted(false);
		settings.setContentAreaFilled(false);

		settings.setBounds((int) settingsLocationWidth, (int) buttonLocationHeight, (int) settingsWidth,
				(int) buttonHeight);

		enterName = new RoundJTextField(15, defaultName);
		enterName.setBounds(425, 670, 300, 60);
		Font field = new Font("Arial", Font.BOLD, 30);
		enterName.setFont(field);
		Color fieldColor = new Color(20, 171, 236);
		enterName.setBackground(fieldColor);

		main.add(enterName);
		main.add(play);
		main.add(settings);
		main.add(temporary);
		mainMenu.add(main);
		mainMenu.pack();
		mainMenu.setLocationRelativeTo(null);
		mainMenu.setResizable(false);
		mainMenu.setVisible(true);
	}

	/**
	 * Sets up the settings menu GUI.
	 */
	void setupSettingsMenu() {
		settingsMenu = new JFrame("Sarvivarz");
		settingsMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		settingsPanel = new DrawingPanel2();
		settingsPanel.setPreferredSize(new Dimension(W, H));
		settingsPanel.setLayout(null);

		Color textFieldColor = new Color(208, 171, 182);
		Font font1 = new Font("SansSerif", Font.BOLD, 40);

		ipAddress = new RoundJTextField(15);
		ipAddress.setBounds(130, 600, 350, 60);
		ipAddress.setFont(font1);
		ipAddress.setBackground(textFieldColor);

		portNum = new RoundJTextField(15);
		portNum.setBounds(905, 600, 125, 60);
		portNum.setFont(font1);
		portNum.setBackground(textFieldColor);

		back = new JButton();
		back.setActionCommand("leave");
		back.addActionListener(this);
		back.setOpaque(false);
		back.setContentAreaFilled(false);
		back.setBorderPainted(false);
		back.setBounds(530, 675, 225, 100);

		showControls = new JButton();
		showControls.setActionCommand("controls");
		showControls.addActionListener(this);
		showControls.setOpaque(false);
		showControls.setContentAreaFilled(false);
		showControls.setBorderPainted(false);
		showControls.setBounds(0, 0, W, 400);

		settingsPanel.add(showControls);
		showControls = new JButton();
		showControls.setActionCommand("controls");
		showControls.addActionListener(this);
		showControls.setOpaque(false);
		showControls.setContentAreaFilled(false);
		showControls.setBorderPainted(false);
		showControls.setBounds(0, 0, W, 400);

		settingsPanel.add(showControls);
		settingsPanel.add(back);
		settingsPanel.add(ipAddress);
		settingsPanel.add(portNum);
		settingsMenu.add(settingsPanel);
		settingsMenu.pack();
		settingsMenu.setResizable(false);
		settingsMenu.setLocationRelativeTo(null);
	}

	private class DrawingPanel2 extends JPanel {

		DrawingPanel2() {
			this.setPreferredSize(new Dimension(W, H));
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			// turn on antialiasing
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// Draw menu
			g2.drawImage(settingsPNG, 0, 0, this.getWidth(), this.getHeight(), null);
			W = this.getWidth();
			H = this.getHeight();
		}
	}

	// Drawing panel for the home page
	private class DrawingPanel extends JPanel {

		DrawingPanel() {
			this.setPreferredSize(new Dimension(W, H));
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			// turn on antialiasing
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// Draw menu
			g2.drawImage(menuPNG, 0, 0, this.getWidth(), this.getHeight(), null);
			W = this.getWidth();
			H = this.getHeight();
		}
	}

	static BufferedImage loadImage(String filename) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println(e.toString());
			JOptionPane.showMessageDialog(null, "An image failed to load: " + filename, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return img;
	}

}
