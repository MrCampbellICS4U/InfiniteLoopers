package entities;

import client.Client;
import java.awt.Graphics;
import java.io.Serializable;
import shared.GlobalConstants;

// the shared class that holds information about players and gets sent from the server to clients
// and draws them on the client
public abstract class EntityInfo implements Serializable {
	public int xGlobal, yGlobal;

	public EntityInfo(int x, int y) {
		this.xGlobal = x;
		this.yGlobal = y;
	}

	public void draw(Graphics g, Client c, int playerX, int playerY) {
		customDraw(g, c, xGlobal - playerX + 1300 / 2,
				yGlobal - playerY + 800 / 2);// TODO: UN-HARDCODE THIS
	}

	// the x and y of the centre of the entity
	protected abstract void customDraw(Graphics g, Client c, int centreRelX, int centreRelY);
}
