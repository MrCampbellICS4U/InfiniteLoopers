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
import entities.PlayerInfo;
import world.Tiles.Tile;

public class MapDrawing extends JFrame {
	DrawingPanel panel;
	int mapSizeW = 700;
	int mapSizeH = 700;
	Client c;
	public GlobalConstants gc;

	MapDrawing(Client c, PlayerInfo player) {
		this.c = c;
		this.gc = c.gc;
		DrawingPanel panel = new DrawingPanel();
		Color darkGreen = new Color(0, 102, 0);
		panel.setBackground(darkGreen);
		panel.repaint();
		this.add(panel);
		this.setUndecorated(true);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setFocusTraversalKeysEnabled(false);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.addKeyListener(new GameKeyListener(c));
	}

	private class DrawingPanel extends JPanel {
		DrawingPanel() {
			this.setPreferredSize(new Dimension(mapSizeW, mapSizeH));
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			PlayerInfo me = c.getMe();
			if (me == null)
				return;

			Graphics2D g2 = (Graphics2D) g;
			int xMapCentre = mapSizeW / 2;
			int yMapCentre = mapSizeH / 2;
			// turn on antialiasing
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// draw tiles on map
			g2.drawImage(c.getMapImage(), 0, 0, null);

			// draw player on map
			double playerRelX = (((double) me.xGlobal) / gc.WORLD_WIDTH);
			double playerRelY = (((double) me.yGlobal) / gc.WORLD_HEIGHT);
			g.setColor(Color.black);
			g.fillOval((int) (playerRelX * mapSizeW), (int) (playerRelY * mapSizeH), 10, 10);
		}
	}

	void toggleMap() {
		this.setVisible(false);
	}
}
