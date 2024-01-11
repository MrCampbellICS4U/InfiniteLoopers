package client;

import java.awt.*;
import javax.swing.*;

import java.net.Socket;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import shared.*;
import packets.*;
import server.Server;

public class Client implements LastWish, ActionListener {
	public static void main(String[] args) {
		new Client();
	}

	JFrame window;
	Canvas canvas;
	Client() {
		window = new JFrame("very cool game!!");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setFocusTraversalKeysEnabled(false); // allow us to detect tab

		canvas = new Canvas(this);
		canvas.setPreferredSize(new Dimension(581, 628));
		window.add(canvas);

		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		startGame("127.0.0.1", 2000);
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
	
	void handleMouseMovement(int mouseX, int mouseY) {
		int relMouseX = mouseX - window.getWidth()/2;
		int relMouseY = mouseY - window.getHeight()/2;
		double angle = Math.atan2(relMouseY, relMouseX);
		send(new ClientPlayerRotationPacket(angle));
	}
	
	boolean mapOpen = false;
	// todo implement
	public void toggleMap() {
		mapOpen = !mapOpen;
		System.out.printf("The map is now %s\n", mapOpen ? "open" : "closed");
	}
}
