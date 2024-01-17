package collision;

public abstract class Rectangle extends Hitbox {
	public Rectangle(double x, double y, double width, double height, Chunker c) {
		super(x, y, width, height, c);
	}

	public boolean collides(Hitbox h) {
		if (h instanceof Rectangle) return collides((Rectangle)h);
		if (h instanceof Circle) return collides((Circle)h);
		return false;
	}

	public boolean collides(Circle c) {
		// TODO implement
		return false;
	}

	public boolean collides(Rectangle r) {
		boolean xCollision = lineCollision(getX1(), getX2(), r.getX1(), r.getX2());
		if (!xCollision) return false;
		boolean yCollision = lineCollision(getY1(), getY2(), r.getY1(), r.getY2());
		return yCollision;
	}

	// do the 1d lines (a1, a2) and (b1, b2) collide?
	private boolean lineCollision(double a1, double a2, double b1, double b2) {
		return (a1 <= b1 && b1 <= a2) || (b1 <= a1 && a1 <= b2);
	}
}
