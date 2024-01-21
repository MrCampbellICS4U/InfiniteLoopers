package entities;

import java.awt.*;
import client.Client;

public class BulletInfo extends EntityInfo {
	private int radius;
	private double angle;
	public BulletInfo(int x, int y, int radius, double angle) {
		super(x, y);
		this.radius = radius;
		this.angle = angle;
	}

	public void customDraw(Graphics g, Client c, int centreRelX, int centreRelY) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLUE);
		g2.rotate(angle, centreRelX, centreRelY);
		g2.drawImage(c.getBulletImage(), centreRelX-(radius), centreRelY-(radius), null);
		g2.rotate(-angle, centreRelX, centreRelY);
		//g.fillOval(centreRelX-radius, centreRelY-radius, radius*2, radius*2);
	}
}
