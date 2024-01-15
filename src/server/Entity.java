package server;

// an entity that can collide
public abstract class Entity {
	private float xx, yy; // the centre of the entity
	private Chunker chunker;

	public float getX() { return xx; }
	public float getY() { return yy; }

	public int getX1() { return (int)(xx - width/2); } // right x
	public int getX2() { return (int)(xx + width/2); } // left x
	public int getY1() { return (int)(yy - height/2); } // top y
	public int getY2() { return (int)(yy + height/2); } // bottom y

	public void setX(float x) {
		int oldX = (int)xx;
		xx = x;
		chunker.updateEntity(this, oldX, (int)yy);
	}
	public void setX(int x) { setX((float)x); }

	public void setY(float y) {
		int oldY = (int)yy;
		yy = y;
		chunker.updateEntity(this, (int)xx, oldY);
	}
	public void setY(int y) { setY((float)y); }

	private final int width, height; // width and height of the smallest rectangle that fully contains the entity
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public Entity(int x, int y, int width, int height, Chunker c) {
		xx = x;
		yy = y;
		this.width = width;
		this.height = height;
		this.chunker = c;
		c.addEntity(this);
	}
}
