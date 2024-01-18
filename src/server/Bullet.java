package server;

import collision.Chunker;
import collision.Circle;
import collision.Hitbox;
import entities.BulletInfo;
import entities.Entity;
import entities.EntityInfo;

public class Bullet extends Circle implements Entity {
	final private double angle;
	private double speed;
	private int senderID;
	private Server server;
	private int ticksAlive;
	public Bullet(double x, double y, double radius, double angle, double speed, int senderID, Chunker c, Server server) {
		super(x, y, radius, c);
		this.speed = speed;
		this.angle = angle;
		this.senderID = senderID;
		this.server = server;
		server.addEntity(this);
	}

	public void smashInto(Hitbox h) {
		if (h instanceof SClient) {
			SClient client = (SClient)h;
			if (client.getID() == senderID) return; // hit self
			client.getShot();
			this.remove();
		}
	}

	public EntityInfo getInfo() {
		return new BulletInfo((int)getX(), (int)getY(), (int)getRadius());
	}

	public void update() {
		setPosition(getX() + Math.cos(angle)*speed, getY() + Math.sin(angle)*speed);
		ticksAlive++;
		if (ticksAlive >= 3000) remove();
	}

	public void remove() {
		super.remove();
		server.removeEntity(this);
	}
}
