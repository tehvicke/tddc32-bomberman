package bman.networking;

public interface UDPServerInterface extends Runnable {
	
	/**
	 * The server port.
	 */
	public static int port = 3456;
	
	/**
	 * Waits for events of the type "establish_connection". Creates a Client-object
	 * that stores the client hash and ip address.
	 */
	public void waitForClients();
	
	/**
	 * Sends an event to all active clients.
	 */
	public void broadcastEvent(UDPEventInterface event);
	
	/**
	 * Sends an event to a specific client.
	 * @param event The event to send.
	 * @param client The client to send to.
	 */
	public void sendEvent(UDPEventInterface event, int client);
	
	/**
	 * Listens for events being sent and deals with them according to some criteria.
	 */
	public void eventListener();
	
	
}
