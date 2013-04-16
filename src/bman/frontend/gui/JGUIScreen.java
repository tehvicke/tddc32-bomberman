package bman.frontend.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
	protected static int w_width = 800;
	protected static int w_heigth = 600;
	private static final long serialVersionUID = 7135568752644883047L;
	
	/**
	 * Members
	 */
	JButton exitButton;

	public JGUIScreen() {
		this.setSize(new Dimension(w_width,w_heigth));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		getContentPane().add(panel, BorderLayout.WEST);

		exitButton = new JButton("Exit");
		panel.add(exitButton);
		exitButton.addActionListener(new exitListener());

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.CYAN);
		getContentPane().add(panel_1, BorderLayout.CENTER);
		this.setVisible(true);
	}


	public void setActive() {

	}

}



