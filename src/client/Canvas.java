package client;

import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;

import shared.PlayerInfo;

class Canvas extends JPanel {
	final private Font f = new Font("Arial", Font.PLAIN, 30);
	private int W, H; // width and height
	private Client c;
	BufferedImage healthImage, armorImage;

	public Canvas(Client c) {
		this.c = c;
		c.getMe();
		healthImage = Client.loadImage("res/game/UI/heart.png");
		armorImage = Client.loadImage("res/game/UI/armor.png");
	}
	Random rand = new Random();

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (c.getMe() == null) return; // we haven't got a packet from the server telling us our position yet

		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color darkGreen  = new Color(0, 102, 0);
		g.setColor(darkGreen);
		g.fillRect(0, 0, W, H);
		g.setColor(Color.BLACK);
		W = getWidth();
		H = getHeight();

		drawGrid(g);

		g.setFont(f);
		g.drawString(c.getFPS() + " fps", 20, 40);
		g.drawString(c.getPing() + " ping", 20, 80);
		g.drawString(c.getTPS() + " tps", 20, 120);
		for (PlayerInfo player : c.getOtherPlayers()) drawPlayer(g, player);

		drawPlayer(g, c.getMe());
		drawUI(g, c.getMe());
	}

	final private int playerWidth = 50;
	int red = rand.nextInt(255) + 1;
	int green = rand.nextInt(255) + 1;
	int blue = rand.nextInt(255) + 1;
	private void drawPlayer(Graphics g, PlayerInfo p) {
		int xCentre = W/2;
		int yCentre = H/2;

		PlayerInfo me = c.getMe();
		int playerRelX = p.x - me.x + xCentre;
		int playerRelY = p.y - me.y + yCentre;
		if (me.equals(p)){

			Color playerColor = new Color(red, green, blue);
			g.setColor(playerColor);

		}else{
			g.setColor(Color.RED);
		}
		g.fillOval(playerRelX - playerWidth/2, playerRelY - playerWidth/2, playerWidth, playerWidth);
		
		int length = 100;
		g.setColor(Color.RED);
		g.drawLine(playerRelX, playerRelY, playerRelX + (int)(Math.cos(p.angle)*length), playerRelY + (int)(Math.sin(p.angle)*length));
	}
	private void drawUI(Graphics g, PlayerInfo p) {
		switch (p.health){
			case 1:
				g.drawImage(healthImage, -30, 700, 200, 100, null);
				break;

			case 2:
				g.drawImage(healthImage, -30, 700, 200, 100, null);
				g.drawImage(healthImage, 45, 700, 200, 100, null);
				break;
			case 3:
				g.drawImage(healthImage, -30, 700, 200, 100, null);
				g.drawImage(healthImage, 45, 700, 200, 100, null);
				g.drawImage(healthImage, 120, 700, 200, 100, null);
				break;
		}
		switch (p.armor){
			case 1:
				g.drawImage(armorImage, 37, 650, 60, 55, null);
				break;

			case 2:
				g.drawImage(armorImage, 37, 650, 60, 55, null);
				g.drawImage(armorImage, 115, 650, 60, 55, null);
				break;
			case 3:
				g.drawImage(armorImage, 37, 650, 60, 55, null);
				g.drawImage(armorImage, 115, 650, 60, 55, null);
				g.drawImage(armorImage, 187, 650, 60, 55, null);
				break;
		}

	}
	
	final private int gridWidth = 100;
	private void drawGrid(Graphics g) {
		PlayerInfo me = c.getMe();
		int xCentre = W/2 - me.x%gridWidth;
		int yCentre = H/2 - me.y%gridWidth;

		for (int xLine = xCentre % gridWidth; xLine < W; xLine += gridWidth) {
			g.drawLine(xLine, 0, xLine, H);
		}
		for (int yLine = yCentre % gridWidth; yLine < H; yLine += gridWidth) {
			g.drawLine(0, yLine, W, yLine);
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
