package packets;

import java.util.ArrayList;

import client.Client;
import entities.Entity;

public class EntitiesPacket extends PacketTo<Client> {
	private ArrayList<Entity> entities;
	public EntitiesPacket(ArrayList<Entity> entities) { this.entities = entities; }
	void handle(Client c) { c.setEntities(entities); }
}
