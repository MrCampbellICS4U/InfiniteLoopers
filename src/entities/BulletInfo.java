package entities;

import java.awt.*;
import client.Client;

public class BulletInfo extends EntityInfo {
	private int radius;
	private double angle;

 /**
  * Constructs a BulletInfo object with the specified coordinates, radius, and angle.
  *
  * @param x The x-coordinate of the bullet
  * @param y The y-coordinate of the bullet
  * @param radius The radius of the bullet
  * @param angle The angle at which the bullet is fired
  */
	public BulletInfo(int x, int y, int radius, double angle) {
		super(x, y);
		this.radius = radius;
		this.angle = angle;
	}

 /**
  * Custom draw method to draw a bullet on the screen.
  *
  * @param g The graphics object to draw on
  * @param c The client object containing the bullet image
  * @param centreRelX The x-coordinate of the center of the bullet relative to the screen
  * @param centreRelY The y-coordinate of the center of the bullet relative to the screen
  */
	public void customDraw(Graphics g, Client c, int centreRelX, int centreRelY) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLUE);
		g2.rotate(angle, centreRelX, centreRelY);
		g2.drawImage(c.getBulletImage(), centreRelX - (radius), centreRelY - (radius), null);
		g2.rotate(-angle, centreRelX, centreRelY);
		// g.fillOval(centreRelX-radius, centreRelY-radius, radius*2, radius*2);
	}
}
