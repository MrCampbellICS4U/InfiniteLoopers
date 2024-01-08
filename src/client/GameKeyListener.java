package client;

import java.awt.event.*;

import shared.Input;
import shared.InputState;
import shared.InputPacket;

class GameKeyListener extends KeyAdapter {
	Client c;
	GameKeyListener(Client c) { this.c = c; }

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W -> c.send(new InputPacket(Input.UP,    InputState.DOWN));
			case KeyEvent.VK_S -> c.send(new InputPacket(Input.DOWN,  InputState.DOWN));
			case KeyEvent.VK_A -> c.send(new InputPacket(Input.LEFT,  InputState.DOWN));
			case KeyEvent.VK_D -> c.send(new InputPacket(Input.RIGHT, InputState.DOWN));
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
