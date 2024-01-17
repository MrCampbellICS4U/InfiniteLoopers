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
}
