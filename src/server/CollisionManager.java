package server;

// the thing that manages and detects collisions
public class CollisionManager {
	private Entity[][] chunks;
	
	public CollisionManager(int width, int height) {
		chunks = new Entity[width][height];
	}
}
