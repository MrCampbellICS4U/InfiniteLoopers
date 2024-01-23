package client;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;

import shared.GlobalConstants;
import entities.*;
import world.Tiles.*;

public class Canvas extends JPanel {
	final private Font f = new Font("Arial", Font.PLAIN, 30);

	public int W; // width and height
	public int H;
	public int mouseX, mouseY;
	public int numKills;
	BufferedImage healthImage, armorImage, gunImage, deathImage, winImage;

	private Client client;
	public GlobalConstants gc;
	private HashMap<String, BufferedImage> TileImages = loadImages();

	public Canvas(Client c) {
		client = c;
		this.gc = c.gc;
		healthImage = Canvas.loadImage("res/game/UI/heart.png");
		armorImage = Canvas.loadImage("res/game/UI/armor.png");
		gunImage = Canvas.loadImage("res/game/Guns/ak.png");
		deathImage = Canvas.loadImage("res/Menus/Death.png");
		winImage = Canvas.loadImage("res/Menus/Win.png");
	}
	public int currentKills = 0;
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		PlayerInfo me = client.getMe();
		if (me == null)
			return; // we haven't got a packet from the server telling us our position yet

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color darkGreen = new Color(0, 102, 0);
		g.setColor(darkGreen);
		g.fillRect(0, 0, W, H);
		g.setColor(Color.BLACK);
		W = getWidth();
		H = getHeight();

