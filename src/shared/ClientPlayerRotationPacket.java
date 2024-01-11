package shared;

import server.Server;

public class ClientPlayerRotationPacket extends PacketTo<Server> {
	private double angle;
	public ClientPlayerRotationPacket(double angle) { this.angle = angle; }
	public void handle(Server s) { s.setPlayerRotation(getClientID(), angle); }
}
