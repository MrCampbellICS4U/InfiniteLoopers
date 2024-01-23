package collision;

import java.util.ArrayList;

public class Chunk {
	private ArrayList<Hitbox>[] hitboxes;

	private boolean changed;

	public void update() {
		changed = true;
	}

	/**
	 * Constructs a new Chunk object.
	 * Initializes the hitboxes array with an ArrayList for each HitboxType.
	 * Each ArrayList is initially empty.
	 */
	public Chunk() {
		int numTypes = HitboxType.values().length;
		hitboxes = new ArrayList[numTypes];
		for (int i = 0; i < numTypes; i++) {
			hitboxes[i] = new ArrayList<>();
		}
	}

	private ArrayList<Hitbox> hitboxesToAdd = new ArrayList();
	private ArrayList<Hitbox> hitboxesToRemove = new ArrayList();

	public void add(Hitbox h) {
		hitboxesToAdd.add(h);
		changed = true;
	}

	public void remove(Hitbox h) {
		hitboxesToRemove.add(h);
	}

	/**
	 * Checks for collisions between hitboxes and performs collision actions if
	 * necessary.
	 * 
	 * @return The number of collision checks performed.
	 */
	public int checkCollisions() {
		int checks = 0;
		if (!changed)
			return checks;

		for (int i = 0; i < hitboxesToAdd.size(); i++) {
			Hitbox h = hitboxesToAdd.get(i);
			hitboxes[h.getHitboxType().ordinal()].add(h);
		}
		hitboxesToAdd.clear();

		for (int i = 0; i < hitboxesToRemove.size(); i++) {
			Hitbox h = hitboxesToRemove.get(i);
			hitboxes[h.getHitboxType().ordinal()].remove(h);
		}
		hitboxesToRemove.clear();

		for (int i = 0; i < AllowedCollisions.collisions.length; i++) {
			for (HitboxType collisionType : AllowedCollisions.collisions[i]) {
				int j = collisionType.ordinal();
				if (j <= i)
					continue;
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
