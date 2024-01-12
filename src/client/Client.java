package client;

import java.awt.*;
import javax.swing.*;

import java.net.Socket;
import java.awt.event.*;
import java.util.ArrayList;
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

	static JFrame window, mainMenu, settingsMenu;
	static Canvas canvas;
	static DrawingPanel main;
	static DrawingPanel2 settingsPanel;
	BufferedImage menuPNG, settingsPNG;
	JButton play, settings, back;
	RoundJTextField ipAddress, portNum;
	int W = 1300;
	int H = 800;
	static String ip = "127.0.0.1";
	static Integer port = 2000;
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
		//window.setVisible(true);
		menuPNG = Client.loadImage("./src/images/image.png");
		settingsPNG = Client.loadImage("./src/images/settingsImage.png");

		//SETTINGS MENU START
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

		//SETTINGS MENU END

		//MAIN MENU CODE START
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
		double buttonWidth = W*0.2;
		double buttonHeight = H*0.15;
		double playLocationWidth = W*0.58;
		double settingsLocationWidth = W*0.2;
		double settingsWidth = W*0.12;
		double buttonLocationHeight = H*0.8;
		play.setOpaque(false);
		play.setContentAreaFilled(false);
		play.setBorderPainted(false);
		play.setBounds((int)playLocationWidth, (int)buttonLocationHeight, (int)buttonWidth, (int)buttonHeight);

		settings.setOpaque(false);
		settings.setBorderPainted(false);
		settings.setContentAreaFilled(false);

		settings.setBounds((int)settingsLocationWidth, (int)buttonLocationHeight, (int)settingsWidth, (int)buttonHeight);

		main.add(play);
		main.add(settings);
		main.add(temporary);
		mainMenu.add(main);
		mainMenu.pack();
		mainMenu.setLocationRelativeTo(null);
		mainMenu.setResizable(false);
		mainMenu.setVisible(true);
		//MAIN MENU CODE END

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
	public void send(PacketTo<Server> p) { pl.send(p); }
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

		Timer tickTimer = new Timer(1000/60, this);
		tickTimer.setActionCommand("tick");
		tickTimer.start();

		Timer secTimer = new Timer(1000, this);
		secTimer.setActionCommand("secUpdate");
		secTimer.start();
	}

		//Drawing panel for the home page
	private class DrawingPanel extends JPanel {

		DrawingPanel() {
			this.setPreferredSize(new Dimension(W, H));
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			// turn on antialiasing
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			//Draw game menu
			g2.drawImage(menuPNG, 0, 0, this.getWidth(), this.getHeight(), null);
			W = this.getWidth();
			H = this.getHeight();
		}
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
			//Draw game menu
			g2.drawImage(settingsPNG, 0, 0, this.getWidth(), this.getHeight(), null);
			W = this.getWidth();
			H = this.getHeight();
		}
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("tick")) tick();
		if (e.getActionCommand().equals("secUpdate")) secUpdate();
		if (settingsMenu.isVisible()){
			String actionCom = e.getActionCommand();
			String ipInput = ipAddress.getText();
			if (ipInput.equals("")){
				ip = "127.0.0.1";
			}else{
				ip = ipInput;
			}
			if ((portNum.getText()).equals("")){
				port = 2000;
			}else{
				port = Integer.parseInt(portNum.getText());
			}

			if (actionCom.equals("leave")) {
				System.out.println(ip);
				System.out.println(port);
				mainMenu.setVisible(true);
				mainMenu.setLocationRelativeTo(null);
				settingsMenu.setVisible(false);

			}
		}
		if (mainMenu.isVisible()) {
			String action = e.getActionCommand();
			if (action.equals("play")) {
				mainMenu.setVisible(false);
				window.setVisible(true);
        		//startGame("76.66.240.46", 2345);
				startGame(ip, port);
			}
			else if (action.equals("settings")) {
				mainMenu.setVisible(false);
				settingsMenu.setVisible(true);
				settingsMenu.setLocationRelativeTo(null);


			}
		}
	}

	private PlayerInfo me;
	public PlayerInfo getMe() { return me; }

	private int fps, frame, ping, tps;
	public int getFPS() { return fps; }
	public int getPing() { return ping; }
	public int getTPS() { return tps; }
	void tick() {
		frame++;
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
	public ArrayList<PlayerInfo> getOtherPlayers() { return otherPlayers; }

	public void setServerInfo(int ping, int tps) { this.ping = ping; this.tps = tps; }
	public void setPosition(PlayerInfo me) { this.me = me; }
	public void setOtherPlayers(ArrayList<PlayerInfo> players) { otherPlayers = players; }
	
	void handleMouseMovement(int mouseX, int mouseY) {
		int relMouseX = mouseX - window.getWidth()/2;
		int relMouseY = mouseY - window.getHeight()/2;
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
}
