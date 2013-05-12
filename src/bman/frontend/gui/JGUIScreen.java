package bman.frontend.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bman.JBomberman;

public class JGUIScreen extends JFrame {

	private class winListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
			
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			
			
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			JBomberman.running = false;
			if (JBomberman.debug) {
			System.err.println("Window Closed Action");
			}
			
			System.exit(0);
			
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	/**
	 * Variables
	 */
	protected static int w_width = 450;
	protected static int w_height = 450;
	private static final long serialVersionUID = 7135568752644883047L;

	//JPanel which is shown in the UI
	JPanel content;
	/**
	 * Default constructor
	 */
	public JGUIScreen() {
		initialize();
	
	}
	/**
	 * Constructor which adds content at initialization
	 * @param content JPanel to be added to JGUIScreen
	 */
	public JGUIScreen(JPanel content) {
		initialize();
		this.content = content;
		this.add(content);
	}
	/**
	 * Initializes the JGUIScreen
	 */
	private void initialize() {
		this.setSize(new Dimension(w_width+2,w_height+28));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		setResizable(false);
		setTitle("BomberDude");
		this.addWindowListener(new winListener());
	}
	
	/**
	 * Adds a JPanel object to the JGUIScreen
	 * @param JPanel to be added
	 */
	public void addContent(JPanel gm) {
		this.content = gm;
		this.add(gm);
		this.validate();
		this.repaint();
	}
	
	/**
	 * Removes current content and displays a message
	 * @param message message to be displayed.
	 */
	public void displayMessage(String message) {
		this.removeContent();
		JPanel panel = new JPanel();
		JLabel info = new JLabel(message);
		info.setFont(new Font("Serif", Font.BOLD, 50));
		panel.add(info);
		panel.setVisible(true);
		panel.setBackground(Color.white);
		this.add(panel);
		validate();
		repaint();
	}

	/**
	 * Removes the current content in the JGUIScreen
	 */
	public void removeContent() {
		if (content != null) {
			this.remove(content);
		}
		this.content = null;
	}
	



}



