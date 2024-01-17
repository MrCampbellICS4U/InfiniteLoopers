package server;

import collision.Chunker;
import collision.Circle;
import collision.Hitbox;
import entities.BulletEntity;
import entities.Entity;
import entities.Renderable;

public class Bullet extends Circle implements Renderable {
	final private double angle;
	private double speed;
	private int senderID;
	private Server server;
	public Bullet(double x, double y, double radius, double angle, double speed, int senderID, Chunker c, Server server) {
		super(x, y, radius, c);
		this.speed = speed;
		this.angle = angle;
		this.senderID = senderID;
		this.server = server;
		server.addRenderable(this);
	}

	public void smashInto(Hitbox h) {
		if (h instanceof SClient) {
			SClient client = (SClient)h;
			if (client.getID() == senderID) return; // hit self
			System.out.println("Bullet hit something!");
			this.remove();
		}
	}

	public Entity getInfo() {
		return new BulletEntity((int)getX(), (int)getY(), (int)getRadius());
	}

	public void update() {
		setPosition(getX() + Math.cos(angle)*speed, getY() + Math.sin(angle)*speed);
	}

	public void remove() {
		super.remove();
		server.removeRenderable(this);
	}
}