package collision;

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

	private int toChunkX(double x) { return ((int)x)/chunkWidth; }
	private int toChunkY(double y) { return ((int)y)/chunkHeight; }

	public void addHitbox(Hitbox e) {
		for (int x = toChunkX(e.getX1()); x <= toChunkX(e.getX2()); x++) {
			for (int y = toChunkX(e.getY1()); y <= toChunkY(e.getY2()); y++) {
				if (x < 0 || width <= x || y < 0 || height <= y) continue;
				chunks[x][y].add(e);
			}
		}
	}

	private void removeHitbox(Hitbox e, int x1, int x2, int y1, int y2) {
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				if (x < 0 || width <= x || y < 0 || height <= y) continue;
				chunks[x][y].remove(e);
			}
		}
	}
	public void removeHitbox(Hitbox e) {
		removeHitbox(e, toChunkX(e.getX1()), toChunkX(e.getX2()), toChunkY(e.getY1()), toChunkY(e.getY2()));
	}

	public void updateHitbox(Hitbox e, double oldX, double oldY) {
		if (oldX == e.getX() && oldY == e.getY()) return; // hitbox didn't move

		int oldX1Chunk = toChunkX(oldX - e.getWidth()/2);
		int oldX2Chunk = toChunkX(oldX + e.getWidth()/2);
		int oldY1Chunk = toChunkY(oldY - e.getHeight()/2);
		int oldY2Chunk = toChunkY(oldY + e.getHeight()/2);

		// not changing chunks
		if (oldX1Chunk == toChunkX(e.getX1()) && oldX2Chunk == toChunkY(e.getX2())
			&& oldY1Chunk == toChunkY(e.getY1()) && oldY2Chunk == toChunkY(e.getY2())) {
			for (int x = oldX1Chunk; x <= oldX2Chunk; x++) {
				for (int y = oldY1Chunk; y <= oldY2Chunk; y++) {
					if (x < 0 || width <= x || y < 0 || height <= y) continue;
					chunks[x][y].update();
				}
			}
			return;
		}

		removeHitbox(e, oldX1Chunk, oldX2Chunk, oldY1Chunk, oldY2Chunk);
		addHitbox(e);
	}

	// returns the number of collision checks
	public int checkCollisions() {
		int checks = 0;
		for (Chunk[] slice : chunks) {
			for (Chunk c : slice) {
				checks += c.checkCollisions();
			}
		}
		return checks;
	}
}
