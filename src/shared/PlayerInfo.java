package shared;

import java.io.Serializable;

// the shared class that holds information about players
public class PlayerInfo implements Serializable {
	public int xGlobal, yGlobal;
	public double angle;
	public int health;
	public int armor;
	public PlayerInfo(int x, int y, double angle, int health, int armor) {
		this.xGlobal = x;
		this.yGlobal = y;
		this.angle = angle;
		this.health = health;
		this.armor = armor;
	}
}
