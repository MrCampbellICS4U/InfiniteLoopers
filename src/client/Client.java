package client;

import java.awt.*;
import javax.swing.*;

import game.world.Tiles.Tile;

import java.net.Socket;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.io.*;
import java.net.UnknownHostException;
import javax.imageio.*;
import java.awt.image.*;
import shared.*;
import packets.*;
import server.Server;

public class Client implements LastWish, ActionListener {
	public static void main(String[] args) {
		new Client();
	}

	JFrame window, mainMenu, settingsMenu;
	Canvas canvas;
	DrawingPanel main, settingsPanel;
	BufferedImage menuPNG;
	JButton play, settings;
	int W = 1300;
	int H = 800;

	private ArrayList<Tile> visibleTiles = new ArrayList<>();
	private ArrayList<Tile> nextVisibleTiles = new ArrayList<>();

	Client() {
		window = new JFrame("Sarvivarz");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setFocusTraversalKeysEnabled(false); // allow us to detect tab

		canvas = new Canvas(this);
		canvas.setPreferredSize(new Dimension(W, H));

		window.add(canvas);

		window.pack();
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		// window.setVisible(true);

		settingsMenu = new JFrame("Sarvivarz");
		settingsMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menuPNG = Client.loadImage("./src/images/image.png");
		mainMenu = new JFrame("Sarvivarz");
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

		main.add(play);
		main.add(settings);
		main.add(temporary);
		mainMenu.add(main);
		mainMenu.pack();
		mainMenu.setLocationRelativeTo(null);
		mainMenu.setVisible(true);

	}

