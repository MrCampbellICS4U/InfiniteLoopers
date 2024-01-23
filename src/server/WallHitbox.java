package server;

import collision.*;

public class WallHitbox extends Rectangle {
	public WallHitbox(double x, double y, double width, double height, Chunker c) {
		super(x, y, width, height, c);
	}
	public void smashInto(Hitbox h) {}

	public HitboxType getHitboxType() { return HitboxType.WALL; }
}