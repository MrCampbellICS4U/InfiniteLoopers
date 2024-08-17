package client;

import java.awt.event.*;

import shared.Input;
import shared.InputState;
import packets.InputPacket;

class GameKeyListener extends KeyAdapter {
	Client c;

	GameKeyListener(Client client) {
		this.c = client;
	}

	/**
	 * Handles key press events and sends corresponding input packets to the server.
	 *
	 * @param e The KeyEvent object representing the key press event.
	 */
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W: c.send(new InputPacket(Input.UP,    InputState.DOWN)); break;
			case KeyEvent.VK_S: c.send(new InputPacket(Input.DOWN,  InputState.DOWN)); break;
			case KeyEvent.VK_A: c.send(new InputPacket(Input.LEFT,  InputState.DOWN)); break;
			case KeyEvent.VK_D: c.send(new InputPacket(Input.RIGHT, InputState.DOWN)); break;
			case KeyEvent.VK_SPACE:
				if (!c.gc.CAN_HOLD_TO_SHOOT) {
					c.send(new InputPacket(Input.ATTACK));
				} else {
					c.isShooting = true;
				}
				break;
			case KeyEvent.VK_R: c.send(new InputPacket(Input.RELOAD));  break;
			case KeyEvent.VK_E: c.send(new InputPacket(Input.USE));     break;
			case KeyEvent.VK_Q: c.send(new InputPacket(Input.DROP));    break;
			case KeyEvent.VK_P: c.send(new InputPacket(Input.SUICIDE)); break;

			case KeyEvent.VK_BACK_QUOTE:              c.toggleStats(); break;
			case KeyEvent.VK_F:                       c.toggleName();  break;
			case KeyEvent.VK_M: case KeyEvent.VK_TAB: c.toggleMap();   break;
		}
	}

	/**
	 * Handles the key release event and sends the corresponding input packet to the
	 * server.
	 *
	 * @param e The KeyEvent object representing the key release event.
	 */
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W: c.send(new InputPacket(Input.UP,    InputState.UP)); break;
			case KeyEvent.VK_S: c.send(new InputPacket(Input.DOWN,  InputState.UP)); break;
			case KeyEvent.VK_A: c.send(new InputPacket(Input.LEFT,  InputState.UP)); break;
			case KeyEvent.VK_D: c.send(new InputPacket(Input.RIGHT, InputState.UP)); break;
			case KeyEvent.VK_SPACE: c.isShooting = false; break;
		}
	}
}
