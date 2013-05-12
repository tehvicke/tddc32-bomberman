package bman;

import javax.swing.SwingUtilities;

import bman.frontend.JGUIMainMenu;
import bman.frontend.JGUIScreen;

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
	public static boolean debug = false;

	/**
	 * Running indicator for threads
	 */
	public static volatile boolean running = true;

	/**
	 * The main screen component.
	 */
	private static JGUIScreen scr;

	public static void main(String[] args) {
		 SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() 
	            {
	            	scr = new JGUIScreen();
	        		scr.addContent(new JGUIMainMenu(scr));
	            }
	        });	
	}
}



