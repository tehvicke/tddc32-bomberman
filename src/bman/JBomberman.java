package bman;

import java.util.Scanner;

import bman.networking.UDPClient;
import bman.networking.UDPEvent;
import bman.networking.UDPEvent.Type;

/**
 * The main application class. This is where the main function is and where
 * the different parts are being run from.
 * @author viktordahl
 *
 */
public class JBomberman {

	//	public static void connect() {
	//		System.out.print("Ange Ip: ");
	//		Scanner scan = new Scanner(System.in);
	//		//String ip = scan.nextLine();
	//		String ip = "192.168.2.11";
	//		UDPClient klient = new UDPClient();
	//		klient.establishConnection(ip);
	//
	//
	//		System.out.println("Välj");
	//		System.out.println("1) Gå upp");
	//		System.out.println("2) Gå ned");
	//		System.out.println("3) Gå vänster");
	//		System.out.println("4) gå höger");
	//		System.out.println("5) stäng av skiten");
	//		while (true) {
	//		    String a = scan.nextLine();
	//		    int x = Integer.parseInt(a);
	//			switch(x) {
	//			case 1: klient.sendEvent(new UDPEvent(Type.player_move_up,klient.playerHash));
	//			case 2: klient.sendEvent(new UDPEvent(Type.player_move_down,klient.playerHash));
	//			case 3: klient.sendEvent(new UDPEvent(Type.player_move_left,klient.playerHash));
	//			case 4: klient.sendEvent(new UDPEvent(Type.player_move_right,klient.playerHash));
	//			case 5: System.exit(0);
	//
	//			}
	//		}
	//
	//	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("====BOMBERMAN====");
		//		System.out.println("1) Set playername");
		//		System.out.println("2) Join Game");
		//		System.out.println("3) Host Game");
		//		Scanner scan = new Scanner(System.in);
		//		int x = scan.nextInt();
		//		scan.close();
		//		switch (x) {
		//		case 1: System.exit(0);
		//		case 2: connect();
		//		case 3: System.exit(0);
		//		}
		System.out.print("Ange Ip: ");
		Scanner scan = new Scanner(System.in);
		//String ip = scan.nextLine();
		String ip = "192.168.2.11";
		UDPClient klient = new UDPClient();
		klient.establishConnection(ip);


		System.out.println("Välj");
		System.out.println("1) Gå upp");
		System.out.println("2) Gå ned");
		System.out.println("3) Gå vänster");
		System.out.println("4) gå höger");
		System.out.println("5) stäng av skiten");

//		for (int i = 0; i < 2000; i++) {
//			klient.sendEvent(new UDPEvent(Type.player_move_down, klient.playerHash));
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		while (true) {
			int x = scan.nextInt();
			scan.nextLine();
			switch(x) {
			case 1: klient.sendEvent(new UDPEvent(Type.player_move_up,klient.playerHash));
			break;
			case 2: klient.sendEvent(new UDPEvent(Type.player_move_down,klient.playerHash));
			break;
			case 3: klient.sendEvent(new UDPEvent(Type.player_move_left,klient.playerHash));
			break;
			case 4: klient.sendEvent(new UDPEvent(Type.player_move_right,klient.playerHash));
			break;
			case 5: System.exit(0);
			}

		}



	}
}
