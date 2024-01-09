package client;

import java.awt.*;
import javax.swing.*;
import java.net.Socket;
import java.awt.event.*;
import java.util.ArrayList;
import java.net.UnknownHostException;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

import shared.*;
import server.Server;

public class Client implements LastWish, ActionListener {
	public static void main(String[] args) {
		new Client();
	}

	JFrame window, menu;
	Canvas canvas;
	DrawingPanel mainMenu;
	BufferedImage menuImage;
	int W = 600;
	int H = 600;
	Client() {
		window = new JFrame("very cool game!!");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		canvas = new Canvas(this);
		mainMenu = new DrawingPanel();

		canvas.setPreferredSize(new Dimension(W, H));

		window.add(canvas);
		menu.add(mainMenu);
		menu.pack();
		menu.setLocationRelativeTo(null);

		window.pack();
		window.setLocationRelativeTo(null);

		menu.setVisible(true);

		startGame("127.0.0.1", 2000);
	}

	private class DrawingPanel extends JPanel {

		DrawingPanel() {
			this.setPreferredSize(new Dimension(W, H));
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			// turn on antialiasing
			menuImage = Client.loadImage("src/images/mainMenu.png");
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			//Draw game name
			g2.drawImage(menuImage, 0, 0, W, H, null);

		}	
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

		Timer tickTimer = new Timer(1000/60, this);
		tickTimer.setActionCommand("tick");
		tickTimer.start();

		Timer secTimer = new Timer(1000, this);
		secTimer.setActionCommand("secUpdate");
		secTimer.start();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("tick")) tick();
		if (e.getActionCommand().equals("secUpdate")) secUpdate();
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

		//Image loader function
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
