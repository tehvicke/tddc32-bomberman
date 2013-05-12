package bman;

import bman.frontend.gui.JGUIMainMenu;
import bman.frontend.gui.JGUIScreen;

/**
 * The main application class. This is where the main function is and where
 * the main menu are being run from.
 * @author viktordahl,Petter
 *
 */
public class JBomberman {
	

	
	/**
	 * Set to true for debug mode that prints lots of information.
	 * This is not the proper nor best way of having a debug mode but
	 * it has to do for now.
	 */
	public static boolean debug = true;
	
	/* Tells Threads to shut down */
	public static volatile boolean running = true;

	public static void main(String[] args) {
		JGUIScreen scr = new JGUIScreen();
		scr.addContent(new JGUIMainMenu(scr));
	}

}



