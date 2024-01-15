package server;


// the thing that manages and detects collisions
public class Chunker {
	private Chunk[][] chunks;

	private int chunkWidth, chunkHeight, width, height;
	public Chunker(int chunkWidth, int chunkHeight, int worldWidth, int worldHeight) {
		this.chunkWidth = chunkWidth;
		this.chunkHeight = chunkHeight;

		width = worldWidth/chunkWidth + 1;
		height = worldHeight/chunkHeight + 1;

		chunks = new Chunk[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				chunks[x][y] = new Chunk();
			}
		}
	}

	private int toChunkX(int x) { return x/chunkWidth; }
	private int toChunkY(int y) { return y/chunkHeight; }

	public void addEntity(Entity e) {
		for (int x = toChunkX(e.getX1()); x <= toChunkX(e.getX2()); x++) {
			for (int y = toChunkX(e.getY1()); y <= toChunkY(e.getY2()); y++) {
				chunks[x][y].add(e);
				chunks[x][y].changed = true;
			}
		}
	}

	public void removeEntity(Entity e, int x1, int x2, int y1, int y2) {
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				// don't count this as a change, since, if anything, it would just end a collision
				chunks[x][y].remove(e);
			}
		}
	}
	public void removeEntity(Entity e) {
		removeEntity(e, toChunkX(e.getX1()), toChunkX(e.getX2()), toChunkY(e.getY1()), toChunkY(e.getY2()));
	}


	public void updateEntity(Entity e, int oldX, int oldY) {
		// since the entities will never change dimensions, we don't need to check the left/bottom points (x2/y2)
		int oldX1 = oldX - e.getWidth()/2;
		int oldY1 = oldY - e.getHeight()/2;
		if (toChunkX(oldX1) == toChunkX(e.getX1()) && toChunkY(oldY1) == toChunkY(e.getY1())) return;
		removeEntity(e, oldX1, oldY1, oldX + e.getWidth()/2, oldY + e.getHeight()/2);
		addEntity(e);
	}

	public void checkCollisions() {
		for (Chunk[] slice : chunks) {
			for (Chunk c : slice) {
				c.checkCollisions();
			}
		}
	}
}
