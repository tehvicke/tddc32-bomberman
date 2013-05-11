package bman.frontend.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JGUIMainMenu extends JPanel implements ActionListener  {

	/**
	 * ID
	 */
	private static final long serialVersionUID = -4776987440183048444L;
	JLabel banan;
	private static Image logo = Toolkit.getDefaultToolkit().getImage("./sprites/logo.png");
	
	private static String[] options = {"Join Game","Host Game","Exit Game"};
	private JButton[] buttons;


	private static int xOffset = 50;
	private static int menuStartHeight = 250;
	private static int buttonSpacing = 50;
	/* KEY BINDING */


	private JPanel joinPanel;
	private JPanel hostPanel;
	private JPanel menuPanel;
	
	
	


	/**
	 * Default constructor
	 */
	public JGUIMainMenu() {
		setSize(JGUIScreen.w_width, JGUIScreen.w_height);
		this.setVisible(true);
		this.setLayout(null);
		int topMargin = 250;
		buttonListener bl = new buttonListener();

		//Init menuPanel
		menuPanel = new JPanel();
		menuPanel.setBounds(0,topMargin, JGUIScreen.w_width/3,JGUIScreen.w_height-topMargin);
		menuPanel.setBackground(Color.green);
		menuPanel.setLayout(new BoxLayout(menuPanel,BoxLayout.Y_AXIS));

		this.add(menuPanel);
		buttons = new JButton[options.length];
		for (int i  = 0; i < options.length; i++ ) {
			buttons[i] = new JButton(options[i]);
			buttons[i].addActionListener(bl);
			menuPanel.add(buttons[i]);
		}
		//Join Game Panel
		joinPanel = new JPanel();
		JTextField ipField = new JTextField("Server IP:");
		joinPanel.add(ipField);
		
	}
	
	
	


	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(logo, xOffset, 100, this);

	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();

	}
	
	private class buttonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if((JButton) e.getSource() == buttons[0]) {
				System.out.println("show pressed");
				joinPanel.setVisible(true);
				hostPanel.setVisible(false);
			} else if ((JButton) e.getSource() == buttons[1]) {
				System.out.println("host pressed");
				joinPanel.setVisible(false);
				hostPanel.setVisible(true);
			} else {
				//NEEDS TO BE DONE PROPERLY
				System.exit(0);
			}
			
		}
		
	}



}



