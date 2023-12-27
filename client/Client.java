package client;

import java.net.Socket;
import java.io.IOException;
import java.net.UnknownHostException;

import shared.*;

public class Client implements LastWish {
	public static void main(String[] args) {
		new Client("127.0.0.1", 2000);
	}

	PacketLord pl;
	Client(String ip, int port) {
		try {
			Socket socket = new Socket(ip, port);
			pl = new PacketLord(socket, this);
		} catch (UnknownHostException e) {
			handleException("Could not connect to server", e);
		} catch (IOException e) {
			handleException("Could not connect to server", e);
		}
	}

	// server acknowledged connection
	public void start(int id) {
		pl.setID(id);
		System.out.println("Connected!");
		pl.send(new Ping());
	}

	public void handlePing(int ms) { System.out.println("Ping: " + ms); }

	public void handleException(String message, Exception e) {
		System.out.println(message);
		e.printStackTrace();
		System.exit(1);
	}
}
