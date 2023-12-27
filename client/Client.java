package client;

import shared.*;
import java.io.*;
import java.net.*;

public class Client {
	public static void main(String[] args) {
		new Client("127.0.0.1", 2000);
	}

	PacketLord pl;
	Client(String ip, int port) {
		try {
		Socket socket = new Socket(ip, port);
		pl = new PacketLord(socket, this);

		//pl.send(new Ping());
		} catch (Exception e) {}
	}

	// server acknowledged connection
	public void start(int id) {
		pl.setID(id);
		System.out.println("Connected!");
		pl.send(new Ping());
	}

	public void handlePing(int ms) { System.out.println("Ping: " + ms); }
}
