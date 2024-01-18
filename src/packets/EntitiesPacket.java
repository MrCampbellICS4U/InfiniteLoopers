package packets;

import java.util.ArrayList;

import client.Client;
import entities.EntityInfo;

public class EntitiesPacket extends PacketTo<Client> {
	private ArrayList<EntityInfo> entities;
	public EntitiesPacket(ArrayList<EntityInfo> entities) { this.entities = entities; }
	void handle(Client c) { c.setEntities(entities); }
}
