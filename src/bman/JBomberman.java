package bman;

import java.util.Scanner;

import bman.backend.JClient;
import bman.backend.JServer;

/**
 * The main application class. This is where the main function is and where
 * the different parts are being run from.
 * @author viktordahl,Petter
 *
 */
public class JBomberman {
	
	public static boolean debug;

	public static void joinGame() {
	

		System.out.println("Please give server IP: ");


//		String ip = "192.168.0.101";
		String ip = "192.168.0.197";

		JClient client = new JClient(ip);
		Thread clientThread = new Thread(client);
		clientThread.start();
	}

	public static void hostGame(int players) {
		JServer server = new JServer(players); //fix
//		UDPServerInterface server = new UDPServer(2);
		
		JClient client = new JClient("127.0.0.1");
		Thread serverThread = new Thread(server);
		Thread clientThread = new Thread(client);
		serverThread.start();
		clientThread.start();
	}



	public static void main(String[] args) {
		System.out.println("=====BOMBERMAN=====");
		System.out.println("Select an option");
		System.out.println("1) Join Game");
		System.out.println("2) Host Game");
		System.out.println("3) Exit Game");
		System.out.println("4) Play alone (for testing)");
		Scanner scan = new Scanner(System.in);
		int x = Integer.parseInt(scan.nextLine());
		scan.close();
		
		switch(x) {
		case 1: joinGame(); break;
		case 2: hostGame(2); break;
		case 3: System.exit(0);
		case 4: hostGame(1); break;
		}


	}
}



