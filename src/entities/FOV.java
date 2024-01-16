package entities;

public class FOV extends Entity {
	SClient client;
	public FOV(double x, double y, double width, double height, SClient client, Chunker c) {
		super(x, y, width, height, c);
		this.client = client;
	}
	public void addEntity(Entity e) {
		client.addEntity(e);
	}
}
