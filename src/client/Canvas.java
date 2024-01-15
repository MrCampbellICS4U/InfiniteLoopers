package client;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

import shared.GlobalConstants;
import shared.PlayerInfo;
import game.world.Tiles.Tile;

class Canvas extends JPanel {
	final private Font f = new Font("Arial", Font.PLAIN, 30);
	private int W, H; // width and height of the canvas
	private Client client;
	private static final int CEILING_DISAPPEARING_DISTANCE = 100;
	private HashMap<String, BufferedImage> TileImages = loadImages();

	public Canvas(Client client) {
		this.client = client;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (client.getMe() == null)
			return; // we haven't got a packet from the server telling us our position yet

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color darkGreen = new Color(0, 102, 0);
		g.setColor(darkGreen);
		g.fillRect(0, 0, W, H);
		g.setColor(Color.BLACK);
		W = getWidth();
		H = getHeight();

		drawTerrain(g);

		g.setColor(Color.BLACK);
		g.setFont(f);
		g.drawString(client.getFPS() + " fps", 20, 40);
		g.drawString(client.getPing() + " ping", 20, 80);
		g.drawString(client.getTPS() + " tps", 20, 120);
		g.drawString("x: " + client.getMe().xGlobal / GlobalConstants.TILE_WIDTH, 20, 160);
		g.drawString("y: " + client.getMe().yGlobal / GlobalConstants.TILE_HEIGHT, 20, 200);
		for (PlayerInfo player : client.getOtherPlayers())
			drawPlayer(g, player);

		drawPlayer(g, client.getMe());
	}

	final private int playerWidth = 50;

	private void drawPlayer(Graphics g, PlayerInfo player) {
		int xCanvasCentre = W / 2;
		int yCanvasCentre = H / 2;

		PlayerInfo me = client.getMe();
		int playerRelX = player.xGlobal - me.xGlobal + xCanvasCentre;
		int playerRelY = player.yGlobal - me.yGlobal + yCanvasCentre;
		g.setColor(Color.BLACK);
		g.fillOval(playerRelX - playerWidth / 2, playerRelY - playerWidth / 2, playerWidth, playerWidth);

		int length = 100;
		g.setColor(Color.RED);
		g.drawLine(playerRelX, playerRelY, playerRelX + (int) (Math.cos(player.angle) * length),
				playerRelY + (int) (Math.sin(player.angle) * length));
	}

	final private int gridWidth = 100;

	// load image from tile
	private BufferedImage loadImage(Tile tile) {
		String imageURL = tile.getStatesMap().get(tile.getState());
		// load the image
		try {
			return ImageIO.read(new File(imageURL));
		} catch (IOException e) {
			System.out.println("Error loading image: " + imageURL);
			e.printStackTrace();
		}

		return null;
	}

	// method to load all images into a hashmap
	private HashMap<String, BufferedImage> loadImages() {
		HashMap<String, BufferedImage> images = new HashMap<>();
		for (String type : new File("res/game/world/Tiles").list()) {
			try {
				for (String state : new File("res/game/world/Tiles/" + type).list()) {
					String imageURL = "res/game/world/Tiles/" + type + "/" + state;
					try {
						images.put(type + "_" + state, (BufferedImage) ImageIO.read(new File(imageURL)));
					} catch (IOException e) {
						System.out.println("Error loading image: " + imageURL);
						e.printStackTrace();
					}
				}
			} catch (NullPointerException e) {
				System.out.println("Error loading images for type: " + type);
				e.printStackTrace();
			}
		}
		return images;
	}

	private void drawTerrain(Graphics g) {

		PlayerInfo me = client.getMe();

		ArrayList<Tile> tiles = client.getVisibleTiles();
		for (Tile currentTile : tiles) {
			if (currentTile == null || currentTile.getType().equals("air"))
				continue;
			// Tile currentTile = tiles[x][y][z];

			// int offsetX = me.xGlobal % gridWidth;
			// int offsetY = me.yGlobal % gridWidth;

			int groundRelX = currentTile.getX() * GlobalConstants.TILE_WIDTH - me.xGlobal
					+ GlobalConstants.DRAWING_AREA_WIDTH / 2;
			int groundRelY = currentTile.getY() * GlobalConstants.TILE_HEIGHT - me.yGlobal
					+ GlobalConstants.DRAWING_AREA_HEIGHT / 2;

			BufferedImage image = TileImages.get(currentTile.getType().substring(0, 1).toUpperCase()
					+ currentTile.getType().substring(1) + "_" + currentTile.getState() + ".png");

			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			int imageRelX = groundRelX - imageWidth / 2;
			int imageRelY = groundRelY - imageHeight / 2;

			g.drawImage(image, groundRelX, groundRelY, gridWidth, gridWidth, null);
		}

		drawGrid(g);
	}

	private void drawGrid(Graphics g) { // deprecated (soon)
		PlayerInfo me = client.getMe();
		int xCentre = W / 2 - me.xGlobal % gridWidth;
		int yCentre = H / 2 - me.yGlobal % gridWidth;

		g.setColor(new Color(0, 0, 0, 50));
		for (int xLine = xCentre % gridWidth; xLine < W; xLine += gridWidth) {
			g.drawLine(xLine, 0, xLine, H);
		}
		for (int yLine = yCentre % gridWidth; yLine < H; yLine += gridWidth) {
			g.drawLine(0, yLine, W, yLine);
		}
	}
}
