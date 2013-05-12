package bman.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import bman.JBomberman;

/**
 * Responsible for the connection to the server. Handles events in a queue
 * so that all events will be taken care of. Has functions regarding the connection
 * such as to establish connection with the server, send and wait for events.
 * @author Viktor Dahl
 *
 */
public class UDPClient implements UDPClientInterface, Runnable {

	// Testing
	int events_sent = 0;
	int events_received = 0;
	
	/**
	 * The event queue. If multiple events exist that hasn't been executed they,
	 * they are stored here. FIFO.
	 */
	private ArrayList<UDPEvent> eventQueue;
	
	/**
	 * The server IP
	 */
	private String serverip;

	/**
	 * The unique player identifier hash
	 */
	private int playerHash;

	/**
	 * The client socket
	 */
	private DatagramSocket clientSocket;

	/**
	 * Constructor for the UDP Client. Creates a unique hash for the player and
	 * initializes it's DatagramSocket.
	 * @param addr The IP address of the server
	 */
	public UDPClient(String addr) {
		this.playerHash = this.hashCode();
		this.serverip = addr;
		this.eventQueue = new ArrayList<UDPEvent>();
		try {
			clientSocket = new DatagramSocket(UDPClientInterface.clientPort);
		} catch (SocketException e) {
			System.err.println("Could not open a connection on port " + UDPClientInterface.clientPort + ". Exits...");
			System.exit(0);
		}
	}

	@Override
	public void establishConnection() {
		sendEvent(
				new UDPEvent(
						UDPEvent.Type.establish_connection, 
						this.playerHash));
		
		if (JBomberman.debug) {
			System.out.println("Klient: Connection request sent. Player ID: " + this.playerHash);
		}
	}

	public void sendEvent(UDPEvent event) {
		try {
			byte[] sendData = new byte[1024];

			/* Serializes object */
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(event);
			oos.flush();
			sendData = baos.toByteArray(); // Serialize
			
			/* Send packet */
			DatagramPacket sendPacket = 
					new DatagramPacket(
							sendData, 
							sendData.length, 
							InetAddress.getByName(this.serverip), 
							UDPClientInterface.serverPort);
			this.clientSocket.send(sendPacket);

			if (JBomberman.debug) {
				System.out.println("Klient: Sent event of type: " + event.type + ". Hash code: " + event.hashCode());
				events_sent++;
			}
			
		} catch (Exception e) {
			System.err.println("Klient: Couldn't send event of type: " + event.type + ". Hash code: " + event.hashCode());
		}
	}

	@Override
	public void eventListener() {
		if (JBomberman.debug) {
			System.out.println("Klient: Client eventlistener startad.");
		}
		
		try {
			clientSocket.setSoTimeout(1000);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean isAlive = true;
		
		while(JBomberman.running) {
			try {
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket); // The server locks here until it receives something.		
								
				/* Deserializes stream */
				ByteArrayInputStream baosi = new ByteArrayInputStream(receivePacket.getData()); // Deserialize
				ObjectInputStream oosi = new ObjectInputStream(baosi);
				UDPEvent event = (UDPEvent) oosi.readObject();
				if (JBomberman.debug) {
					System.out.println("Klient: " + event.type + " recieved from " + event.getOriginID());
				}
				if (event.type == UDPEventInterface.Type.is_alive) {
					isAlive = true;
				}
				this.eventQueue.add(event);
				
				if (JBomberman.debug) {
				System.out.println("Klient: Skickade: " + events_sent + " Mottaget: " + events_received++ + " EventQueue: " + eventQueue.size());
				}
			} catch (SocketTimeoutException e) {
				if (!isAlive) {
					System.out.println("Server svarar ej!!");
					break;
				}
				this.sendEvent(new UDPEvent(UDPEventInterface.Type.is_alive, this.playerHash));
				isAlive = false;
			}
			catch (Exception e) {
				System.err.println("LOLOLOLOLOL");
				e.printStackTrace();
			}
		}
		System.out.println("HOPLLA HOPPLSA");
	}
	
	@Override
	public void run() {
		establishConnection();
		eventListener();
		if (JBomberman.debug) {
			System.err.println("UDPClient Thread Exiting");
		}
	}
	
	@Override
	public boolean eventExists() {
		return !eventQueue.isEmpty();
	}
	
	@Override
	public UDPEvent getEvent() {
		if (!eventQueue.isEmpty()) {
			if (JBomberman.debug) {
				System.err.println("Removed " + this.eventQueue.get(0).toString() + " from the EventQueue");
			}
			return this.eventQueue.remove(0);
		}
		return null;
	}
}