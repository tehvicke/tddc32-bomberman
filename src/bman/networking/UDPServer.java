package bman.networking;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class UDPServer implements UDPServerInterface {
	
	private int numberOfClients = 1;
	private clientThread[] clientThreads;
//	DatagramSocket serverSocket;
	
	public UDPServer(int numberOfClients) {
//		this.numberOfClients = numberOfClients;
//		clientThreads = new clientThread[numberOfClients];
//		
//		try {
//			serverSocket = new DatagramSocket(Integer.parseInt(port));
//		} catch (Exception e) {
//			System.err.println("hej");
//		}
	}
	
	@Override
	public boolean waitForClients(int number) {
//		for (clientThread cli : clientThreads) {
//			try {
//				
//				byte[] receiveData = new byte[1024];
//		        byte[] sendData = new byte[1024];
//		        
//				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//	            serverSocket.receive(receivePacket);
//			} catch (Exception e) {
//				
//			}
//			
//			
//			cli = new clientThread();
//			
//			
//		}
		return false;
	}

	@Override
	public boolean broadcastEvent(UDPEventInterface event) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * This one should be in the Interface aswell....how?
	 * @param client
	 * @return 
	 */
	public boolean sendEvent(clientThread client) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void eventListener() {
		// TODO Auto-generated method stub

	}
	
	public void testServer() {
		try {
			DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(port));
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			int count = 0;
			while(true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
//	              String sentence = new String( receivePacket.getData());
//	              ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(recBytes));
//	              Message messageClass = (Message) iStream.readObject();
//	              iStream.close();
//	              
//	              event = receivePacket.getData();
				
				ByteArrayInputStream baosi = new ByteArrayInputStream(receivePacket.getData()); // Deserialize
				ObjectInputStream oosi = new ObjectInputStream(baosi);
				UDPEvent eventi = (UDPEvent) oosi.readObject();
				System.out.println(eventi.name + " recieved. Type: " + eventi.type);
	            
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
	
	private class clientThread implements Runnable {
		private String playerName;
		private String IPAddress;
		public boolean isActive;
		
		public clientThread() {
			isActive = true;
			
		}
		
		@Override
		public void run() {
			
		}
		
	}

}
