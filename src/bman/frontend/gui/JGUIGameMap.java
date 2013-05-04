package bman.frontend.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import bman.backend.JHuman;
import bman.backend.JMapObject;

public class JGUIGameMap extends JPanel implements ActionListener {

	/**
	 * VARIABLES
	 */
	private static final long serialVersionUID = -5735185698246996895L;
	
	/******************************************************************************************************
	 * CONSTANTS
	 *****************************************************************************************************/
	 
	private static String player_front = "./sprites/white_front.png";
	private static String player_back = "./sprites/white_back.png";
	private static String player_left = "";
	private static String player_right = "";
	
	private static String p2_front = "";
	private static String p2_back= "";
	private static String p2_left = "";
	private static String p2_right = "";
	
	private static String solidBlock = "./sprites/solid_block.png"; 
	private static String destroyableBlock = "./sprites/destroyable_block.png";
	
	private static String bomb_fire = "";
	private static String bomb_nofire = "";
	
	private static String explosion = "";
	
	private static String superman = "./sprites/superman.png";
	
	/****************************************************************************************************/
	
	
	
	
	/**
	 * Contents
	 */
	JHuman player;
	JMapObject[][] gameMap;
	static int gridsize = 15;
	protected int gridXSize = JGUIScreen.w_width/gridsize;
	protected int gridYSize = JGUIScreen.w_height/gridsize;

	public JGUIGameMap() {
		setSize(JGUIScreen.w_width, JGUIScreen.w_height);
		setVisible(true);
		this.setBackground(Color.green);
		player = new JHuman(new JGUIMapObject(player_front,player_back,superman,player_front),50,50);
		addKeyListener(new KAdapter());
		setFocusable(true);
		setDoubleBuffered(true);
		Timer timer = new Timer(6, this);
		timer.start();
		gameMap = new JMapObject[15][15];
		//Test layout
		JGUIMapObject block = new JGUIMapObject(destroyableBlock); 
		for (int i = 0; i < 15; i++) {
			gameMap[0][i] = new JMapObject(block,i*30,0);
			gameMap[14][i]= new JMapObject(block,i*30,14*30);
		}
		for (int i = 1; i < 15; i++) {
			gameMap[i][0] = new JMapObject(block,0,i*30);
			gameMap[i][14] = new JMapObject(block,14*30,i*30);
		}
		gameMap[5][5] = new JMapObject(new JGUIMapObject(superman),5*30,5*30);
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
	
		for (int i = 0; i <= JGUIScreen.w_height; i +=30) {
			g2d.setColor(Color.black);
			g2d.drawLine(0, i, JGUIScreen.w_width,i );
		}
		for (int i = 0; i <= JGUIScreen.w_width; i +=30) {
			g2d.drawLine(i, 0,i,JGUIScreen.w_height);
		}
		
		g2d.drawImage(player.getImage(),player.getX(),player.getY(),this);
		for (int i = 0; i < gridsize; i++) {
			for (int j = 0; j < gridsize ; j++) {
				try {
				g2d.drawImage(gameMap[i][j].getImage(),gameMap[i][j].getX(),gameMap[i][j].getY(),this);
				} catch (Exception e) {
					
				}
			}
		}
		
		
		Toolkit.getDefaultToolkit().sync();
		g.dispose();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//player.move();
		repaint();

	}

	private class KAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			player.keypress(e);
		}

	}

}
