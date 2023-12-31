package client;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

class Canvas extends JPanel {
	final private Font f = new Font("Arial", Font.PLAIN, 30);
	private int W, H; // width and height
	private int fps, ping, tps, x, y;
	private ArrayList<Point> otherPlayers;

	public void draw(int fps, int ping, int tps, int x, int y, ArrayList<Point> otherPlayers) {
		this.fps = fps; this.ping = ping; this.tps = tps; this.x = x; this.y = y; this.otherPlayers = otherPlayers;
		repaint();
	}

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
		g.drawString("Hi", x, playerWidth);
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
