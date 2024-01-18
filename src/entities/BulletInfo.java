package entities;

import java.awt.*;
import client.Client;

public class BulletInfo extends EntityInfo {
	private int radius;
	public BulletInfo(int x, int y, int radius) {
		super(x, y);
		this.radius = radius;
	}

	public void customDraw(Graphics g, Client c, int centreRelX, int centreRelY) {
		g.setColor(Color.BLUE);
		g.fillOval(centreRelX-radius, centreRelY-radius, radius*2, radius*2);
	}
}
