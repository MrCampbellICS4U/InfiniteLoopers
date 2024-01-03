package shared;

import java.io.Serializable;

// the shared class that holds information about players
public class PlayerInfo implements java.io.Serializable {
	public int x, y;
	public PlayerInfo(int x, int y) { this.x = x; this.y = y; }
}
