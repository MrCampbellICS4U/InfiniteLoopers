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
				Hitbox h1 = hitboxes.get(i);
				Hitbox h2 = hitboxes.get(j);
				if (h1.collides(h2)) {
					h1.smashInto(h2);
					h2.smashInto(h1);
				}
			}
		}
		changed = false;

		return checks;
	}
}
