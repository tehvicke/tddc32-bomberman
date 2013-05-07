package bman.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import bman.backend.JGameMap;

/**
 * The UDP Server class. Handles everything that has to do with connection
 * and such.
 * @author viktordahl
 *
 */
public class UDPServer implements UDPServerInterface, Runnable {

	// Testing
	int events_sent = 0;
	int events_received = 0;
	int broadcasts_sent = 0;
	
	
	
	/**
	 * Whether the listener shall be active or not. Default: Always active.
	 */
	private boolean listen = true;
	
	/**
	 * The JGameMap object
	 */
	private JGameMap gmap;
	
	/**
	 * The number of clients to accept.
	 */
	private int numberOfClients;

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
	public UDPServer(int numberOfClients, JGameMap gmap) {
		this.numberOfClients = numberOfClients;
		this.gmap = gmap;
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
				/* Waits for packet */
				serverSocket.receive(receivePacket);
				
				/* Decodes string of bytes to an object */
				ByteArrayInputStream baosi = new ByteArrayInputStream(receivePacket.getData()); // Deserialize
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

			/* Adds the client hash if the eventtype is correct */
			if (event.type == UDPEvent.Type.establish_connection) {
				clients[clientsConnected++] = new Client(event.getOriginID(), receivePacket.getAddress());
//				gmap.handleEvent(event);
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
			if (true || event.getOriginID() != cli.hash) { // Ta bort true om det inte ska skickas till origin.
				sendEvent(event, cli.hash);
//				System.out.println("Server: " + event.getType() + " sent to " + cli.hash);
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
			
			sendData = baos.toByteArray(); // Serialize
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
//			System.out.println(
//					"Server: Sent event of type: " + 
//					event.getType() + 
//					". Hash code: " + event.hashCode() + 
//					". Origin: " + event.getOriginID());	
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
//		int count = 0;
		while(listen) {
			try {
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				
				/* The server locks here until it recieves something. */
				serverSocket.receive(receivePacket); 

				/* Here the stream of bytes are decoded into an event object. */
				ByteArrayInputStream baosi = new ByteArrayInputStream(receivePacket.getData());
				ObjectInputStream oosi = new ObjectInputStream(baosi);
				baosi.close(); /* May or may not be required to close the streams... */
				oosi.close();
				UDPEvent event = (UDPEvent) oosi.readObject();
//				System.out.println("Server: " + event.getType() + " recieved from " + event.getOriginID());

				/* Sends the event to the game map to update it and make calculations etc. */
//				gmap.handleEvent(event);

				/* Send the event to all clients. It shall not send all events so some critera will be added */
				broadcastEvent(event);
				System.out.println("Server: Skickade: " + events_sent + " Mottagna: " + events_received++  + " BC: " + broadcasts_sent);
//				System.out.println("Server: " + count++);
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
		System.out.println("Server: Thread started.");
		waitForClients(); /* Wait for all clients to join */
		System.out.println("Server: Game starts");
		
		/* Broadcast start game event */
		broadcastEvent(new UDPEvent(UDPEventInterface.Type.game_start, 0));
		
		
		String[] mapLayout = {
				"sssssssssssssss",
				"s             d",
				"s             s",
				"s             s",
		        "s             s",
		        "s             s",
		        "s             s",
		        "s             s",
		        "s ddd    d d  s",
		        "s d d    d d  s",
		        "s d d    ddd  s",
		        "s             s",
		        "s             s",
		        "s             s",
		        "sssssssssssssss"
			};
		
		broadcastEvent(new UDPEvent(UDPEventInterface.Type.game_map, 0, mapLayout));
		
		eventListener(); /* Start the event listener */
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