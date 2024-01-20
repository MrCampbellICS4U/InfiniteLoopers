package server;

import collision.*;

public class WaterHitbox extends Rectangle {
	public WaterHitbox(double x, double y, double width, double height, Chunker c) {
		super(x, y, width, height, c);
	}
	public void smashInto(Hitbox h) {}
}
