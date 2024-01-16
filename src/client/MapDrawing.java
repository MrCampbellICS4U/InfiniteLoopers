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
import game.world.Tiles.Tile;

public class MapDrawing extends JFrame{
    DrawingPanel panel;
    int mapSizeW = 700;
    int mapSizeH= 700;
    Client c;
    MapDrawing() {
		
        DrawingPanel panel = new DrawingPanel();
		Color darkGreen = new Color(0, 102, 0);
		panel.setBackground(darkGreen);
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
			Graphics2D g2 = (Graphics2D) g;
			// turn on antialiasing
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// Draw game menu
		}
	}
	void toggleMap(){
		this.setVisible(false);
	}
}
