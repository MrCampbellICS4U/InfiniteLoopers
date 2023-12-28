package shared;

// an interface for determined classes that must realize
// one final wish before they succumb to an Exception
public interface LastWish {
	public void handleException(String message, Exception e);

	// for some reason, java uses an exception to handle closing the connection :/
	// id is the id of the connection that closed
	public void handleDisconnection(int id, Exception e);
}
