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

	/**
	 * Whether the listener shall be active or not. Default: Always active.
	 */
	private boolean listen = true;
	private JGameMap gmap;
	
	/**
	 * The number of clients to accept.
	 */
	private int numberOfClients;

	/**
	 * The server port.
	 */
	private int port = 3456;

	/**
	 * The server socket.
	 */
	public DatagramSocket serverSocket;

	/**
	 * An array with all the clients.
	 */
	public Client[] clients;



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
			serverSocket = new DatagramSocket(this.port);
		} catch (Exception e) {
			System.err.println("Problem konstruktor.");
		}
	}

	/**
	 * Waits for events of the type "establish_connection". Creates a Client-object
	 * that stores the client hash and ip address.
	 * @param The number of clients to wait for.
	 */
	@Override
	public void waitForClients(int number) {
		int clientsConnected = 0;
		/* Loops until all clients is connected. */
		while(clientsConnected < this.numberOfClients) {
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			UDPEvent event;
			try {
				serverSocket.receive(receivePacket);
				event = decode(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				continue;
			}

			/* Adds the client hash if the eventtype is correct */
			if (event.type == UDPEvent.Type.establish_connection) {
				clients[clientsConnected++] = new Client(event.player_id, receivePacket.getAddress());
				gmap.handleEvent(event);
			}
		}
	}

	/**
	 * Sends an event to all active clients, threaded.
	 */
	@Override
	public void broadcastEvent(UDPEvent event) {
		if (clients[0] == null) {
			System.err.println("No clients are connected.");
			return;
		}
		for (Client cli : clients) {
			if (true || event.player_id != cli.hash) { // Ta bort true om det inte ska skickas till origin.
				sendEvent(event, cli.hash);
				System.out.println(event.type + " " + cli.hash);
			}
		}
	}

	/**
	 * Sends an event to a specific client.
	 * @param event The event to send.
	 * @param client The client to send to.
	 */
	public void sendEvent(UDPEvent event, int client) {
		if (getClient(client) == null) {
			System.err.println("Klient " + client + " finns inte. Skickar ej event.");
			return;
		}
		try {
			byte[] sendData = new byte[1024];

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(event);
			oos.flush();
			
			sendData = baos.toByteArray(); // Serialize
			DatagramPacket sendPacket = 
					new DatagramPacket(sendData, sendData.length, getClient(client).addr, this.port + 1);
			this.serverSocket.send(sendPacket);
			
			System.out.println("Sent event of type: " + event.type + ". Hash code: " + event.hashCode());				
//			return true;
		} catch (Exception e) {
			System.err.println("Couldn't send event of type: " + event.type + ". Hash code: " + event.hashCode());
		}
		
		
	}

	/**
	 * Privat funktion fšr att fŒ ut klienten frŒn en hash.
	 * @return
	 */
	public Client getClient(int hash) {
		for (Client cli : clients) {
			if (cli.hash == hash) {
				return cli;
			}
		}
		return null;
	}

	/**
	 * Denna lyssnar efter events och loopar genom en if-sats fšr att veta vad som ska hŠnda.
	 * Denna ska kšras hela tiden nŠr gamet Šr startat.
	 */
	@Override
	public void eventListener() {
		int count = 0;
		while(listen) {
			try {
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket); // The server locks here until it recieves something.
//			UDPEvent event = decode(receivePacket);
			
			ByteArrayInputStream baosi = new ByteArrayInputStream(receivePacket.getData()); // Deserialize
			ObjectInputStream oosi = new ObjectInputStream(baosi);
			baosi.close();
			UDPEvent event = (UDPEvent) oosi.readObject();
			
			oosi.close();
			
			System.out.println(event.type + " recieved from " + event.player_id);
			gmap.handleEvent(event);
			broadcastEvent(event);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(count++);
		}
	}

	/**
	 * Testserver. Ska tas bort.
	 */
	public void testServer() {
		try {
			DatagramSocket serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[1024];
			while(true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				UDPEvent event = decode(receivePacket);
			}
		} catch (Exception e) {
			System.err.println("Couldn't create server.");
			System.exit(1);
		}
	}

	/**
	 * Privat funktion som dekodar en serialiserad byte-array och returnerar ett UDPEvent.
	 * @param receivePacket Mottaget paket.
	 * @return Det mottagna eventet.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private UDPEvent decode(DatagramPacket receivePacket) throws IOException, ClassNotFoundException {
		ByteArrayInputStream baosi = new ByteArrayInputStream(receivePacket.getData()); // Deserialize
		ObjectInputStream oosi = new ObjectInputStream(baosi);
		baosi.close();
		UDPEvent event = (UDPEvent) oosi.readObject();
		oosi.close();
		return event;
	}

	/**
	 * Runs the server in a separate thread.
	 */
	@Override
	public void run() {
		System.out.println("before");
		waitForClients(numberOfClients); /* Wait for all clients to join */
		System.out.println("after");
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
			System.out.println("Klient skapad. Hash: " + hash + " Addr: " + addr.getHostAddress());
			this.hash = hash;
			this.addr = addr;
		}
	}
}