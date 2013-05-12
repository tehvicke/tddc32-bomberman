package bman.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import bman.JBomberman;
import bman.backend.JGameMap;

/**
 * This class represents the server and the only thing it does is to
 * start an UDPServer. The reason it exists is mainly for design purpose, 
 * as it doesn’t serve any purpose but to start the UDPServer. Additional 
 * functionality can be added if wanted.
 * @author viktordahl
 */
public class UDPServer implements UDPServerInterface {

	// For testing purpose
	private int events_sent = 0;
	private int events_received = 0;
	private int broadcasts_sent = 0;
	
	/**
	 * The number of players currently alive.
	 */
	private int playersAlive;
	/**
	 * The number of clients to accept.
	 */
	private int numberOfClients;
	/**
	 * The number of clients currently connected.
	 */
	private int clientsConnected = 0;
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
	public void broadcastEvent(UDPEventInterface event) {
		if ((clients == null && clients[0] == null) || clientsConnected == 0) {
			System.err.println("Server: No clients are connected.");
			return;
		}
		for (Client cli : clients) {
			if (cli == null) {
				continue;
			}
			try {
				sendEvent(event, cli.hash);
				if (JBomberman.debug) {
					System.out.println("Server: Broadcast. " + event.getType() + " sent to " + cli.hash);
				}
			} catch (NullPointerException e) {
				System.err.println("Error sending " + event.getType() + " to " + cli);
			}
		}
		broadcasts_sent++;
	}

	@Override
	public void sendEvent(UDPEventInterface event, int client) {
		if (getClient(client) == null) {
			System.err.println("Server: Client " + client + " doesn't exist. Doesn't send event.");
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
		boolean broadcast = true;
		while(JBomberman.running) {
			try {
				broadcast = true;
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

				
				if (event.type == UDPEventInterface.Type.player_die) {
					playersAlive--;
					this.getClient(event.getOriginID()).setAlive(false);
					if (playersAlive == 1 && clients.length - playersAlive > 0) {
						for (Client cli : clients) {
							if (cli.isAlive()) {
								sendEvent(new UDPEvent(UDPEventInterface.Type.player_win, 0), cli.hash);
							}
						}
					}
				} else if (event.type == UDPEventInterface.Type.player_join) {
					playersAlive++;
				} else if (event.type == UDPEventInterface.Type.is_alive) {
					sendEvent(new UDPEvent(UDPEventInterface.Type.is_alive, 0), event.getOriginID());
					broadcast = false;
				} else if (event.type == UDPEventInterface.Type.establish_connection) {
					clients[clientsConnected++] = new Client(event.getOriginID(), receivePacket.getAddress());
					broadcast = false;
					System.out.println(clients);
					if (clientsConnected == numberOfClients) {
						broadcastEvent(new UDPEvent(UDPEventInterface.Type.game_start, 0));
						/* Sends the map layout */
						broadcastEvent(new UDPEvent(UDPEventInterface.Type.game_map, 0, randomizedMap(this.percentFilled)));
					}
				}
				/* Send the event to all clients if wanted. Default is true s*/
				if (broadcast) {
					broadcastEvent(event);
				}

				if (JBomberman.debug) {
					System.out.println("Server: Players alive: " + playersAlive);
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
		System.out.println("Server: Event listener started");		
		eventListener();
		
		
		if (JBomberman.debug) {
			System.err.println("UDPServer Thread exiting");
		}
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
	 * Private class that stores info about clients
	 * @author viktordahl
	 */
	private class Client {
		private InetAddress addr;
		private int hash;
		private boolean alive = true;

		/**
		 * Constructor for the client class.
		 * @param hash The client hash.
		 * @param addr The IP address of the client.
		 */
		public Client(int hash, InetAddress addr) {
			System.out.println("Server: Client Created. Hash: " + hash + " Addr: " + addr.getHostAddress());
			this.hash = hash;
			this.addr = addr;
		}
		
		/**
		 * 
		 * @return If the client is alive
		 */
		public boolean isAlive() {
			return this.alive;
		}
		
		/**
		 * 
		 * @param al Whether the client is alive or not
		 */
		public void setAlive(boolean al) {
			alive = al;
		}
	}
}