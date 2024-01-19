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
	private long deathTime;
	public Bullet(double x, double y, double radius, double angle, double speed, int senderID, Chunker c, Server server) {
		super(x, y, radius, c);
		this.speed = speed;
		this.angle = angle;
		this.senderID = senderID;
		server.addEntity(this);
		deathTime = System.currentTimeMillis() + 2000; // despawn after 2 seconds
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

	public void update(double deltaTime) {
		setPosition(getX() + Math.cos(angle)*speed*deltaTime,
				getY() + Math.sin(angle)*speed*deltaTime);
		if (System.currentTimeMillis() >= deathTime) remove();
	}

	private boolean shouldRemove = false;
	public void remove() { shouldRemove = true; }
	public boolean shouldRemove() { return shouldRemove; }

	public Hitbox getHitbox() { return this; }
}
