package entities;

import java.io.Serializable;

// the shared class that holds information about players
public class Entity implements Serializable {
	public int xGlobal, yGlobal;
	public Entity(int x, int y) {
		this.xGlobal = x;
		this.yGlobal = y;
	}
}
