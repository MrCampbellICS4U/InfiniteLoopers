package packets;

import java.util.ArrayList;

import client.Client;
import entities.EntityInfo;

public class EntitiesPacket extends PacketTo<Client> {
	private ArrayList<EntityInfo> entities;

	/**
	 * Constructs an EntitiesPacket object with the given list of entities.
	 *
	 * @param entities The list of entities to include in the packet.
	 */
	public EntitiesPacket(ArrayList<EntityInfo> entities) {
		this.entities = entities;
	}

	/**
	 * Handles the client by setting the entities.
	 *
	 * @param c The client to handle
	 */
	void handle(Client c) {
		c.setEntities(entities);
	}
}
