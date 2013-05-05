package bman;

import java.util.Scanner;

/**
 * The main application class. This is where the main function is and where
 * the different parts are being run from.
 * @author viktordahl
 *
 */
public class JBomberman {
	
	public static void connect() {
		System.out.print("Ange Ip: ");
		Scanner scan = new Scanner(System.in);
		String ip = scan.nextLine();
		System.out.println(ip);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("====BOMBERMAN====");
		System.out.println("1) Set playername");
		System.out.println("2) Join Game");
		System.out.println("3) Host Game");
		Scanner scan = new Scanner(System.in);
		int x = scan.nextInt();
		switch (x) {
		case 1: System.exit(0);
		case 2: connect();
		case 3: System.exit(0);
		}
		

	}

}
