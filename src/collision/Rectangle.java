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
		double cX = Math.max(getX(), Math.min(c.getX(), getX() + getWidth()));
		double cY = Math.max(getY(), Math.min(c.getY(), getY() + getHeight()));
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

	// do the 2d lines a and b collide, where (x1, y1) and (x2, y2) are the endpoints of each line?
	static public boolean lineCollision(double ax1, double ay1, double ax2, double ay2,
		double bx1, double by1, double bx2, double by2) {
		// m = slope
		double m1 = (ay2-ay1) / (ax2-ax1);
		double m2 = (by2-by1) / (bx2-bx1);

		// b = y intercept
		double b1 = ay1 - m1*ax1;
		double b2 = by1 - m2*bx1;

		if (m1 == m2) {
			// same slope
			// either they're parallel, or collide everywhere
			return b1 == b2;
		}


		// find x value of the point of intersection
		// m1x + b1 = m2x + b2, solve for x
		double poiX = (b2 - b1) / (m1 - m2);

		// see if the POI lies on both line segments
		// we don't know if x1 or x2 is larger though
		boolean onA = (ax1 <= poiX && poiX <= ax2) || (ax2 <= poiX && poiX <= ax1);
		boolean onB = (bx1 <= poiX && poiX <= bx2) || (bx2 <= poiX && poiX <= bx1);
		return onA && onB;
	}
}
