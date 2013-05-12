package bman.networking;

/**
 * The UDPClient is responsible for all communication from and to the client.
 * It listens for packets and decodes them and interpret them and act upon them.
 * @author Viktor Dahl
 *
 */
public interface UDPClientInterface extends Runnable {
	
	/**
	 * The port of the server
	 */
	public static int serverPort = 3456;
	
	/**
	 * The port of the client
	 */
	public static int clientPort = 3457;
	
	/**	
	 * Connects to the server
	 */
	public void establishConnection();
	
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
	 * Returns and removes the first event in the EventQueue
	 * @return The current event.
	 */
	public UDPEvent getEvent();
}