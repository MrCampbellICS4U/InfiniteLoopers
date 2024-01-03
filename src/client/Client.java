package client;

import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.net.UnknownHostException;

import shared.*;

public class Client implements LastWish {
	public static void main(String[] args) {
		new Client("127.0.0.1", 2000);
	}

	private PacketLord<Client> pl;
	Client(String ip, int port) {
		try {
			Socket socket = new Socket(ip, port);
			pl = new PacketLord<Client>(socket, this);
		} catch (UnknownHostException e) {
			handleException("Could not connect to server", e);
		} catch (IOException e) {
			handleException("Could not connect to server", e);
		}
	}

	// server acknowledged connection, we can start sending packets
	// before this, we don't know our id
	Game game;
	public void start(int id, int x, int y) {
		pl.setID(id);
		pl.send(new ReadyPacket()); // acknowledge that we're ready (see note in server/SClient.java)
		System.out.println("Connected!");

		game = new Game(id, pl, x, y);
	}

	public void handlePing(int ms) { game.setPing(ms); }
	public void handleTPS(int ms) { game.setTPS(ms); }
	public void handlePosition(PlayerInfo player) { game.setPosition(player.x, player.y); }
	public void handleOtherPlayers(ArrayList<PlayerInfo> players) { game.setOtherPlayers(players); }

	public void handleException(String message, Exception e) {
		System.out.println(message);
		e.printStackTrace();
		System.exit(1);
	}
	public void handleDisconnection(int id, Exception e) {
		System.out.println("Server closed");
		e.printStackTrace();
		System.exit(1);
	}
}
