package entities;

import java.awt.*;
import client.Client;
import shared.GlobalConstants;

// the shared class that holds information about players
public class PlayerEntity extends Entity {
	public double angle;
	public int radius;
	public int health;
	public int armor;
	public String hotBar[] = new String[3];
	public PlayerEntity(int x, int y, int id, int radius, double angle, int health, int armor, String[] hotBar) {
		super(x, y, id);
		this.radius = radius;
		this.angle = angle;
		this.health = health;
		this.armor = armor;
		this.hotBar = hotBar;
	}

	public void customDraw(Graphics g, Client c, int centreRelX, int centreRelY) {
		if (id == c.getID()) {
			centreRelX = GlobalConstants.DRAWING_AREA_WIDTH/2;
			centreRelY = GlobalConstants.DRAWING_AREA_HEIGHT/2;
			g.setColor(c.getCanvas().getPlayerColor());
		} else g.setColor(Color.RED);
		g.fillOval(centreRelX-radius, centreRelY-radius, radius*2, radius*2);

		// drawing red line for direction
		int length = 100;
		g.setColor(Color.RED);
		g.drawLine(centreRelX, centreRelY, centreRelX + (int) (Math.cos(angle) * length),
				centreRelY + (int) (Math.sin(angle) * length));
	}
}