package bman.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient implements UDPClientInterface, Runnable {

	private UDPEvent currentEvent;
	private boolean eventFetched = true;
	
	
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
	 */
	public UDPClient(String addr) {
		this.playerHash = this.hashCode();
		this.serverip = addr;
		try {
			clientSocket = new DatagramSocket(3457);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void establishConnection(String ip) {
		this.serverip = ip;
		sendEvent(
				new UDPEvent(
						UDPEvent.Type.establish_connection, 
						this.playerHash));
		System.out.println("Connection request sent. Player ID: " + this.playerHash);
	}

	@Override
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
							UDPClientInterface.port);
			this.clientSocket.send(sendPacket);

			System.out.println("Sent event of type: " + event.type + ". Hash code: " + event.hashCode());				
		} catch (Exception e) {
			System.err.println("Couldn't send event of type: " + event.type + ". Hash code: " + event.hashCode());
		}
	}

	@Override
	public void eventListener() {
		System.out.println("Client eventlistener startad.");
		while(true) {
			try {
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket); // The server locks here until it receives something.
				
				/* Deserializes stream */
				ByteArrayInputStream baosi = new ByteArrayInputStream(receivePacket.getData()); // Deserialize
				ObjectInputStream oosi = new ObjectInputStream(baosi);
				UDPEvent event = (UDPEvent) oosi.readObject();
				System.out.println(event.type + " recieved from " + event.getOriginID());
				
				/* Updates the clients current event */
				this.currentEvent = event;
				this.eventFetched = false;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * The run function.
	 */
	@Override
	public void run() {
		establishConnection(this.serverip);
		eventListener();
	}
	
	public boolean eventExists() {
		return !eventFetched;
	}
	
	public UDPEvent getEvent() {
		if (!eventFetched) {
			this.eventFetched = true;
			return this.currentEvent;
		}
		System.err.println("Skumt fel.");
		return null;
	}
}