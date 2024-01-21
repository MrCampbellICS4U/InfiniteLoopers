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
	public PlayerInfo(int x, int y, int id, int radius, double angle, int health, int armor, String[] hotBar, String name) {
		super(x, y);
		this.radius = radius;
		this.angle = angle;
		this.health = health;
		this.armor = armor;
		this.hotBar = hotBar;
		this.id = id;
		this.name = name;
	}
	public void customDraw(Graphics g, Client c, int centreRelX, int centreRelY) {

		g.setColor(Color.RED);
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(angle, centreRelX, centreRelY);
		g.drawImage(c.getImage(), centreRelX-15, centreRelY-15, 80, 40, null);
		g2.rotate(-angle, centreRelX, centreRelY);
		//g.drawLine(centreRelX, centreRelY, centreRelX + (int) (Math.cos(angle) * length), centreRelY + (int) (Math.sin(angle) * length));
		g2.setColor(Color.BLACK);
		g2.setFont(c.getFont());
		int nameLength = name.length();
		if (c.drawName){
			g2.drawString(name, centreRelX-(8*nameLength)/2, centreRelY-30);
		}

		if (id == c.getID()) {
			centreRelX = GlobalConstants.DRAWING_AREA_WIDTH/2;
			centreRelY = GlobalConstants.DRAWING_AREA_HEIGHT/2;
			g.setColor(c.getCanvas().getPlayerColor());
		} else g.setColor(Color.RED);
		g.fillOval(centreRelX-radius, centreRelY-radius, radius*2, radius*2);
	}
}
