package client;

import java.awt.event.*;

import shared.Input;
import shared.InputState;
import shared.InputPacket;

class GameKeyListener extends KeyAdapter {
	Game g;
	GameKeyListener(Game g) { this.g = g; }

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W -> g.send(new InputPacket(Input.UP,    InputState.DOWN));
			case KeyEvent.VK_S -> g.send(new InputPacket(Input.DOWN,  InputState.DOWN));
			case KeyEvent.VK_A -> g.send(new InputPacket(Input.LEFT,  InputState.DOWN));
			case KeyEvent.VK_D -> g.send(new InputPacket(Input.RIGHT, InputState.DOWN));
		}
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W -> g.send(new InputPacket(Input.UP,    InputState.UP));
			case KeyEvent.VK_S -> g.send(new InputPacket(Input.DOWN,  InputState.UP));
			case KeyEvent.VK_A -> g.send(new InputPacket(Input.LEFT,  InputState.UP));
			case KeyEvent.VK_D -> g.send(new InputPacket(Input.RIGHT, InputState.UP));
		}
	}
}
