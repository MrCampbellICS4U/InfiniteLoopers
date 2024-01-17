package collision;

import java.util.ArrayList;

public class Chunk {
	ArrayList<Hitbox> hitboxes = new ArrayList<>();
	boolean changed;
	public void add(Hitbox e) { hitboxes.add(e); }
	public void remove(Hitbox e) { hitboxes.remove(e); }

	// returns the number of checks
	public int checkCollisions() {
		int checks = 0;
		if (!changed) return checks;

		for (int i = 0; i < hitboxes.size(); i++) {
			for (int j = i+1; j < hitboxes.size(); j++) {
				checks++;
				if (collides(hitboxes.get(i), hitboxes.get(j))) System.out.println("Collision at time " + System.currentTimeMillis());
			}
		}
		changed = false;

		return checks;
	}
	// do the 1d lines (a1, a2) and (b1, b2) collide?
	private boolean lineCollision(double a1, double a2, double b1, double b2) {
		return (a1 <= b1 && b1 <= a2) || (b1 <= a1 && a1 <= b2);
	}
	private boolean collides(Hitbox e1, Hitbox e2) {
		boolean xCollision = lineCollision(e1.getX1(), e1.getX2(), e2.getX1(), e2.getX2());
		if (!xCollision) return false;
		boolean yCollision = lineCollision(e1.getY1(), e1.getY2(), e2.getY1(), e2.getY2());
		return yCollision;
	}
}
