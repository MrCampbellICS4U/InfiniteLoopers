package packets;

import client.Client;
import shared.PlayerInfo;

// this packet holds information about the client it's being sent to
public class MePacket extends PacketTo<Client> {
	private PlayerInfo player;
	public MePacket(PlayerInfo player) { this.player = player; }
	void handle(Client c) { c.setMe(player); }
}
