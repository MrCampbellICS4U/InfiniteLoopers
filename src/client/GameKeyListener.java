package client;

import java.awt.event.*;

import shared.Input;
import shared.InputState;
import packets.InputPacket;

class GameKeyListener extends KeyAdapter {
	Client c;
	GameKeyListener(Client client) { this.c = client; }

	/**
	 * Handles key press events and sends corresponding input packets to the server.
	*
	* @param e The KeyEvent object representing the key press event.
	*/
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W -> c.send(new InputPacket(Input.UP,    InputState.DOWN));
			case KeyEvent.VK_S -> c.send(new InputPacket(Input.DOWN,  InputState.DOWN));
			case KeyEvent.VK_A -> c.send(new InputPacket(Input.LEFT,  InputState.DOWN));
			case KeyEvent.VK_D -> c.send(new InputPacket(Input.RIGHT, InputState.DOWN));

			case KeyEvent.VK_SPACE -> c.send(new InputPacket(Input.ATTACK));
			case KeyEvent.VK_R     -> c.send(new InputPacket(Input.RELOAD));
			case KeyEvent.VK_E     -> c.send(new InputPacket(Input.USE));
			case KeyEvent.VK_Q     -> c.send(new InputPacket(Input.DROP));
			case KeyEvent.VK_P     -> c.send(new InputPacket(Input.SUICIDE));
			case KeyEvent.VK_BACK_QUOTE -> c.toggleStats();
			case KeyEvent.VK_M, KeyEvent.VK_TAB -> c.toggleMap();
			case KeyEvent.VK_F -> c.toggleName();
		}
	}

	/**
	 * Handles the key release event and sends the corresponding input packet to the server.
	*
	* @param e The KeyEvent object representing the key release event.
	*/
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W -> c.send(new InputPacket(Input.UP,    InputState.UP));
			case KeyEvent.VK_S -> c.send(new InputPacket(Input.DOWN,  InputState.UP));
			case KeyEvent.VK_A -> c.send(new InputPacket(Input.LEFT,  InputState.UP));
			case KeyEvent.VK_D -> c.send(new InputPacket(Input.RIGHT, InputState.UP));
		}
	}
}
