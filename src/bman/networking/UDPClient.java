package bman.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
			
//			UDPEvent;
			BufferedReader inFromUser =
					new BufferedReader(new InputStreamReader(System.in));
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName("localhost");
			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];
			String sentence = inFromUser.readLine();
			sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 3456);
			clientSocket.send(sendPacket);
			Thread.sleep(2000);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			String modifiedSentence = new String(receivePacket.getData());
			System.out.println("FROM SERVER:" + modifiedSentence);
			clientSocket.close();
		} catch (Exception e) {
			System.err.println("Couldn't connect.");
		}
	}
}
