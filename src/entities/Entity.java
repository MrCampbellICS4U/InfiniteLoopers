package entities;

import collision.Hitbox;

public interface Entity {
	public EntityInfo getInfo();
	public void update();

	public void remove();
	public boolean shouldRemove();

	public Hitbox getHitbox();
}
