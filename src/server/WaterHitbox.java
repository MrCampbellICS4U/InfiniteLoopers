package server;

import collision.*;

public class WaterHitbox extends Rectangle {
	/**
	 * Constructs a WaterHitbox object with the specified position, dimensions, and
	 * associated Chunker.
	 *
	 * @param x      The x-coordinate of the top-left corner of the hitbox
	 * @param y      The y-coordinate of the top-left corner of the hitbox
	 * @param width  The width of the hitbox
	 * @param height The height of the hitbox
	 * @param c      The Chunker object associated with the hitbox
	 */
	public WaterHitbox(double x, double y, double width, double height, Chunker c) {
		super(x, y, width, height, c);
	}

	public void smashInto(Hitbox h) {
	}

	/**
	 * Retrieves the type of hitbox for an entity.
	 *
	 * @return The type of hitbox, which is set to "WATER" in this case.
	 */
	public HitboxType getHitboxType() {
		return HitboxType.WATER;
	}
}
