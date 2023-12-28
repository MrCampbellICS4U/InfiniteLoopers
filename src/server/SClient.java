package server;

import java.net.Socket;

import shared.PacketLord;

// clients from the server's perspective
class SClient extends PacketLord<Server> {
	SClient(Socket socket, Server state, int id) {
		super(socket, state);
		setID(id);
	}
}
