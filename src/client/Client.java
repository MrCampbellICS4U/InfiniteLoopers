package client;

import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;

import java.net.Socket;
import java.awt.event.*;
import java.util.*;
import java.util.HashSet;
import java.util.Set;
import java.io.*;
import java.net.UnknownHostException;
import javax.imageio.*;
import java.awt.image.*;

import shared.*;
import packets.*;
import server.Server;
import game.world.Tiles.Tile;
import entities.*;

public class Client implements LastWish, ActionListener {
	public static void main(String[] args) {
		new Client();
	}

	JFrame window, mainMenu, settingsMenu;

	DrawingPanel main;
	DrawingPanel2 settingsPanel;
	static MapDrawing map;
	BufferedImage menuPNG, settingsPNG, akImage;
	JButton play, settings, back;
	RoundJTextField ipAddress, portNum;
	static int W = GlobalConstants.DRAWING_AREA_WIDTH;
	static int H = GlobalConstants.DRAWING_AREA_HEIGHT;
	static String ip = GlobalConstants.SERVER_IP;
	static int port = GlobalConstants.SERVER_PORT;

	private ArrayList<Tile> visibleTiles = new ArrayList<>();
	private ArrayList<Tile> nextVisibleTiles = new ArrayList<>();

	Canvas canvas;

	public Canvas getCanvas() {
		return canvas;
	}

	Client() {
		window = new JFrame("Sarvivarz");
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
		setupSettingsMenu();
		setupMainMenu();
	}

	public void handleException(String message, Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stackTrace = sw.toString();

		JOptionPane.showMessageDialog(window, stackTrace, message, JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

	public void handleDisconnection(int id, Exception e) {
		me.health = 0;
		// handleException("Could not connect to server", e);
	}

	private PacketLord<Client> pl;

	boolean ready = false; // gets set to true when we get our id

	public void send(PacketTo<Server> p) {
		if (!ready)
			return; // not ready to send yet
		if (me != null && me.health == 0)
			return; // don't send packets when you're dead (and the socket is closed)

		pl.send(p);
	}

	Timer tickTimer;

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

		window.addKeyListener(new GameKeyListener(this));
		window.addMouseListener(new GameMouseListener(this));
		window.addMouseMotionListener(new GameMouseListener(this));

		tickTimer = new Timer(1000 / GlobalConstants.FPS, this);
		tickTimer.setActionCommand("tick");
		tickTimer.start();

		Timer secTimer = new Timer(1000, this);
		secTimer.setActionCommand("secUpdate");
		secTimer.start();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("tick"))
			tick();
		if (e.getActionCommand().equals("secUpdate"))
			secUpdate();
		if (settingsMenu.isVisible()) {
			String actionCom = e.getActionCommand();
			String ipInput = ipAddress.getText();
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
					System.out.println(ip);
					System.out.println(port);
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
				startGame(ip, port);
			} else if (action.equals("settings")) {
				mainMenu.setVisible(false);
				settingsMenu.setVisible(true);
				settingsMenu.setLocationRelativeTo(null);
			}
		}
	}

	private PlayerInfo me;

	public PlayerInfo getMe() {
		return me;
	}

	private int fps, frame, ping, tps;
	private double collisionChecksPerFrame;

	public int getFPS() {
		return fps;
	}

	public int getPing() {
		return ping;
	}

	public int getTPS() {
		return tps;
	}

	public double getCollisionChecksPerFrame() {
		return collisionChecksPerFrame;
	}

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
			tickTimer.stop();
			return;
		}

		frame++;
		setVisibleTiles(getNextVisibleTiles());
		canvas.repaint();
		map.repaint();
	}

	// gets called once a second
	void secUpdate() {
		fps = frame;
		frame = 0;
		send(new GetServerInfoPacket());
	}

	private int id;

	public int getID() {
		return id;
	}

	// server acknowledged connection, we can start sending packets
	// before this, we don't know our id
	public void start(int id) {
		pl.setID(id);
		this.id = id;
		ready = true;
		send(new ReadyPacket()); // acknowledge that we're ready (see note in server/SClient.java)
		System.out.println("Connected!");
	}

	private ArrayList<EntityInfo> entities = new ArrayList<>();

	public ArrayList<EntityInfo> getEntities() {
		return entities;
	}

	public void setServerInfo(int ping, int tps, double collisionChecksPerFrame) {
		this.ping = ping;
		this.tps = tps;
		this.collisionChecksPerFrame = collisionChecksPerFrame;
	}

	public void setMe(PlayerInfo me) {
		this.me = me;
	}

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

	void handleMouseMovement(int mouseX, int mouseY) {
		int relMouseX = mouseX - window.getWidth() / 2;
		int relMouseY = mouseY - window.getHeight() / 2;
		double angle = Math.atan2(relMouseY, relMouseX);
		send(new ClientPlayerRotationPacket(angle));
	}

	public static boolean mapOpen = false;

	public static void toggleMap() {
		mapOpen = !mapOpen;
		map.setVisible(mapOpen);
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
	public BufferedImage getImage(){
		return akImage;
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
			// from the visible tiles array
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

		main.add(play);
		main.add(settings);
		main.add(temporary);
		mainMenu.add(main);
		mainMenu.pack();
		mainMenu.setLocationRelativeTo(null);
		mainMenu.setResizable(false);
		mainMenu.setVisible(true);
	}

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
			// Draw game menu
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
			// Draw game menu
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
