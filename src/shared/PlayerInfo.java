package shared;

import java.io.Serializable;

// the shared class that holds information about players
public class PlayerInfo implements java.io.Serializable {
	public int x, y;
	public double angle;
	public int health;
	public PlayerInfo(int x, int y, double angle, int health) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.health = health;
	}
}
