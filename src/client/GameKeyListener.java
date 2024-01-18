package client;

import java.awt.event.*;

import shared.Input;
import shared.InputState;
import packets.InputPacket;

class GameKeyListener extends KeyAdapter {
	Client c;
	GameKeyListener(Client client) { this.c = client; }

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
			case KeyEvent.VK_P     -> c.send(new InputPacket(Input.Dead));
			case KeyEvent.VK_M, KeyEvent.VK_TAB -> c.toggleMap();
		}
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W -> c.send(new InputPacket(Input.UP,    InputState.UP));
			case KeyEvent.VK_S -> c.send(new InputPacket(Input.DOWN,  InputState.UP));
			case KeyEvent.VK_A -> c.send(new InputPacket(Input.LEFT,  InputState.UP));
			case KeyEvent.VK_D -> c.send(new InputPacket(Input.RIGHT, InputState.UP));
		}
	}
}
