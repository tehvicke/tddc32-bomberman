package bman.networking;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient implements UDPClientInterface {
	
	/**
	 * Implement later.
	 */
	@Override
	public void listActiveServers() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean establishConnection(String ip) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Implement later.
	 */
	@Override
	public boolean sendEvent(UDPEventInterface event) {
		// TODO Auto-generated method stub
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
		try {
			
			while(true) {
				BufferedReader inFromUser =
						new BufferedReader(new InputStreamReader(System.in));
				DatagramSocket clientSocket = new DatagramSocket();
				InetAddress IPAddress = InetAddress.getByName("localhost");
				byte[] sendData = new byte[1024];
				byte[] receiveData = new byte[1024];
				System.out.println("Enter amount of datagrams to send: ");
				int len = Integer.parseInt(inFromUser.readLine());
				UDPEvent event;
				switch(len) {
					case (1): {
						event = new UDPEvent(UDPEvent.Type.player_move_right);
						System.out.println("Tjoho!");
						break;
					} default: {
						event = new UDPEvent(UDPEvent.Type.player_move_left, new String[] {"hej"});
					}
				}
						
				// Send things
//				for (int i = 0; i < len; i++) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(baos);
					
					oos.writeObject(event);
					oos.flush();
					sendData = baos.toByteArray();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 3456);
					clientSocket.send(sendPacket);
//				}
				System.out.println("Sent " + len + " datagrams.");				
				
				// Receivethings
//				Thread.sleep(2000);
//				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//				clientSocket.receive(receivePacket);
//				ByteArrayInputStream baosi = new ByteArrayInputStream(receivePacket.getData()); // Deserialize
//			    ObjectInputStream oosi = new ObjectInputStream(baosi);
//			    UDPEvent eventi = (UDPEvent) oosi.readObject();
//				System.out.println(eventi.name);
//				String modifiedSentence = new String(receivePacket.getData());
//				System.out.println("FROM SERVER:" + modifiedSentence);
				
				clientSocket.close();
			}
		} catch (Exception e) {
			System.err.println("Couldn't connect.");
		}
	}
}
