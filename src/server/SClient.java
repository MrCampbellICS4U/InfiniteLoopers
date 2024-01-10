package server;

import java.awt.Point;
import java.net.Socket;
import java.util.ArrayList;

import shared.*;

// clients from the server's perspective
class SClient extends PacketLord<Server> {
	private double xx, yy;
	SClient(Socket socket, Server state, int id) {
		super(socket, state);
		setID(id);
	}

	// is the client ready to receive messages?
	// had a weird issue where i had to wait a bit before sending a ton of messages to the client;
	// it would throw a `UTFDataFormatException` if i immediately started sending messages after opening the socket
	private boolean ready = false;
	public void setReady() { ready = true; }

	public int getX() { return (int)xx; }
	public int getY() { return (int)yy; }

	private boolean up, down, left, right;
	public void handleInput(Input i, InputState is) {
		boolean isDown = is == InputState.DOWN;
		switch (i) {
			case UP    -> up    = isDown;
			case DOWN  -> down  = isDown;
			case LEFT  -> left  = isDown;
			case RIGHT -> right = isDown;
		}
	}

	private final int speed = 5;
	public void update() {
		double dx = 0, dy = 0;
		if (up) dy -= speed;
		if (down) dy += speed;
		if (left) dx -= speed;
		if (right) dx += speed;
		if (dx != 0 && dy != 0) {
			dx /= Math.sqrt(2);
			dy /= Math.sqrt(2);
		}
		xx += dx;
		yy += dy;
	}

	public void sendPackets() {
		if (!ready) return;

		send(new PositionPacket(getX(), getY()));
		send(new OtherPlayersPacket(otherPlayers));
	}

	// all the other players this one can see
	private ArrayList<Point> otherPlayers;
	public void clearOtherPlayers() { otherPlayers = new ArrayList<>(); }
	public void addOtherPlayer(Point player) { otherPlayers.add(player); }
}
