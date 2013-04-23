package bman.networking;

public interface UDPClientInterface {
	
	
	/**
	 * Scans the subnet for active servers
	 */
	public void listActiveServers();
	
	/**
	 * Connects to a server
	 * @params ip The IP address to connect to.
	 * @return True when connection is established.
	 */
	public boolean establishConnection(String ip);
	
	public boolean sendEvent(UDPEventInterface event);
	
	public void eventListener();
	
	public void testClient();
	
}
