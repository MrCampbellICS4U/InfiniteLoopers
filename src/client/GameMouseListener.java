package client;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

class GameMouseListener extends MouseAdapter {
	Client c;
	public GameMouseListener(Client c) { this.c = c; }
	public void mouseMoved(MouseEvent e) {
		c.handleMouseMovement(e.getX(), e.getY());
	}
}
