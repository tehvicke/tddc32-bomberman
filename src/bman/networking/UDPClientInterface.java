package bman.networking;

/**
 * The UDPClient is responsible for all communication from and to the client.
 * It listens for packets and decodes them and interpret them and act upon them.
 * @author viktordahl
 *
 */
public interface UDPClientInterface {
	
	/**
	 * The port of the server
	 */
	public static int serverPort = 3456;
	
	/**
	 * The port of the client
	 */
	public static int clientPort = 3457;
	
	/**
	 * Connects to a server
	 * @param ip The IP address to connect to.
	 */
	public void establishConnection(String ip);
	
	/**
	 * Sends an event to the server.
	 */
	public void sendEvent(UDPEvent event);
	
	/**
	 * Listens for incoming packets and decodes it to events and reads them and acts accordingly
	 */
	public void eventListener();
	
	/**
	 * 
	 * @return True if a new event exists.
	 */
	public boolean eventExists();
	
	/**
	 * 
	 * @return The current event.
	 */
	public UDPEvent getEvent();
}