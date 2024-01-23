package client;

import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import packets.InputPacket;
import shared.Input;

class GameMouseListener extends MouseAdapter {
	Client c;

	public GameMouseListener(Client c) {
		this.c = c;
	}

	public void mouseMoved(MouseEvent e) {

		c.handleMouseMovement(e.getX(), e.getY());
	}

	public void mouseExited(MouseEvent e) {
		System.out.println("Mouse exited" + e.getX() + " " + e.getY());

		c.canvas.mouseX = Math.max(-1, Math.min(c.canvas.W + 1, e.getX()));
		c.canvas.mouseY = Math.max(28, Math.min(c.canvas.H + 1, e.getY()));
		c.canvas.repositionMouse();
	}

	public void mousePressed(MouseEvent e) {
		c.send(new InputPacket(Input.ATTACK));
	}
}
