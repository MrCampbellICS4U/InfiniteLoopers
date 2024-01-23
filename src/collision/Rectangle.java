package collision;

/**
 * Represents a rectangle hitbox that extends the Hitbox class.
 * 
 * @param x      The x-coordinate of the top-left corner of the rectangle
 * @param y      The y-coordinate of the top-left corner of the rectangle
 * @param width  The width of the rectangle
 * @param height The height of the rectangle
 * @param c      The Chunker object associated with the rectangle
 */
public abstract class Rectangle extends Hitbox {
	public Rectangle(double x, double y, double width, double height, Chunker c) {
		super(x, y, width, height, c);
	}

	/**
	 * Checks if this Hitbox collides with another Hitbox.
	 *
	 * @param h The Hitbox to check collision with.
	 * @return true if there is a collision, false otherwise.
	 */
	public boolean collides(Hitbox h) {
		if (h instanceof Rectangle)
			return collides((Rectangle) h);
		if (h instanceof Circle)
			return collides((Circle) h);
		return false;
	}

	/**
	 * Checks if a given circle collides with this rectangle.
	 *
	 * @param c The circle to check for collision.
	 * @return True if the circle collides with the rectangle, false otherwise.
	 */
	public boolean collides(Circle c) {
		// clamp the circle into the rectangle, then see if that point's in the circle
		double cX = Math.max(getX1(), Math.min(c.getX(), getX2()));
		double cY = Math.max(getY1(), Math.min(c.getY(), getY2()));
		return c.contains(cX, cY);
	}

	/**
	 * Checks if the current rectangle collides with another rectangle.
	 *
	 * @param r The rectangle to check collision with.
	 * @return True if there is a collision, false otherwise.
	 */
	public boolean collides(Rectangle r) {
		boolean xCollision = lineCollision(getX1(), getX2(), r.getX1(), r.getX2());
		if (!xCollision)
			return false;
		boolean yCollision = lineCollision(getY1(), getY2(), r.getY1(), r.getY2());
		return yCollision;
	}

	/**
	 * Checks if two line segments collide.
	 *
	 * @param a1 The starting point of the first line segment
	 * @param a2 The ending point of the first line segment
	 * @param b1 The starting point of the second line segment
	 * @param b2 The ending point of the second line segment
	 * @return true if the line segments collide, false otherwise
	 */
	// do the 1d lines (a1, a2) and (b1, b2) collide?
	private boolean lineCollision(double a1, double a2, double b1, double b2) {
		return (a1 <= b1 && b1 <= a2) || (b1 <= a1 && a1 <= b2);
	}
}
