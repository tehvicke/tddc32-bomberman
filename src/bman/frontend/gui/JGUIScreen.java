package bman.frontend.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import bman.backend.JGameMap;
import bman.backend.JHuman;

public class JGUIScreen extends JFrame {

	private class exitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
			
		}
		
	}
	private boolean isActive = false;
	/**
	 * Variables
	 */
	protected static int w_width = 450;
	protected static int w_height = 450;
	private static final long serialVersionUID = 7135568752644883047L;
	
	/**
	 * Members
	 */
	JGUIGameMap game;

	public JGUIScreen(JGameMap gmap, JHuman player) {
		this.setSize(new Dimension(w_width+2,w_height+28));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		setResizable(false);
		setTitle("Bomberman");
		//JGUISplashScreen splash = new JGUISplashScreen();
		//add(splash);
		//repaint();
		//remove(splash);
		
		game = new JGUIGameMap(gmap,player);
		add(game);
	
		
	}


	public void setActive() {

	}
	

}



