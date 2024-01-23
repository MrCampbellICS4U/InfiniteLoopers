package packets;

import server.Server;

public class ClientPlayerRotationPacket extends PacketTo<Server> {
	private double angle;

	/**
	 * Constructs a new instance of the ClientPlayerRotationPacket class with the
	 * given angle.
	 *
	 * @param angle The angle of the player rotation.
	 */

	public ClientPlayerRotationPacket(double angle) {
		this.angle = angle;
	}

	/**
	 * Handles the player rotation by setting the rotation angle for the specified
	 * client ID.
	 *
	 * @param s The server instance to handle the rotation on.
	 */
	public void handle(Server s) {
		s.setPlayerRotation(getClientID(), angle);
	}
}
