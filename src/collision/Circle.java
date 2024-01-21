package collision;

public abstract class Circle extends Hitbox {
	private double radius;
	public double getRadius() { return radius; }
	public Circle(double x, double y, double radius, Chunker c) {
		super(x, y, radius*2, radius*2, c);
		this.radius = radius;
	}

	public boolean collides(Hitbox h) {
		if (h instanceof Circle) return collides((Circle)h);
		if (h instanceof Rectangle) return h.collides(this);
		return false;
	}

	public boolean collides(Circle c) {
		double dist = Math.hypot(c.getX() - getX(), c.getY() - getY());
		return dist <= radius + c.getRadius();
	}

	public boolean contains(double x, double y) {
		return Math.hypot(x - getX(), y - getY()) <= radius;
	}

	// does this circle collide with the the line whose endpoints are (x1, y1) and (x2, y2)?
	public boolean lineCollision(double x1, double y1, double x2, double y2) {
		// https://mathworld.wolfram.com/Circle-LineIntersection.html
		x1 -= getX();
		x2 -= getX();
		y1 -= getY();
		y2 -= getY();
		double len = Math.hypot(x2-x1, y2-y1);
		double d = x1*y2 - x2*y1;
		return radius*radius * len*len - d*d > 0;
	}
}
