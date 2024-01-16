package server;

// an entity that can collide
public abstract class Entity {
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
		
		chunker.updateEntity(this, oldX, oldY);
	}

	private final double width, height; // width and height of the smallest rectangle that fully contains the entity
	public double getWidth() { return width; }
	public double getHeight() { return height; }

	public Entity(double x, double y, double width, double height, Chunker c) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.chunker = c;
		chunker.addEntity(this);
	}

	public void remove() {
		chunker.removeEntity(this);
	}
}
