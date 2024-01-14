package server;

import java.util.ArrayList;

public class Chunk {
	ArrayList<Entity> entities = new ArrayList<>();
	boolean changed;
	public void add(Entity e) { entities.add(e); }
	public void remove(Entity e) { entities.remove(e); }
	public void checkCollisions() {
		if (!changed) return;

		for (int i = 0; i < entities.size(); i++) {
			for (int j = i+1; j < entities.size(); j++) {
				if (collides(entities.get(i), entities.get(j))) System.out.println("Collision");
			}
		}
		changed = false;
	}
	// do the 1d lines (a1, a2) and (b1, b2) collide?
	private boolean lineCollision(int a1, int a2, int b1, int b2) {
		return (a1 <= b1 && b1 <= a2) || (b1 <= a1 && a1 <= b2);
	}
	private boolean collides(Entity e1, Entity e2) {
		boolean xCollision = lineCollision(e1.getX1(), e1.getX2(), e1.getX1(), e2.getX2());
		if (!xCollision) return false;
		boolean yCollision = lineCollision(e1.getY1(), e1.getY2(), e1.getY1(), e2.getY2());
		return yCollision;
	}
}
