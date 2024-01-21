package collision;

public abstract class Hitbox {
	private double x, y; // the centre of the entity
	private Chunker chunker;

	public double getX() { return x; }
	public double getY() { return y; }

	public double getX1() { return x - width/2; } // right x
	public double getX2() { return x + width/2; } // left x
	public double getY1() { return y - height/2; } // top y
	public double getY2() { return y + height/2; } // bottom y

	public void setPosition(double newX, double newY) {
		double oldX = x;
		double oldY = y;

		x = newX;
		y = newY;

		chunker.updateHitbox(this, oldX, oldY);
	}

	private final double width, height; // width and height of the smallest rectangle that fully contains the entity
	public double getWidth() { return width; }
	public double getHeight() { return height; }

	public Hitbox(double x, double y, double width, double height, Chunker c) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.chunker = c;
		chunker.addHitbox(this);
	}

	// does this hitbox collide with another?
	abstract public boolean collides(Hitbox h);

	// what to do on collision
	abstract public void smashInto(Hitbox h);

	abstract public HitboxType getHitboxType();
}
