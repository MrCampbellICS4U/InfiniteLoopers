package shared;

import java.util.ArrayList;

import client.Client;

public class OtherPlayersPacket extends PacketTo<Client> {
	private ArrayList<PlayerInfo> players;
	public OtherPlayersPacket(ArrayList<PlayerInfo> players) { this.players = players; }
	void handle(Client c) { c.handleOtherPlayers(players); }
}
