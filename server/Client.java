package server;

import java.net.Socket;

import shared.PacketLord;

class Client extends PacketLord<Server> {
	public int id;
	Client(Socket socket, Server state, int id) {
		super(socket, state);
		this.id = id;
	}
}