	public void handleException(String message, Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stackTrace = sw.toString();

		JOptionPane.showMessageDialog(window, stackTrace, message, JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

	public void handleDisconnection(int id, Exception e) {
		handleException("Could not connect to server", e);
	}

	private PacketLord<Client> pl;

	public void send(PacketTo<Server> p) {
		pl.send(p);
	}

	private void startGame(String ip, int port) {
		try {
			Socket socket = new Socket(ip, port);
			pl = new PacketLord<Client>(socket, this);
		} catch (UnknownHostException e) {
			handleException("Could not connect to server", e);
		} catch (IOException e) {
			handleException("Could not connect to server", e);
		}

		window.addKeyListener(new GameKeyListener(this));
		window.addMouseListener(new GameMouseListener(this));
		window.addMouseMotionListener(new GameMouseListener(this));

		Timer tickTimer = new Timer(1000 / 60, this);
		tickTimer.setActionCommand("tick");
		tickTimer.start();

		Timer secTimer = new Timer(1000, this);
		secTimer.setActionCommand("secUpdate");
		secTimer.start();
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
			// Draw game menu
			g2.drawImage(menuPNG, 0, 0, this.getWidth(), this.getHeight(), null);
			W = this.getWidth();
			H = this.getHeight();
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("tick"))
			tick();
		if (e.getActionCommand().equals("secUpdate"))
			secUpdate();
		if (mainMenu.isVisible()) {
			String action = e.getActionCommand();
			if (action.equals("play")) {
				mainMenu.setVisible(false);
				window.setVisible(true);
				// startGame("76.66.240.46", 2345);
				startGame("127.0.0.1", 2000);
			}
		}
	}

	private PlayerInfo me;

	public PlayerInfo getMe() {
		return me;
	}

	private int fps, frame, ping, tps;

	public int getFPS() {
		return fps;
	}

	public int getPing() {
		return ping;
	}

	public int getTPS() {
		return tps;
	}

	void tick() {
		frame++;
		System.out.println("Num Tiles before: " + getVisibleTiles().size());
		setVisibleTiles((getNextVisibleTiles()));
		System.out.println("Num Tiles after: " + getVisibleTiles().size());
		canvas.repaint();
	}

	// gets called once a second
	void secUpdate() {
		fps = frame;
		frame = 0;
		send(new GetServerInfoPacket());
	}

	// server acknowledged connection, we can start sending packets
	// before this, we don't know our id
	public void start(int id) {
		pl.setID(id);
		send(new ReadyPacket()); // acknowledge that we're ready (see note in server/SClient.java)
		System.out.println("Connected!");
	}

	private ArrayList<PlayerInfo> otherPlayers = new ArrayList<>();

	public ArrayList<PlayerInfo> getOtherPlayers() {
		return otherPlayers;
	}

	public void setServerInfo(int ping, int tps) {
		this.ping = ping;
		this.tps = tps;
	}

	public void setPosition(PlayerInfo me) {
		this.me = me;
	}

	public void setOtherPlayers(ArrayList<PlayerInfo> players) {
		otherPlayers = players;
	}

	void handleMouseMovement(int mouseX, int mouseY) {
		int relMouseX = mouseX - window.getWidth() / 2;
		int relMouseY = mouseY - window.getHeight() / 2;
		double angle = Math.atan2(relMouseY, relMouseX);
		send(new ClientPlayerRotationPacket(angle));
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

	boolean mapOpen = false;

	// todo implement
	public void toggleMap() {
		mapOpen = !mapOpen;
		System.out.printf("The map is now %s\n", mapOpen ? "open" : "closed");
	}

	public ArrayList<Tile> setVisibleTiles(ArrayList<Tile> terrain) {
		return this.visibleTiles = (ArrayList<Tile>) terrain.clone();
	}

	public ArrayList<Tile> setVisibleTiles(Tile[][][] terrain) {
		return this.visibleTiles = (ArrayList<Tile>) ConvertToArrayList.convert(terrain).clone();
	}

	public ArrayList<Tile> setNextVisibleTiles(ArrayList<Tile> terrain) {
		return this.nextVisibleTiles = (ArrayList<Tile>) terrain.clone();
	}

	public ArrayList<Tile> setNextVisibleTiles(Tile[][][] terrain) {
		return this.nextVisibleTiles = (ArrayList<Tile>) ConvertToArrayList.convert(terrain).clone();
	}

	public ArrayList<Tile> getVisibleTiles() {
		return this.visibleTiles;
	}

	public ArrayList<Tile> getNextVisibleTiles() {
		return this.nextVisibleTiles;
	}

	public void updateTile(Tile newTile) {
		System.out.println("Recieved: " + newTile);
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

	public void handlePartialFOVUpdate(ArrayList<Tile> tiles) {
		for (Tile tile : tiles) {
			updateTile(tile);
		}
	}

	public ArrayList<Tile> purgeInvisibleTiles(ArrayList<Tile> tiles) { // TODO: call this
		// removes tiles from the tiles arraylist that are out of the buffer zone

		PlayerInfo me = this.getMe();
		if (me == null || tiles.isEmpty()) // if the server hasn't given client an identity, TODO: Ethan! FIX ME!
			return tiles;

		Set<Tile> tilesToPurge = new HashSet<>();

		for (Tile currentTile : tiles) {
			if (currentTile == null || currentTile.getType().equals("air"))
				continue;

			int groundRelX = currentTile.getX() * GlobalConstants.TILE_WIDTH - me.xGlobal
					+ GlobalConstants.DRAWING_AREA_WIDTH / 2;
			int groundRelY = currentTile.getY() * GlobalConstants.TILE_HEIGHT - me.yGlobal
					+ GlobalConstants.DRAWING_AREA_HEIGHT / 2;

			// if the tile is outside the screen and beyond the tile buffer size remove it
			// from the visible tiles arry
			if (groundRelX < -GlobalConstants.TILE_WIDTH * GlobalConstants.TILE_X_BUFFER
					|| groundRelX > GlobalConstants.DRAWING_AREA_WIDTH
							+ GlobalConstants.TILE_WIDTH * GlobalConstants.TILE_X_BUFFER + GlobalConstants.TILE_WIDTH
					|| groundRelY < -GlobalConstants.TILE_HEIGHT * GlobalConstants.TILE_Y_BUFFER
					|| groundRelY > GlobalConstants.DRAWING_AREA_HEIGHT
							+ GlobalConstants.TILE_HEIGHT * GlobalConstants.TILE_Y_BUFFER
							+ GlobalConstants.TILE_HEIGHT) {
				tilesToPurge.add(currentTile);
			}
		}

		for (Tile byebyeTile : tilesToPurge) {
			tiles.remove(byebyeTile);
		}

		return tiles;
	}
}
