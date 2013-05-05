package bman.networking;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import bman.networking.UDPEvent.Type;

public class UDPClient implements UDPClientInterface {

	InetAddress serverip;
	int playerHash;
	int port = 3456;
	DatagramSocket clientSocket;

	/**
	 * Constructor for the UDP Client. Creates a unique hash for the player and
	 * initializes it's DatagramSocket.
	 */
	public UDPClient() {
		this.playerHash = this.hashCode();
		try {
			clientSocket = new DatagramSocket();
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
			this.serverip = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
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
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.serverip, this.port);
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
		// TODO Auto-generated method stub

	}
	@Override
	public void testClient() {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void testClient() {
//		try {
//			while(true) {
//				BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); // Stdin
//				DatagramSocket clientSocket = new DatagramSocket(); // Skapa datagram
//				InetAddress IPAddress = InetAddress.getByName("localhost");
//				byte[] sendData = new byte[1024];
//				byte[] receiveData = new byte[1024];
//				System.out.println("Enter amount of datagrams to send: ");
//				int len = Integer.parseInt(inFromUser.readLine());
//				UDPEvent event;
//				switch(len) {
//				case (1): {
////					event = new UDPEvent(UDPEvent.Type.player_move_right);
//					System.out.println("Tjoho!");
//					break;
//				} default: {
//					event = new UDPEvent(UDPEvent.Type.player_move_left, new String[] {"hej"});
//				}
//				}
//
//				// Send things
//				//				for (int i = 0; i < len; i++) {
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				ObjectOutputStream oos = new ObjectOutputStream(baos);
//
//				oos.writeObject(event);
//				oos.flush();
//				sendData = baos.toByteArray();
//				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 3456);
//				clientSocket.send(sendPacket);
//				//				}
//				System.out.println("Sent " + len + " datagrams.");				
//
//				// Receivethings
//				//				Thread.sleep(2000);
//				//				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//				//				clientSocket.receive(receivePacket);
//				//				ByteArrayInputStream baosi = new ByteArrayInputStream(receivePacket.getData()); // Deserialize
//				//			    ObjectInputStream oosi = new ObjectInputStream(baosi);
//				//			    UDPEvent eventi = (UDPEvent) oosi.readObject();
//				//				System.out.println(eventi.name);
//				//				String modifiedSentence = new String(receivePacket.getData());
//				//				System.out.println("FROM SERVER:" + modifiedSentence);
//
//				clientSocket.close();
//			}
//		} catch (Exception e) {
//			System.err.println("Couldn't connect.");
//		}
//	}
	
}