package server;

import java.net.Socket;
import java.util.ArrayList;

import shared.*;

// clients from the server's perspective
class SClient extends PacketLord<Server> {
	private double xx, yy;

	public int getX() { return (int)xx; }
	public int getY() { return (int)yy; }

	private double angle;

	public double setAngle(double angle) { return this.angle = angle; }
	public double getAngle() { return angle; }
	
	public PlayerInfo getInfo() { return new PlayerInfo(getX(), getY(), angle); }

	SClient(Socket socket, Server state, int id) {
		super(socket, state);
		setID(id);
	}

	// is the client ready to receive messages?
	// had a weird issue where i had to wait a bit before sending a ton of messages to the client;
	// it would throw a `UTFDataFormatException` if i immediately started sending messages after opening the socket
	private boolean ready = false;
	public void setReady() { ready = true; }

	private boolean up, down, left, right;
	public void handleInput(Input i, InputState is) {
		boolean isDown = is == InputState.DOWN;
		switch (i) {
			case UP    -> up    = isDown;
			case DOWN  -> down  = isDown;
			case LEFT  -> left  = isDown;
			case RIGHT -> right = isDown;
			
			case ATTACK -> attack();
			case RELOAD -> reload();
			case USE    -> useItem();
			case DROP   -> dropItem();
		}
	}
	
	// todo implement
	private void attack() {
		System.out.printf("Client %d unleashed a devastating attack!\n", getID());
	}
	
	// todo implement
	private void reload() {
		System.out.printf("Client %d attempts to reload\n", getID());
	}
	
	// todo implement
	private void useItem() {
		System.out.printf("Client %d tries to use an item\n", getID());
	}
	
	// todo implement
	private void dropItem() {
		System.out.printf("Client %d drops something on the ground\n", getID());
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

		send(new MePacket(getX(), getY(), getAngle()));
		send(new OtherPlayersPacket(otherPlayers));
	}

	// all the other players this one can see
	private ArrayList<PlayerInfo> otherPlayers;
	public void clearOtherPlayers() { otherPlayers = new ArrayList<>(); }
	public void addOtherPlayer(PlayerInfo player) { otherPlayers.add(player); }
}
