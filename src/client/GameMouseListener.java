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

 /**
  * Handles the mouse movement event by passing the coordinates to the controller.
  *
  * @param e The MouseEvent containing the mouse movement information
  */
	public void mouseMoved(MouseEvent e) {
		c.handleMouseMovement(e.getX(), e.getY());
	}

 /**
  * Handles the event when the mouse is pressed.
  * Sends an input packet to the server to indicate an attack action.
  *
  * @param e The MouseEvent object representing the mouse press event
  */
	public void mousePressed(MouseEvent e) {
		c.send(new InputPacket(Input.ATTACK));
	}

 /**
  * Handles the event when the mouse is being dragged.
  *
  * @param e The MouseEvent object containing information about the event
  */
	public void mouseDragged(MouseEvent e) {
		c.handleMouseMovement(e.getX(), e.getY());
	}
}
