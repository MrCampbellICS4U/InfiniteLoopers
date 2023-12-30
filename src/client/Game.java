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
		canvas.repaint();
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

	private ArrayList<Point> otherPlayers = new ArrayList<>();
	void setOtherPlayers(ArrayList<Point> players) { otherPlayers = players; }


	private class Canvas extends JPanel {
		final private Font f = new Font("Arial", Font.PLAIN, 30);
		private int W, H; // width and height
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			W = getWidth();
			H = getHeight();

			drawGrid(g);

			g.setFont(f);
			g.drawString(fps + " fps", 20, 40);
			g.drawString(ping + " ping", 20, 80);
			g.drawString(tps + " tps", 20, 120);

			for (Point player : otherPlayers) drawPlayer(g, player.x, player.y);

			drawPlayer(g, x, y);
		}

		final private int playerWidth = 50;
		private void drawPlayer(Graphics g, int px, int py) {
			int xCentre = W/2;
			int yCentre = H/2;
			g.fillRect(px - x + xCentre - playerWidth/2, py - y + yCentre - playerWidth/2, playerWidth, playerWidth);
		}

		final private int gridWidth = 117;
		private void drawGrid(Graphics g) {
			int xCentre = W/2 - x%gridWidth;
			int yCentre = H/2 - y%gridWidth;
			for (int xLine = xCentre % gridWidth; xLine < W; xLine += gridWidth) {
				g.drawLine(xLine, 0, xLine, H);
			}
			for (int yLine = yCentre % gridWidth; yLine < H; yLine += gridWidth) {
				g.drawLine(0, yLine, W, yLine);
			}
		}
	}
}
