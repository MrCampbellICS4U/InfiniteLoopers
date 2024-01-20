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
		// clamp the circle into the rectangle, then see if that point's in the circle
		double cX = Math.max(getX1(), Math.min(c.getX(), getX2()));
		double cY = Math.max(getY1(), Math.min(c.getY(), getY2()));
		return c.contains(cX, cY);
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
