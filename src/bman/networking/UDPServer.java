package bman.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import bman.JBomberman;
import bman.backend.JGameMap;

/**
 * The UDP Server class. Handles everything that has to do with connection
 * and such.
 * @author viktordahl
 *
 */
public class UDPServer implements UDPServerInterface {

	// For testing purpose
	private int events_sent = 0;
	private int events_received = 0;
	private int broadcasts_sent = 0;
	private boolean testmode = false;

	/**
	 * Whether the listener shall be active or not. Default: Always active.
	 */
	private boolean listen = true;

	/**
	 * The number of clients to accept.
	 */
	private int numberOfClients;

	/**
	 * The percent of the map filled with destroyable blocks.
	 */
	private int percentFilled;

	/**
	 * The server socket.
	 */
	private DatagramSocket serverSocket;

	/**
	 * An array with all the clients.
	 */
	private Client[] clients;

	/**
	 * Constructor for the server.
	 * @param numberOfClients The number of clients the server shall wait for,
	 * including the server itself.
	 */
	public UDPServer(int numberOfClients) {
		System.out.println(this);
		this.numberOfClients = numberOfClients;
		this.clients = new Client[numberOfClients];
		try {
			serverSocket = new DatagramSocket(UDPServerInterface.port);
		} catch (Exception e) {
			System.err.println("Problem UDPServer constructor.");
		}
	}

	/**
	 * Constructor for the server with two arguments
	 * @param numberOfClients The number of clients the server shall wait for,
	 * including the server itself.
	 * @param percentFilled The percent of the map filled with destroyable blocks.
	 */
	public UDPServer(int numberOfClients, int percentFilled) {
		System.out.println(this);
		this.numberOfClients = numberOfClients;
		this.percentFilled = percentFilled;
		this.clients = new Client[numberOfClients];
		try {
			serverSocket = new DatagramSocket(UDPServerInterface.port);
		} catch (Exception e) {
			System.err.println("Problem UDPServer constructor.");
		}
	}

	@Override
	public void waitForClients() {
		int clientsConnected = 0;
		/* Loops until all clients are connected. */
		while(clientsConnected < this.numberOfClients) {
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			UDPEvent event;
			try {
				/* Waits for packet. The server locks here until a package is received. */
				serverSocket.receive(receivePacket);

				/* Deserializes string of bytes to an object */
				ByteArrayInputStream baosi = new ByteArrayInputStream(receivePacket.getData());
				ObjectInputStream oosi = new ObjectInputStream(baosi);
				baosi.close();
				oosi.close();
				event = (UDPEvent) oosi.readObject();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				continue;
			}

			/* Adds the client hash if the event type is correct */
			if (event.type == UDPEvent.Type.establish_connection) {
				clients[clientsConnected++] = new Client(event.getOriginID(), receivePacket.getAddress());
			} else {
				System.err.println("Wrong type of event. " + event.type);
			}
		}
	}

	@Override
	public void broadcastEvent(UDPEventInterface event) {
		if (clients == null && clients[0] == null) {
			System.err.println("Server: No clients are connected.");
			return;
		}
		for (Client cli : clients) {
			sendEvent(event, cli.hash);
			if (JBomberman.debug) {
				System.out.println("Server: " + event.getType() + " sent to " + cli.hash);
			}
		}
		broadcasts_sent++;
	}

	public void sendEvent(UDPEventInterface event, int client) {
		if (getClient(client) == null) {
			System.err.println("Server: Klient " + client + " finns inte. Skickar ej event.");
			return;
		}
		try {
			byte[] sendData = new byte[1024];

			/* Serialize event */
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(event);
			oos.flush();
			sendData = baos.toByteArray(); // Serializes

			DatagramPacket sendPacket = 
					new DatagramPacket(
							sendData, 
							sendData.length, 
							getClient(client).addr, 
							3457);  					   /* Sends to 3457 as is where the clients listens.
							 * This for allowing a server to be run on a 
							 * client computer.
							 */
			this.serverSocket.send(sendPacket);
			events_sent++;
		} catch (Exception e) {
			System.err.println("Couldn't send event of type: " + event.getType() + ". Hash code: " + event.hashCode());
		}
	}

	/**
	 * Private function for getting a client hash.
	 * @return The client object.
	 */
	private Client getClient(int hash) {
		for (Client cli : clients) {
			if (cli.hash == hash) {
				return cli;
			}
		}
		return null;
	}

	@Override
	public void eventListener() {
		while(true) {
			try {
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

				/* The server locks here until it receives something. */
				serverSocket.receive(receivePacket); 

				/* Here the stream of bytes are decoded into an event object. */
				ByteArrayInputStream baosi = new ByteArrayInputStream(receivePacket.getData());
				ObjectInputStream oosi = new ObjectInputStream(baosi);
				baosi.close(); /* May or may not be required to close the streams... */
				oosi.close();
				UDPEvent event = (UDPEvent) oosi.readObject();

				/* Send the event to all clients. It shall not send all events so some critera will be added */
				broadcastEvent(event);

				if (JBomberman.debug) {
					System.out.println("Server: Skickade: " + events_sent + " Mottagna: " + events_received++  + " BC: " + broadcasts_sent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Runs the server in a separate thread.
	 */
	@Override
	public void run() {
		System.out.println("Server: Server thread started.");
		/* Wait for all clients to join */
		waitForClients();
		System.out.println("Server: Game starts");		

		/* Broadcast start game event */
		broadcastEvent(new UDPEvent(UDPEventInterface.Type.game_start, 0));

		/* Sends the map layout */
		broadcastEvent(new UDPEvent(UDPEventInterface.Type.game_map, 0, randomizedMap(this.percentFilled)));

		/* Start the event listener */
		eventListener(); 
	}

	/**
	 * This randomizes where the destroyable blocks are in the game map.
	 * 
	 * @param percentFilled The amount of destroyable blocks in the map as percent.
	 * No more than 40 % recommended
	 * @return The map layout in a String array format
	 */
	public String[] randomizedMap(int percentFilled) {
		Random gen = new Random();
		String[] layout = new String[JGameMap.mapsize];
		for (int row = 0; row < JGameMap.mapsize; row++) {
			StringBuilder stringRow = new StringBuilder();
			for (int col = 0; col < JGameMap.mapsize; col++) {
				int number = gen.nextInt(100);
				if (
						(row == 0) || // top row solid blocks
						(col == 0) || // left row solid blocks
						(col == JGameMap.mapsize - 1) || // right row solid blocks
						(row == JGameMap.mapsize - 1) || // bottom row solid blocks
						(row % 2 == 0 && col % 2 == 0) // middle solid blocks
						) {
					stringRow.append("s");
				} else if (number < percentFilled) {
					stringRow.append("d");
				} else {
					stringRow.append(" ");
				}
			}
			layout[row] = stringRow.toString();
		}
		return layout;
	}

	/**
	 * Private class that stores hash and ip address
	 * @author viktordahl
	 */
	private class Client {
		private InetAddress addr;
		private int hash;

		/**
		 * Constructor for the client class.
		 * @param hash The client hash.
		 * @param addr The IP address of the client.
		 */
		public Client(int hash, InetAddress addr) {
			System.out.println("Server: Klient skapad. Hash: " + hash + " Addr: " + addr.getHostAddress());
			this.hash = hash;
			this.addr = addr;
		}
	}
}