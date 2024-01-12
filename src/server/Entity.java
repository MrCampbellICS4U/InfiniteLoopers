package server;

// an entity that can collide
public abstract class Entity {
	private float xx, yy;
	private CollisionManager collisionManager;

	public float getX() { return xx; }
	public float getY() { return yy; }
	
	public void setX(float x) {
		xx = x;
	}
	public void setX(int x) { setX((float)x); }

	public void setY(float y) {
		yy = y;
	}
	public void setY(int y) { setY((float)y); }

	// does this entity collide with another?
	public abstract boolean collides(Entity e);
}
