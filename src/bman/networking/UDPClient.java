package bman.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import bman.networking.UDPEvent.Type;

public class UDPClient implements UDPClientInterface, Runnable {

	String serverip;
	int playerHash;
	int port = 3456;
	public DatagramSocket clientSocket;

	/**
	 * Constructor for the UDP Client. Creates a unique hash for the player and
	 * initializes it's DatagramSocket.
	 */
	public UDPClient(String addr) {
		this.playerHash = this.hashCode();
		try {
			clientSocket = new DatagramSocket(3457);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Skipped.
	 */
	@Override
	public void listActiveServers() {
		// TODO Auto-generated method stub
	}

	/**
	 * "Joins" the server. 
	 * @return True if connection was established.
	 */
	@Override
	public boolean establishConnection(String ip) {
		try {
			this.serverip = ip;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (sendEvent(new UDPEvent(UDPEvent.Type.establish_connection, this.playerHash))) {
			System.out.println("Anslutning etablerad. Hash: " + this.playerHash);
			return true;
		} else {
			System.err.println("Couldn't establish connection.");
			return false;
		}
	}

	/**
	 * Sends an event.
	 */
	@Override
	public boolean sendEvent(UDPEvent event) {
		try {
			byte[] sendData = new byte[1024];

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(event);
			oos.flush();
			
			sendData = baos.toByteArray(); // Serialize
			DatagramPacket sendPacket = 
					new DatagramPacket(sendData, sendData.length, InetAddress.getByName(this.serverip), this.port);
			this.clientSocket.send(sendPacket);
			
			System.out.println("Sent event of type: " + event.type + ". Hash code: " + event.hashCode());				
			return true;
		} catch (Exception e) {
			System.err.println("Couldn't send event of type: " + event.type + ". Hash code: " + event.hashCode());
		}
		return false;
	}

	/**
	 * Implement later.
	 */
	@Override
	public void eventListener() {
		System.out.println("Eventlistener startad.");
		int count = 0;
		while(true) {
			try {
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket); // The server locks here until it recieves something.
			UDPEvent event = decode(receivePacket);
			System.out.println(event.type + " recieved from " + event.player_id);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(count++);
		}

	}
	@Override
	public void testClient() {
		// TODO Auto-generated method stub
		
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
	@Override
	public void run() {
		establishConnection(this.serverip);
		eventListener();
	}

	
}