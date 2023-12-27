package shared;

// an interface for determined classes that must realize
// one final wish before they succumb to an Exception
public interface LastWish {
	public void handleException(String message, Exception e);
}
