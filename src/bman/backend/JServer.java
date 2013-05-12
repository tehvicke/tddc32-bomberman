package bman.backend;

import bman.JBomberman;
import bman.networking.UDPServer;
import bman.networking.UDPServerInterface;

/**
 * This class represents the server and the only thing it does is to start an UDPServer.
 * The reason it exists is mainly for design purpose, as it doesn’t serve any purpose
 * but to start the UDPServer. Additional functionality can be added if wanted.
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
		if (JBomberman.debug) {
			System.out.println(this);
		}
		server = new UDPServer(numberOfPlayers);
	}

	/**
	 * Initializes the JServer and initiates the UDP Server.
	 * @param numberOfPlayers The number of players that should be in the game.
	 * @param percentFilled The percent of the map filled with destroyable blocks.
	 */
	public JServer(int numberOfPlayers, int percentFilled) {
		if (JBomberman.debug) {
			System.out.println(this);
		}
		server = new UDPServer(numberOfPlayers, percentFilled);
	}

	@Override
	public void run() {
		Thread serverThread = new Thread(server);
		serverThread.start(); /* Starts the server in a different thread */
		if (JBomberman.debug) {
			//Should Exit early
			System.err.println("Game Server Thread Exiting");
		}
	}
}
