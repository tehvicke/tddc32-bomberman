package bman.frontend.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import bman.backend.JGameMap;
import bman.backend.JHuman;

public class JGUIScreen extends JFrame {

	private class exitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("TROLOLOLOLLOLOLOLO");
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
		setTitle("Bomberdude");
	}
	
	/**
	 * Adds content to the JGUIScreen
	 * @param JPanel to be added
	 */
	public void addContent(JPanel gm) {
		this.content = gm;
		this.add(gm);
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
	

	public void setActive() {

	}


}



