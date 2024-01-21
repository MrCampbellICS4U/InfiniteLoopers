package server;

import collision.*;
import entities.BulletInfo;
import entities.Entity;
import entities.EntityInfo;
import shared.GlobalConstants;

public class Bullet extends Circle implements Entity {
	final private double angle;
	private double speed;
	private int shooterID;
	private long deathTime;
	private GlobalConstants gc;

	public Bullet(double x, double y, double radius, double angle, double speed, int shooterID, Chunker c,
			Server server, GlobalConstants gc) {
		super(x, y, radius, c);
		this.speed = speed;
		this.angle = angle;
		this.shooterID = shooterID;
		server.addEntity(this);
		this.gc = gc;
		deathTime = System.currentTimeMillis() + gc.BULLET_DESPAWN_TIME;
	}

	public void smashInto(Hitbox h) {
		if (h instanceof SClient) {
			SClient client = (SClient) h;
			if (client.getID() == shooterID)
				return; // hit self
			client.getShot(shooterID);
			this.remove();
		}
		if (h instanceof WallHitbox)
			this.remove();
	}

	public EntityInfo getInfo() {
		return new BulletInfo((int) getX(), (int) getY(), (int) getRadius(), angle);
	}

	public void update(double deltaTime) {
		setPosition(getX() + Math.cos(angle) * speed * deltaTime,
				getY() + Math.sin(angle) * speed * deltaTime);
		if (System.currentTimeMillis() >= deathTime)
			remove();
	}

	private boolean shouldRemove = false;

	public void remove() {
		shouldRemove = true;
	}

	public boolean shouldRemove() {
		return shouldRemove;
	}

	public Hitbox getHitbox() {
		return this;
	}

	public HitboxType getHitboxType() {
		return HitboxType.BULLET;
	}
}
