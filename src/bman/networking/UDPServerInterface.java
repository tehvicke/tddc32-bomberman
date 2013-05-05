package bman.networking;

public interface UDPServerInterface {
	public static String IPAddress = "127.0.0.1";
	public static String port = "3456";
	
	public void waitForClients(int number);
	
	public void broadcastEvent(UDPEvent event);
	
	public void sendEvent(UDPEvent event, int client);
	
	public void eventListener();
	
	public void testServer();
	
}
