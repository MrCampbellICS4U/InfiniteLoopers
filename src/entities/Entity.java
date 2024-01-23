package entities;

import collision.Hitbox;

/**
 * Represents an entity in the game. Interface meaning all entities must contain these methods
 */
public interface Entity {
	public EntityInfo getInfo();
	public void update(double deltaTime);

	public void remove();
	public boolean shouldRemove();

	public Hitbox getHitbox();
}
