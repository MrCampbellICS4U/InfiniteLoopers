package server;

// an entity that can collide
public abstract class Entity {
	private float x, y; // the centre of the entity
	private Chunker chunker;

	public float getX() { return x; }
	public float getY() { return y; }

	public float getX1() { return x - width/2; } // right x
	public float getX2() { return x + width/2; } // left x
	public float getY1() { return y - height/2; } // top y
	public float getY2() { return y + height/2; } // bottom y

	public void setX(float newX) {
		float oldX = x;
		x = newX;
		chunker.updateEntity(this, oldX, y);
	}

	public void setY(float newY) {
		float oldY = y;
		y = newY;
		chunker.updateEntity(this, x, oldY);
	}

	private final float width, height; // width and height of the smallest rectangle that fully contains the entity
	public float getWidth() { return width; }
	public float getHeight() { return height; }

	public Entity(float x, float y, float width, float height, Chunker c) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.chunker = c;
		c.addEntity(this);
	}
}
