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

public class JGUIMainMenu extends JPanel implements ActionListener  {

	/**
	 * ID
	 */
	private static final long serialVersionUID = -4776987440183048444L;
	JLabel banan;
	private static Image logo = Toolkit.getDefaultToolkit().getImage("./sprites/logo.png");
	private int selected;
	private static String[] options = {"Join Game","Host Game","Exit Game"};
	private static String selectedLeft = "****";
	private static String selectedRight = "***";
	private JButton[] buttons;


	private static int xOffset = 50;
	private static int menuStartHeight = 250;
	private static int buttonSpacing = 50;
	/* KEY BINDING */


	JPanel joinPanel;
	JPanel hostPanel;
	JPanel menuPanel;


	/**
	 * Default constructor
	 */
	public JGUIMainMenu() {
		setSize(JGUIScreen.w_width, JGUIScreen.w_height);
		this.setVisible(true);
		this.setLayout(null);
		int topMargin = 250;

		//Init menuPanel
		menuPanel = new JPanel();
		menuPanel.setBounds(0,topMargin, JGUIScreen.w_width/3,JGUIScreen.w_height-topMargin);
		menuPanel.setBackground(Color.green);
		menuPanel.setLayout(new BoxLayout(menuPanel,BoxLayout.Y_AXIS));

		this.add(menuPanel);
		buttons = new JButton[options.length];
		for (int i  = 0; i < options.length; i++ ) {
			buttons[i] = new JButton(options[i]);

			menuPanel.add(buttons[i]);
		}
		menuPanel.isVisible();


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



}



