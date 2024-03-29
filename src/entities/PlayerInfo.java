package entities;

import java.awt.*;
import client.Client;
import shared.GlobalConstants;

public class PlayerInfo extends EntityInfo {
	public double angle;
	public int radius;
	public int health;
	public int armor;
	public int id;
	public String hotBar[] = new String[3];
	public String name;
	public int kills;
 /**
  * Constructs a new PlayerInfo object with the specified parameters.
  *
  * @param x The x-coordinate of the player's position
  * @param y The y-coordinate of the player's position
  * @param id The ID of the player
  * @param radius The radius of the player
  * @param angle The angle of the player
  * @param health The health of the player
  * @param armor The armor of the player
  * @param hotBar The hotbar items of the player
  * @param name The name of the player
  * @param kills The number of kills of the player
  */
	public PlayerInfo(int x, int y, int id, int radius, double angle, int health, int armor, String[] hotBar,
			String name, int kills) {
		super(x, y);
		this.radius = radius;
		this.angle = angle;
		this.health = health;
		this.armor = armor;
		this.hotBar = hotBar;
		this.id = id;
		this.name = name;
		this.kills = kills;
	}

	/**
	 * Custom draw method to render a player on the graphics object.
	*
	* @param g The graphics object to draw on.
	* @param c The client object representing the player.
	* @param centreRelX The relative x-coordinate of the center of the player.
	* @param centreRelY The relative y-coordinate of the center of the player.
	*/
	public void customDraw(Graphics g, Client c, int centreRelX, int centreRelY) {
		boolean drawingSelf = id == c.getID();

		if (drawingSelf) {
			centreRelX = 1300 / 2; // TODO: UN-HARDCODE THIS
			centreRelY = 800 / 2;
		}

		g.setColor(Color.RED);
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(angle, centreRelX, centreRelY);
		g.drawImage(c.getImage(), centreRelX - 15, centreRelY - 15, 80, 40, null);
		g2.rotate(-angle, centreRelX, centreRelY);
		// g.drawLine(centreRelX, centreRelY, centreRelX + (int) (Math.cos(angle) *
		// length), centreRelY + (int) (Math.sin(angle) * length));
		g2.setColor(Color.BLACK);
		g2.setFont(c.getFont());
		int nameLength = name.length();
		if (c.drawName) {
			g2.drawString(name, centreRelX - (8 * nameLength) / 2, centreRelY - 30);
		}

		if (drawingSelf)
			g.setColor(c.getCanvas().getPlayerColor());
		else
			g.setColor(Color.RED);
		g.fillOval(centreRelX - radius, centreRelY - radius, radius * 2, radius * 2);
	}
}
