package bman.networking;

public interface UDPClientInterface {
	
	/**
	 * The port of the server
	 */
	public static int port = 3456;
	
	/**
	 * Connects to a server
	 * @param ip The IP address to connect to.
	 */
	public void establishConnection(String ip);
	
	/**
	 * Sends an event.
	 */
	public void sendEvent(UDPEvent event);
	
	/**
	 * Listens for incoming packets and decodes it to events and reads them and acts accordingly
	 */
	public void eventListener();
	

}
