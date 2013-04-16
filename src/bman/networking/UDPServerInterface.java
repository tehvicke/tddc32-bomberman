package bman.networking;

public interface UDPServerInterface {
	public static String IPAddress = "127.0.0.1";
	public static String port = "3456";
	
	public boolean waitForClients(int number);
	
	public boolean broadcastEvent(UDPEventInterface event);
	
//	public boolean sendEvent(clientThread client);
	
	public void createEvent();
	
	public void eventListener();
	
	public void testServer();
	
}