		drawTerrain(g); // also draws entities and grid
		drawBorder(g); // draw border over everything else
		drawUI(g, me);
		drawStats(client, g);
		if (me.health == 0)
			drawDeath(g, me);
		if (me.kills > currentKills){
			numKills++;
			currentKills++;
		}
		if (numKills >= gc.KILLS_TO_WIN)
			drawWin(g, me);
	}

	Random rand = new Random();
	int red = rand.nextInt(255) + 1;
	int green = rand.nextInt(255) + 1;
	int blue = rand.nextInt(255) + 1;
	Color playerColor = new Color(red, green, blue);

	public Color getPlayerColor() {
		return playerColor;
	}

	private void drawUI(Graphics g, PlayerInfo p) {
		int itemHotbarSize = 80;
		for (int i = 0; i < p.health; i++) {
			g.drawImage(healthImage, (-30 + i * 75), 700, 200, 100, null);
		}
		for (int i = 0; i < p.armor; i++) {
			g.drawImage(armorImage, (37 + i * 78), 650, 60, 55, null);
		}
		g.setColor(Color.BLACK);
		((Graphics2D) g).setStroke(new BasicStroke(10.0f));

		// for (int i = 0; i < gc.MAXHOTBAR;i++){g.drawOval((975 +i*100),
		// 700, itemHotbarSize, itemHotbarSize);}
		// g.setColor(new Color(50, 50, 50, 100));
		// for (int i = 0; i < gc.MAXHOTBAR;i++){g.fillOval((975 +i*100),
		// 700, itemHotbarSize, itemHotbarSize);}
	}

	public void repositionMouse() {
		// calculate the new position of the mouse relative to the canvas
		int x = this.mouseX;
		int y = this.mouseY;

		// convert the mouse position to the screen
		Point p = new Point();
		SwingUtilities.convertPointFromScreen(new Point(x, y), this);

		// target mouse coords
		int newMouseX = Math.max(-1, Math.min(this.W + 1, p.x));
		int newMouseY = Math.max(28, Math.min(this.H + 1, p.y));

		// Move the mouse back inside
		try {
			Robot robot = new Robot();
			robot.mouseMove(newMouseX, newMouseY);
		} catch (AWTException ex) {
			ex.printStackTrace();
		}
	}

	final private int gridWidth = 100;

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
					// rotate the image 90 degrees 3 times and put it in the hashmap with the
					// rotation agle at the end of the key
					for (int i = 1; i < 4; i++) {
						images.put(type + "_" + state + "_" + i,
								rotateImage(images.get(type + "_" + state), i * 90));
					}
				}
			} catch (NullPointerException e) {
				System.out.println("Error loading images for type: " + type);
				e.printStackTrace();
			}
		}
		return images;
	}

	private BufferedImage rotateImage(BufferedImage originalImage, double degrees) {
		int w = originalImage.getWidth();
		int h = originalImage.getHeight();
		BufferedImage rotatedImage = new BufferedImage(w, h, originalImage.getType());
		Graphics2D g2 = rotatedImage.createGraphics();

		// Calculate the center of the image
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(degrees), w / 2, h / 2);

		g2.setTransform(at);
		g2.drawImage(originalImage, 0, 0, null);
		g2.dispose();

		return rotatedImage;
	}

	private void drawTerrain(Graphics g) {

		PlayerInfo me = client.getMe();

		ArrayList<Tile> tiles = client.getVisibleTiles();
		for (int layer = 0; layer < 3; layer++) {
			if (layer == 2) {
				// drawGrid(g);
				for (EntityInfo entity : client.getEntities()) {
					entity.draw(g, client, me.xGlobal, me.yGlobal);
				}
			}

			for (Tile currentTile : tiles) {
				if (currentTile == null || currentTile.getType().equals("air") || currentTile.getZ() != layer)
					continue;
				// Tile currentTile = tiles[x][y][z];

				// int offsetX = me.xGlobal % gridWidth;
				// int offsetY = me.yGlobal % gridWidth;
				
				client.setMapColour(currentTile);

				int groundRelX = currentTile.getX() * gc.TILE_WIDTH - me.xGlobal
						+ gc.DRAWING_AREA_WIDTH / 2;
				int groundRelY = currentTile.getY() * gc.TILE_HEIGHT - me.yGlobal
						+ gc.DRAWING_AREA_HEIGHT / 2;

				// if the player is close enough, don't render the ceiling
				if (layer == 2
						&& currentTile.getType().equals("roof")
						&& Math.abs(groundRelX + gc.TILE_WIDTH / 2 - gc.DRAWING_AREA_WIDTH
								/ 2) < gc.CEILING_DISAPPEARING_DISTANCE
						&& Math.abs(groundRelY + gc.TILE_HEIGHT / 2 - gc.DRAWING_AREA_HEIGHT
								/ 2) < gc.CEILING_DISAPPEARING_DISTANCE)
					continue;

				// rotatte the image based on the orientation of the tile
				if (currentTile.getOrientation() != 0) {
					g.drawImage(TileImages.get(currentTile.getType().substring(0, 1).toUpperCase()
							+ currentTile.getType().substring(1) + "_" + currentTile.getState() + ".png" + "_"
							+ currentTile.getOrientation()), groundRelX, groundRelY, gridWidth, gridWidth,
							null);
				} else {
					BufferedImage image = TileImages.get(currentTile.getType().substring(0, 1).toUpperCase()
							+ currentTile.getType().substring(1) + "_" + currentTile.getState() + ".png");
					g.drawImage(image, groundRelX, groundRelY, gridWidth, gridWidth, null);
				}

			}
		}
	}

	public void drawDeath(Graphics g, PlayerInfo p) {
		Color reddish = new Color(135, 0, 0, 50);
		g.setColor(reddish);
		g.fillRect(0, 0, W, H);
		g.drawImage(deathImage, 0, 0, gc.DRAWING_AREA_WIDTH, gc.DRAWING_AREA_HEIGHT, null);
		g.setColor(Color.BLACK);
		// Font f = new Font("Arial", Font.PLAIN, 70);
		// g.setFont(f);
		// g.drawString("Thanks for playing! Click enter to exit.", 50, 700);
	}
	public void drawWin(Graphics g, PlayerInfo p) {
		Color reddish = new Color(0, 135, 0, 50);
		g.setColor(reddish);
		g.fillRect(0, 0, W, H);
		g.drawImage(winImage, 0, 0, gc.DRAWING_AREA_WIDTH, gc.DRAWING_AREA_HEIGHT, null);
		g.setColor(Color.BLACK);
		// Font f = new Font("Arial", Font.PLAIN, 70);
		// g.setFont(f);
		// g.drawString("Thanks for playing! Click enter to exit.", 50, 700);
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

	private void drawBorder(Graphics g) {
		PlayerInfo me = client.getMe();

		int xCanvasCentre = W / 2;
		int yCanvasCentre = H / 2;

		Graphics2D g2 = (Graphics2D) g;

		int borderX1 = 0 - me.xGlobal + xCanvasCentre;
		int borderX2 = gc.WORLD_TILE_WIDTH * gc.TILE_WIDTH - me.xGlobal + xCanvasCentre;
		int borderY1 = 0 - me.yGlobal + yCanvasCentre;
		int borderY2 = gc.WORLD_TILE_HEIGHT * gc.TILE_HEIGHT - me.yGlobal + yCanvasCentre;

		g2.setStroke(new BasicStroke(20));
		g2.setColor(Color.BLACK);
		g.drawLine(borderX1, borderY1, borderX2, borderY1);
		g.drawLine(borderX1, borderY2, borderX2, borderY2);
		g.drawLine(borderX1, borderY1, borderX1, borderY2);
		g.drawLine(borderX2, borderY1, borderX2, borderY2);
		g2.setStroke(new BasicStroke(1));
	}

	public void drawStats(Client c, Graphics g) {
		if (c.showStats) {
			Font font1 = new Font("Arial", Font.PLAIN, 40);
			g.setFont(font1);
			g.drawString("x: " + c.getMe().xGlobal / gc.TILE_WIDTH, 20, 50);
			g.drawString("y: " + c.getMe().yGlobal / gc.TILE_HEIGHT, 20, 100);
			g.drawString(c.getFPS() + " fps", 20, 150);
			g.drawString(c.getPing() + " ping", 20, 200);
			g.drawString(c.getTPS() + " tps", 20, 250);
			g.drawString("collision checks/tick: " + String.format("%.2f", c.getCollisionChecksPerFrame()), 20, 300);
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
