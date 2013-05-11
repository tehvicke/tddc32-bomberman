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

import bman.backend.JClient;
import bman.backend.JServer;

public class JGUIMainMenu extends JPanel  {

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
	
	private JClient client;
	private JServer server;
	
	private JTextField ipField;
	private JTextField fill;
	
	private JGUIScreen parentFrame;
	


	/**
	 * Default constructor
	 */
	public JGUIMainMenu(JGUIScreen parentFrame) {
		this.parentFrame=parentFrame;
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
		joinPanel.setVisible(false);
		joinPanel.setBounds(JGUIScreen.w_width/3,topMargin, 2*JGUIScreen.w_width/3,JGUIScreen.w_height-topMargin);
		joinPanel.setBackground(Color.red);
		
		//Content of joinPanel
		JLabel ip = new JLabel("IP Address:");
		ipField = new JTextField("xxx.xxx.xxx.xxx");
		JLabel errorMSG = new JLabel();
		errorMSG.setVisible(false);
		JButton connect = new JButton("Connect");
		connect.addActionListener(new connectListener());
		joinPanel.add(errorMSG);
		joinPanel.add(ip);
		joinPanel.add(ipField);
		joinPanel.add(connect);
		this.add(joinPanel);
		
		//Host Panel
		hostPanel = new JPanel();
		hostPanel.setVisible(false);
		hostPanel.setBounds(JGUIScreen.w_width/3,topMargin, 2*JGUIScreen.w_width/3,JGUIScreen.w_height-topMargin);
		
		//hostPanel content
		JLabel fillDesc = new JLabel("Map block fill % (<40 recommended):");
		fill = new JTextField("25");
		JButton start = new JButton("Start Server");
		start.addActionListener(new createListener());
		
		//Adding
		hostPanel.add(fillDesc);
		hostPanel.add(fill);
		hostPanel.add(start);
		this.add(hostPanel);
		
	}
	
	
	


	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(logo, xOffset, 100, this);

	}

	/**
	 * Class which handles left side buttons (Join/Host/Exit)
	 * @author petter
	 *
	 */
	private class buttonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if((JButton) e.getSource() == buttons[0]) {
				
				joinPanel.setVisible(true);
				hostPanel.setVisible(false);
			} else if ((JButton) e.getSource() == buttons[1]) {
				
				joinPanel.setVisible(false);
				hostPanel.setVisible(true);
			} else {
				//NEEDS TO BE DONE PROPERLY
				System.exit(0);
			}
			
		}
		
	}
	/**
	 * Listener responsible for connecting
	 * @author petter
	 *
	 */
	private class connectListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String ip = ipField.getText();
			
			//IP CHECKING HERE
			
			// Create client
			client = new JClient(ip,parentFrame);
			Thread clientThread = new Thread(client);
			clientThread.start();
		}
		
	}
	private class createListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String fillSt = fill.getText();
    		int fillperc = Integer.parseInt(fillSt);
			server = new JServer(2, fillperc);
			client = new JClient("localhost",parentFrame);
			Thread serverThread = new Thread(server);
			Thread clientThread = new Thread(client);
			serverThread.start();
			clientThread.start();
			
		}
		
	}




}



