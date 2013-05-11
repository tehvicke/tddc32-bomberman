package bman.backend;

import bman.networking.UDPServer;
import bman.networking.UDPServerInterface;

/**
 * This one is only starting the UDPServer. It doesn't server a purpose
 * other than design consistency.
 * @author viktordahl
 *
 */
public class JServer implements Runnable {
	
	/**
	 * The UDPServer that handles all communication between the clients.
	 */
	private UDPServerInterface server;
	
	/**
	 * Initializes the JServer and initiates the UDP Server.
	 * @param numberOfPlayers The number of players that should be in the game.
	 */
	public JServer(int numberOfPlayers) {
		System.out.println(this);
		server = new UDPServer(numberOfPlayers);
	}
	
	/**
	 * Initializes the JServer and initiates the UDP Server.
	 * @param numberOfPlayers The number of players that should be in the game.
	 * @param percentFilled The percent of the map filled with destroyable blocks.
	 */
	public JServer(int numberOfPlayers, int percentFilled) {
		System.out.println(this);
		server = new UDPServer(numberOfPlayers, percentFilled);
	}

	@Override
	public void run() {
		Thread serverThread = new Thread(server);
		serverThread.start(); /* Starts the server in a different thread */
		
	}
}
