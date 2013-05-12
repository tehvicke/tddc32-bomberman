package bman.frontend;

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

import bman.JBomberman;
import bman.backend.JClient;
import bman.backend.JServer;

/**
 * Handles the menu GUI and presents the user with choices for starting or joining game.
 * @author Viktor, Petter
 *
 */
public class JGUIMainMenu extends JPanel  {

	/**
	 * ID
	 */
	private static final long serialVersionUID = -4776987440183048444L;
	
	//BackGround Image and buttonlabels
	private static Image background = Toolkit.getDefaultToolkit().getImage("./sprites/background.png");
	private static String[] options = {"Join Game","Host Game","Exit Game"};
	
	//Panels and buttons which make up the JMainMenu
	private JPanel joinPanel;
	private JPanel hostPanel;
	private JPanel menuPanel;
	private JButton[] buttons;
	
	//References to client and server to be started by CreateListener and connectListener
	private JClient client;
	private JServer server;
	
	//Components for joinPanel & hostPanel
	private JTextField ipField;
	private JTextField fill;
	private JLabel waitMsg = new JLabel("Waiting for Connections..");
	
	//Reference to the JGUIScreen which holds the mainMenu
	private JGUIScreen parentFrame;
	
	/**
	 * Constructs a JGUIMainMenu object.
	 * @param parentFrame, JGUISCreen or other JFrame which contains the menu
	 */
	public JGUIMainMenu(JGUIScreen parentFrame) {
		//Init of Main Menu
		this.parentFrame=parentFrame;
		this.setBackground(Color.white);
		setSize(JGUIScreen.w_width, JGUIScreen.w_height);
		this.setVisible(true);
		this.setLayout(null);
		this.setOpaque(false);
		
		//Listener used by left side buttons
		buttonListener bl = new buttonListener();
				
		//Init of menuPanel
		int topMargin = 250;
		menuPanel = new JPanel();
		menuPanel.setBounds(0,topMargin, JGUIScreen.w_width/3,JGUIScreen.w_height-topMargin);
		menuPanel.setBackground(Color.white);
		menuPanel.setLayout(new BoxLayout(menuPanel,BoxLayout.Y_AXIS));
		
		buttons = new JButton[options.length];
		for (int i  = 0; i < options.length; i++ ) {
			buttons[i] = new JButton(options[i]);
			buttons[i].addActionListener(bl);
			menuPanel.add(buttons[i]);
		}

		//Join Game Panel
		joinPanel = new JPanel();
		joinPanel.setVisible(false);
		joinPanel.setBounds(JGUIScreen.w_width/3,topMargin, 2*JGUIScreen.w_width/3,JGUIScreen.w_height-topMargin);
		joinPanel.setBackground(Color.white);
		
		//Content of joinPanel
		JLabel ip = new JLabel("IP Address:");
		ipField = new JTextField("xxx.xxx.xxx.xxx");
	
		JButton connect = new JButton("Connect");
		connect.addActionListener(new connectListener());
		
		joinPanel.add(ip);
		joinPanel.add(ipField);
		joinPanel.add(connect);
		
		//Host Panel
		hostPanel = new JPanel();
		hostPanel.setVisible(false);
		hostPanel.setBounds(JGUIScreen.w_width/3,topMargin, 2*JGUIScreen.w_width/3,JGUIScreen.w_height-topMargin);
		hostPanel.setBackground(Color.white);
		
		//hostPanel content
		JLabel fillDesc = new JLabel("Map block fill % (<40 recommended):");
		fill = new JTextField("25");
		JButton start = new JButton("Start Server");
		start.addActionListener(new createListener());
		waitMsg.setForeground(Color.red);
		waitMsg.setVisible(false);
		
		hostPanel.add(fillDesc);
		hostPanel.add(fill);
		hostPanel.add(start);
		hostPanel.add(waitMsg);
		
		//Add to mainMenu
		this.add(menuPanel);
		this.add(joinPanel);
		this.add(hostPanel);
	}
	/**
	 * Paints the menu with background
	 */
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(background,0,0,this);
		super.paint(g);
		g.dispose();
	}

	/**
	 * Class which handles left side buttons (Join/Host/Exit)
	 * Depending on which button is pressed either changes which
	 * components should be visible or exits the game.
	 * @author petter
	 *
	 */
	private class buttonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if((JButton) e.getSource() == buttons[0]) {
				joinPanel.setVisible(!joinPanel.isVisible());
				hostPanel.setVisible(false);
			} else if ((JButton) e.getSource() == buttons[1]) {
				
				joinPanel.setVisible(false);
				hostPanel.setVisible(!hostPanel.isVisible());
			} else {
				JBomberman.running = false;
				System.exit(0);
			}
		}
	}
	/**
	 * Listener which handles game joining
	 * @author petter
	 *
	 */
	private class connectListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//Reads IP from UI component
			String ip = ipField.getText();
			// Create client
			client = new JClient(ip,parentFrame);
			Thread clientThread = new Thread(client);
			clientThread.start();
		}
	}
	
	/**
	 * Listener which creates a server when the button is pressed and also
	 * creates a client which connects to the local server.
	 * @author petter
	 *
	 */
	private class createListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//Reads input
			String fillSt = fill.getText();
    		int fillperc = Integer.parseInt(fillSt);
			server = new JServer(2, fillperc);
			client = new JClient("localhost",parentFrame);
			Thread serverThread = new Thread(server);
			Thread clientThread = new Thread(client);
			serverThread.start();
			clientThread.start();
			waitMsg.setVisible(true);
		}
	}
}



