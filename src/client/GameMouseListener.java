package client;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import packets.InputPacket;
import shared.Input;

class GameMouseListener extends MouseAdapter {
	Client c;
	boolean firstTime = false;

	public GameMouseListener(Client c) {
		this.c = c;
	}

	public void mouseMoved(MouseEvent e) {

		try {
			// if the mouse left the window put the mouse in the closest location to the
			// window
			// that is still in the window use Robot to move the mouse
			int n = 0;
			Robot robot = new Robot();
			// get the mouse location
			Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
			// get the window location
			Point windowLocation = c.canvas.getLocationOnScreen();
			// if the mouse is to the left of the window
			// System.out.println("Mouse exited " + (mouseLocation.y > windowLocation.y +
			// c.gc.DRAWING_AREA_HEIGHT));
			if (mouseLocation.x < windowLocation.x) {
				// move the mouse to the left side of the window
				robot.mouseMove(windowLocation.x, mouseLocation.y);
			}
			// if the mouse is to the right of the window
			if (mouseLocation.x > windowLocation.x + c.gc.DRAWING_AREA_WIDTH) {
				// move the mouse to the right side of the window
				while (mouseLocation.x > windowLocation.x + c.gc.DRAWING_AREA_WIDTH && (n++) < 5) {
					robot.mouseMove(windowLocation.x + c.gc.DRAWING_AREA_WIDTH, mouseLocation.y);
					mouseLocation = MouseInfo.getPointerInfo().getLocation();
				}
				n = 0;
			}
			// if the mouse is above the window
			if (mouseLocation.y < windowLocation.y) {
				// move the mouse to the top of the window
				robot.mouseMove(mouseLocation.x, windowLocation.y);
			}
			// if the mouse is below the window
			if (mouseLocation.y > windowLocation.y + c.gc.DRAWING_AREA_HEIGHT) {
				System.out.println("Mouse exited bottom");
				// move the mouse to the bottom of the window
				while (mouseLocation.y > windowLocation.y + c.gc.DRAWING_AREA_HEIGHT && (n++) < 5) {
					robot.mouseMove(mouseLocation.x, windowLocation.y + c.gc.DRAWING_AREA_HEIGHT);
					mouseLocation = MouseInfo.getPointerInfo().getLocation();
				}
			}

		} catch (AWTException ex) {
			ex.printStackTrace();
		}

		c.handleMouseMovement(e.getX(), e.getY());

	}

	public void mouseExited(MouseEvent e) {
		try {
			// if the mouse left the window put the mouse in the closest location to the
			// window
			// that is still in the window use Robot to move the mouse
			Robot robot = new Robot();
			int n = 0;

			// get the mouse location
			Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
			// get the window location
			Point windowLocation = c.canvas.getLocationOnScreen();
			// get the window size
			Dimension windowSize = c.canvas.getSize();
			// if the mouse is to the left of the window
			// System.out.println("Mouse exited " + mouseLocation.x + " " +
			// windowLocation.x);
			if (mouseLocation.x < windowLocation.x) {
				// move the mouse to the left side of the window
				robot.mouseMove(windowLocation.x, mouseLocation.y);
			}
			// if the mouse is to the right of the window
			if (mouseLocation.x > windowLocation.x + c.gc.DRAWING_AREA_WIDTH) {
				// move the mouse to the right side of the window
				while (mouseLocation.x > windowLocation.x + c.gc.DRAWING_AREA_WIDTH && (n++) < 50) {
					robot.mouseMove(windowLocation.x + c.gc.DRAWING_AREA_WIDTH, mouseLocation.y);
					mouseLocation = MouseInfo.getPointerInfo().getLocation();
				}
				n = 0;
			}
			// if the mouse is above the window
			if (mouseLocation.y < windowLocation.y) {
				// move the mouse to the top of the window
				robot.mouseMove(mouseLocation.x, windowLocation.y);
			}
			// if the mouse is below the window
			if (mouseLocation.y > windowLocation.y + c.gc.DRAWING_AREA_HEIGHT) {
				System.out.println("Mouse exited bottom");
				// move the mouse to the bottom of the window
				while (mouseLocation.y > windowLocation.y + c.gc.DRAWING_AREA_HEIGHT && (n++) < 50) {
					robot.mouseMove(mouseLocation.x, windowLocation.y + c.gc.DRAWING_AREA_HEIGHT);
					mouseLocation = MouseInfo.getPointerInfo().getLocation();
				}
			}

		} catch (AWTException ex) {
			ex.printStackTrace();
		}
	}

	public void mousePressed(MouseEvent e) {
		c.send(new InputPacket(Input.ATTACK));
	}
}
