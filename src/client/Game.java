package client;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

import shared.*;
import server.Server;

class Game implements ActionListener {
	private final int id;
	private final PacketLord<Client> pl;
	void send(PacketTo<Server> p) { pl.send(p); }

	private int x, y;
	public void setPosition(int x, int y) { this.x = x; this.y = y; };

	Canvas canvas;
	Game(int id, PacketLord<Client> pl, int x, int y) {
		this.id = id;
		this.pl = pl;

		JFrame window = new JFrame("very cool game!!");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.addKeyListener(new GameKeyListener(this));

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(581, 628));
		window.add(canvas);

		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

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

	private int fps = 0;
	private int frame = 0;
	void tick() {
		frame++;
		canvas.draw(fps, ping, tps, x, y, otherPlayers);
	}

	// gets called once a second
	void secUpdate() {
		fps = frame;
		frame = 0;

		send(new PingPacket());
	}

	private int ping = 0;
	void setPing(int ping) { this.ping = ping; }
	private int tps = 0;
	void setTPS(int tps) { this.tps = tps; }

	private ArrayList<PlayerInfo> otherPlayers = new ArrayList<>();
	void setOtherPlayers(ArrayList<PlayerInfo> players) { otherPlayers = players; }
}
