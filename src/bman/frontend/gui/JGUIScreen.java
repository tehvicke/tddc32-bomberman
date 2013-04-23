package bman.frontend.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

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

	public JGUIScreen() {
		this.setSize(new Dimension(w_width+2,w_height+28));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		game = new JGUIGameMap();
		add(game);
		this.setVisible(true);
		setResizable(false);
	}


	public void setActive() {

	}
	
	public static void main(String args[]) {
		JGUIScreen test = new JGUIScreen();
	}

}



