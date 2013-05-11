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
	
	/**
	 * Set to true for debug mode that prints lots of information.
	 * This is not the proper nor best way of having a debug mode but
	 * it has to do for now.
	 */
	public static boolean debug = false;

	public static void joinGame() {
		
		System.out.println("Please give server IP: ");
		Scanner vicks = new Scanner(System.in);
		String ipaddr = vicks.nextLine();

//		String ip = "192.168.0.101";
		String ip = "192.168.0.197";

		JClient client = new JClient(ipaddr);
		Thread clientThread = new Thread(client);
		clientThread.start();
	}

	public static void hostGame(int players) {
		
		System.out.println("Enter percentage of destroyable blocks (less than 40 % recommended: ");
		Scanner vicks = new Scanner(System.in);
		int percent = Integer.parseInt(vicks.nextLine());
		
		JServer server = new JServer(players, percent);
		JClient client = new JClient("localhost");
		Thread serverThread = new Thread(server);
		Thread clientThread = new Thread(client);
		serverThread.start();
		clientThread.start();
	}
	
	public static void main(String[] args) {
		System.out.println("=====BOMBERDUDE=====");
		System.out.println("Select an option");
		System.out.println("1) Join Game");
		System.out.println("2) Host Game");
		System.out.println("3) Exit Game");
		System.out.println("4) Play alone");
		Scanner scan = new Scanner(System.in);
		int x = Integer.parseInt(scan.nextLine());
		
		switch(x) {
		case 1:
			joinGame();
			break;
		case 2:
			hostGame(2);
			break;
		case 3:
			System.exit(0);
		case 4:
			hostGame(1);
			break;
		default:
			
		}
	}
}



