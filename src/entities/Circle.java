package entities;

public class Circle extends Entity {
	private double radius;
	public Circle(double x, double y, double radius, Chunker c) {
		super(x, y, radius*2, radius*2, c);
		this.radius = radius;
	}
}
