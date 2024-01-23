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

	/**
	 * Creates a new Bullet object with the specified parameters.
	 *
	 * @param x         The x-coordinate of the bullet's position
	 * @param y         The y-coordinate of the bullet's position
	 * @param radius    The radius of the bullet
	 * @param angle     The angle at which the bullet is fired
	 * @param speed     The speed at which the bullet travels
	 * @param shooterID The ID of the entity that fired the bullet
	 * @param c         The Chunker object used for collision detection
	 * @param server    The Server object representing the game server
	 * @param gc        The GlobalConstants object containing game constants
	 */
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

	/**
	 * Handles the collision between this object and a given hitbox.
	 * If the hitbox is an instance of SClient, it calls the getShot() method on the
	 * client
	 * and removes this object if the client's ID is not equal to the shooter's ID.
	 * If the hitbox is an instance of WallHitbox, it simply removes this object.
	 *
	 * @param h The hitbox to collide with.
	 */
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

	/**
	 * Retrieves the information of the entity.
	 *
	 * @return The information of the entity, including its position, radius, and
	 *         angle.
	 */
	public EntityInfo getInfo() {
		return new BulletInfo((int) getX(), (int) getY(), (int) getRadius(), angle);
	}

	/**
	 * Updates the position of the object based on the elapsed time since the last
	 * update.
	 * Also checks if the object has reached its death time and removes it if
	 * necessary.
	 *
	 * @param deltaTime The elapsed time since the last update, in seconds.
	 */
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
