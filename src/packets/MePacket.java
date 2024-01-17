package packets;

import client.Client;
import entities.PlayerEntity;

// this packet holds information about the client it's being sent to
public class MePacket extends PacketTo<Client> {
	private PlayerEntity player;
	public MePacket(PlayerEntity player) { this.player = player; }
	void handle(Client c) { c.setMe(player); }
}
