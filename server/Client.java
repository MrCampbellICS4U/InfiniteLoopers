package server;

import java.net.Socket;

import shared.PacketLord;

class Client extends PacketLord<Server> {
	Client(Socket socket, Server state, int id) {
		super(socket, state);
		setID(id);
	}
}
