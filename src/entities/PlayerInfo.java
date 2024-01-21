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
	public PlayerInfo(int x, int y, int id, int radius, double angle, int health, int armor, String[] hotBar) {
		super(x, y);
		this.radius = radius;
		this.angle = angle;
		this.health = health;
		this.armor = armor;
		this.hotBar = hotBar;
		this.id = id;
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
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(angle, centreRelX, centreRelY);
		g.drawImage(c.getImage(), centreRelX-15, centreRelY-15, 80, 40, null);
		g2.rotate(-angle, centreRelX, centreRelY);
		//g.drawLine(centreRelX, centreRelY, centreRelX + (int) (Math.cos(angle) * length), centreRelY + (int) (Math.sin(angle) * length));
	}
}
