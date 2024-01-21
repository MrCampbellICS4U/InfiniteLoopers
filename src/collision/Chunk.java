package collision;

import java.util.ArrayList;

public class Chunk {
	private ArrayList<Hitbox>[] hitboxes;

	private boolean changed;
	public void update() { changed = true; }

	public Chunk() {
		int numTypes = HitboxType.values().length;
		hitboxes = new ArrayList[numTypes];
		for (int i = 0; i < numTypes; i++) {
			hitboxes[i] = new ArrayList<>();
		}
	}

	private ArrayList<Hitbox> hitboxesToAdd = new ArrayList();
	private ArrayList<Hitbox> hitboxesToRemove = new ArrayList();

	public void add(Hitbox h) { hitboxesToAdd.add(h); changed = true; }
	public void remove(Hitbox h) { hitboxesToRemove.add(h); }

	// returns the number of checks
	public int checkCollisions() {
		int checks = 0;
		if (!changed) return checks;

		for (Hitbox h : hitboxesToAdd) {
			hitboxes[h.getHitboxType().ordinal()].add(h);
		}
		hitboxesToAdd.clear();

		for (Hitbox h : hitboxesToRemove) {
			hitboxes[h.getHitboxType().ordinal()].remove(h);
		}
		hitboxesToRemove.clear();

		for (int i = 0; i < AllowedCollisions.collisions.length; i++) {
			for (HitboxType collisionType : AllowedCollisions.collisions[i]) {
				int j = collisionType.ordinal();
				if (j <= i) continue;
				for (Hitbox h1 : hitboxes[i]) {
					for (Hitbox h2 : hitboxes[j]) {
						checks++;
				 		if (h1.collides(h2)) {
				 			h1.smashInto(h2);
				 			h2.smashInto(h1);
				 		}
					}
				}
			}
		}

		changed = false;
		return checks;
	}
}
