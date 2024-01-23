package client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import packets.InputPacket;
import shared.Input;

class GameMouseListener extends MouseAdapter {
	Client c;
	boolean firstTime = false;

	public GameMouseListener(Client c) {
		this.c = c;
	}

	public void mouseMoved(MouseEvent e) {
		c.handleMouseMovement(e.getX(), e.getY());

	}

	public void mousePressed(MouseEvent e) {
		c.send(new InputPacket(Input.ATTACK));
	}

	public void mouseDragged(MouseEvent e) {
		c.handleMouseMovement(e.getX(), e.getY());
		c.send(new InputPacket(Input.ATTACK));
	}
}
