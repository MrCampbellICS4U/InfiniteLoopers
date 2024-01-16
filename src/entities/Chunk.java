package entities;

import java.util.ArrayList;

public class Chunk {
	ArrayList<Entity> entities = new ArrayList<>();
	boolean changed;
	public void add(Entity e) { entities.add(e); }
	public void remove(Entity e) { entities.remove(e); }

	// returns the number of checks
	public int checkCollisions() {
		int checks = 0;
		if (!changed) return checks;

		for (int i = 0; i < entities.size(); i++) {
			for (int j = i+1; j < entities.size(); j++) {
				Entity e1 = entities.get(i);
				Entity e2 = entities.get(j);
				String t1 = e1.getClass().getSimpleName();
				String t2 = e2.getClass().getSimpleName();
				checks++;
				if (collides(e1, e2)) {
					System.out.println("Collision at time " + System.currentTimeMillis());
					if (t1.equals("FOV") && !t2.equals("FOV")) {
						FOV fov = (FOV)e1;
						fov.addEntity(e2);
					} else if (t2.equals("FOV") && !t1.equals("FOV")) {
						FOV fov = (FOV)e2;
						fov.addEntity(e1);
					}
				}
			}
		}
		changed = false;

		return checks;
	}
	// do the 1d lines (a1, a2) and (b1, b2) collide?
	private boolean lineCollision(double a1, double a2, double b1, double b2) {
		return (a1 <= b1 && b1 <= a2) || (b1 <= a1 && a1 <= b2);
	}
	private boolean collides(Entity e1, Entity e2) {
		boolean xCollision = lineCollision(e1.getX1(), e1.getX2(), e2.getX1(), e2.getX2());
		if (!xCollision) return false;
		boolean yCollision = lineCollision(e1.getY1(), e1.getY2(), e2.getY1(), e2.getY2());
		return yCollision;
	}
}
