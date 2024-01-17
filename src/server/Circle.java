package server;

public class Circle extends Entity {
	private float radius;
	public Circle(float x, float y, float radius, Chunker c) {
		super(x, y, radius*2, radius*2, c);
		this.radius = radius;
	}
}
