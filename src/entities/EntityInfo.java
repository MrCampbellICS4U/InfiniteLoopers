package entities;

import client.Client;
import java.awt.Graphics;
import java.io.Serializable;
import shared.GlobalConstants;

// the shared class that holds information about players and gets sent from the server to clients
// and draws them on the client
public abstract class EntityInfo implements Serializable {
	public int xGlobal, yGlobal, id;
	public EntityInfo(int x, int y, int id) {
		this.xGlobal = x;
		this.yGlobal = y;
		this.id = id;
	}
	public void draw(Graphics g, Client c, int playerX, int playerY) {
		customDraw(g, c, xGlobal - playerX + GlobalConstants.DRAWING_AREA_WIDTH/2,
				yGlobal - playerY + GlobalConstants.DRAWING_AREA_HEIGHT/2);
	}

	// the x and y of the centre of the entity
	protected abstract void customDraw(Graphics g, Client c, int centreRelX, int centreRelY);
}
