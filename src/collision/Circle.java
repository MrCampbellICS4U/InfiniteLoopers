package collision;

public abstract class Circle extends Hitbox {
	private double radius;

	public double getRadius() {
		return radius;
	}

	/**
	 * Constructs a Circle object with the specified coordinates, radius, and
	 * chunker.
	 *
	 * @param x      The x-coordinate of the center of the circle
	 * @param y      The y-coordinate of the center of the circle
	 * @param radius The radius of the circle
	 * @param c      The chunker object associated with the circle
	 */
	public Circle(double x, double y, double radius, Chunker c) {
		super(x, y, radius * 2, radius * 2, c);
		this.radius = radius;
	}

	/**
	 * Checks if this Hitbox collides with another Hitbox.
	 *
	 * @param h The Hitbox to check for collision.
	 * @return true if there is a collision, false otherwise.
	 */
	public boolean collides(Hitbox h) {
		if (h instanceof Circle)
			return collides((Circle) h);
		if (h instanceof Rectangle)
			return h.collides(this);
		return false;
	}

	/**
	 * Checks if this circle collides with another circle.
	 *
	 * @param c The circle to check collision with.
	 * @return True if the circles collide, false otherwise.
	 */
	public boolean collides(Circle c) {
		double dist = Math.hypot(c.getX() - getX(), c.getY() - getY());
		return dist <= radius + c.getRadius();
	}

	/**
	 * Checks if a given point (x, y) is contained within the circle.
	 *
	 * @param x The x-coordinate of the point
	 * @param y The y-coordinate of the point
	 * @return true if the point is within the circle, false otherwise
	 */
	public boolean contains(double x, double y) {
		return Math.hypot(x - getX(), y - getY()) <= radius;
	}

	/**
	 * Checks if a line segment collides with a circle.
	 *
	 * @param x1 The x-coordinate of the starting point of the line segment
	 * @param y1 The y-coordinate of the starting point of the line segment
	 * @param x2 The x-coordinate of the ending point of the line segment
	 * @param y2 The y-coordinate of the ending point of the line segment
	 * @return true if the line segment collides with the circle, false otherwise
	 */
	public boolean lineCollision(double x1, double y1, double x2, double y2) {
		// https://mathworld.wolfram.com/Circle-LineIntersection.html
		// does this circle collide with the the line whose endpoints are (x1, y1) and (x2, y2)?
		x1 -= getX();
		x2 -= getX();
		y1 -= getY();
		y2 -= getY();
		double len = Math.hypot(x2 - x1, y2 - y1);
		double d = x1 * y2 - x2 * y1;
		return radius * radius * len * len - d * d > 0;
	}
}
