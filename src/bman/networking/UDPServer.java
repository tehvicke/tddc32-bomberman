package bman.networking;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * The UDP Server class. Handles everything that has to do with connection
 * and such.
 * @author viktordahl
 *
 */
public class UDPServer implements UDPServerInterface, Runnable {

	/**
	 * The number of clients to accept.
	 */
	private int numberOfClients = 2;

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
	 * Private class that stores hash and ip address
	 * @author viktordahl
	 *
	 */
	private class Client {
		public InetAddress addr;
		public int hash;
		public Client(int hash, InetAddress addr) {
			System.out.println("Klient skapad. Hash: " + hash + " Addr: " + addr.getHostAddress());
			this.hash = hash;
			this.addr = addr;
		}
	}

	/**
	 * Constructor for the server.
	 * @param numberOfClients The number of clients the server shall wait for,
	 * including the server itself.
	 */
	public UDPServer(int numberOfClients) {
		this.numberOfClients = numberOfClients;
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
			}
		}
	}

	/**
	 * Sends an event to all active clients, threaded.
	 */
	@Override
	public void broadcastEvent(UDPEvent event) {
		for (Client cli : clients) {
			if (true || event.player_id != cli.hash) { // Ta bort true om det inte ska skickas till origin.
				sendEvent(event, cli.hash);
			}
		}
	}

	/**
	 * Sends an event to a specific client.
	 * @param event The event to send.
	 * @param client The client to send to.
	 */
	public void sendEvent(UDPEvent event, int client) {
		(new Thread (new EventSender(event, getClient(client)))).start();
		
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
		// TODO Auto-generated method stub
	}

	/**
	 * Testserver. Ska tas bort.
	 */
	public void testServer() {
		try {
			DatagramSocket serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[1024];
			//			byte[] sendData = new byte[1024];
			//			int count = 0;
			while(true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				UDPEvent event = decode(receivePacket);

				System.out.println(event.name + " recieved. Type: " + event.type);

				//	              InetAddress IPAddress = receivePacket.getAddress();
				//	              int port = receivePacket.getPort();
				////	              String capitalizedSentence = sentence.toUpperCase();
				////	              sendData = capitalizedSentence.getBytes();
				//	              sendData = receivePacket.getData();
				//	              DatagramPacket sendPacket =
				//	              new DatagramPacket(sendData, sendData.length, IPAddress, port);
				//	              serverSocket.send(sendPacket);
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
		return (UDPEvent) oosi.readObject();
	}


	/**
	 * A private class necessary to pass variables to a new thread.
	 * @author viktordahl
	 *
	 */
	private class EventSender implements Runnable {
		private UDPEvent event;
		private Client client;
		
		/**
		 * Constructor for EventSender
		 * @param event Event to be sent.
		 * @param client Client to send to.
		 */
		public EventSender(UDPEvent event, Client client) {
			this.event = event;
			this.client = client;
		}
		
		/**
		 * Sends events to a client.
		 * @param event Event to send
		 * @param addr IP to the receiver.
		 */
		private void sendEvent(UDPEvent event, InetAddress addr) {
			byte[] sendData = new byte[1024];
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, port);
			try {
				serverSocket.send(sendPacket);
				System.out.println("Event skickat. " + event.type + " Address: " + addr);
			} catch (IOException e) {
				System.err.println("Fel vid sŠndning av " + event.type + " " + event.hashCode());
			}
		}
		
		@Override
		public void run() {
			this.sendEvent(event, client.addr);
		}
	}

	/**
	 * Runs the server in a separate thread.
	 */
	@Override
	public void run() {
		waitForClients(numberOfClients);
		eventListener();
	}
}