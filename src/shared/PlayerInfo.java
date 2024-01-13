package shared;

import java.io.Serializable;

// the shared class that holds information about players
public class PlayerInfo implements Serializable {
	public int xGlobal, yGlobal;
	public double angle;

	public PlayerInfo(int x, int y, double angle) {
		this.xGlobal = x;
		this.yGlobal = y;
		this.angle = angle;
	}
}
