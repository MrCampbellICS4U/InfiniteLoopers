package shared;

import java.awt.Point;
import java.util.ArrayList;

import client.Client;

public class OtherPlayersPacket extends PacketTo<Client> {
	private ArrayList<Point> players;
	public OtherPlayersPacket(ArrayList<Point> players) { this.players = players; }
	void handle(Client c) { c.handleOtherPlayers(players); }
}
