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

import bman.backend.JGameMap;
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

	public static final String player_front = "./sprites/white_front.png";
	public static final String player_back = "./sprites/white_back.png";
	public static final String player_left = "";
	public static final String player_right = "";
    
	public static final String p2_front = "";
	public static final String p2_back= "";
	public static final String p2_left = "";
	public static final String p2_right = "";
    
	public static final String solidBlock = "./sprites/solid_block.png"; 
	public static final String destroyableBlock = "./sprites/destroyable_block.png";

	public static final String bomb_fire = "./sprites/solid_block.png";
	public static final String bomb_nofire = "./sprites/solid_block.png";;
    
	public static final String explosion = "";
    
	public static final String superman = "./sprites/superman.png";

	/****************************************************************************************************/
	public static JGUIMapObject solidBlockGUI = new JGUIMapObject(solidBlock);
	public static JGUIMapObject destroyableBlockGUI = new JGUIMapObject(destroyableBlock);



	/**
	 * Contents
	 */
	JGameMap gameMap;
	JHuman player;


	public JGUIGameMap() {
		//Window properties
		setSize(JGUIScreen.w_width, JGUIScreen.w_height);
		setVisible(true);
		this.setBackground(Color.green);
		addKeyListener(new KAdapter());
		setFocusable(true);
		setDoubleBuffered(true);
		//Timer which triggers actionlistener in this class
		Timer timer = new Timer(6, this);
		timer.start();

		

		gameMap = new JGameMap();
		player = new JHuman(new JGUIMapObject(player_front,player_back,superman,player_front),gameMap);
		gameMap.addObject(player, 3,3);
		//Test layout

		//		for (int i = 1; i < 15; i++) {
		//			gameMap[i][0] = new JMapObject(block,0,i*30);
		//			gameMap[i][14] = new JMapObject(block,14*30,i*30);
		//		}
		//		gameMap[5][5] = new JMapObject(new JGUIMapObject(superman),5*30,5*30);
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;

		/* DRAWS THE GRID */
		for (int i = 0; i <= JGUIScreen.w_height; i +=30) {
			g2d.setColor(Color.black);
			g2d.drawLine(0, i, JGUIScreen.w_width,i );
		}
		for (int i = 0; i <= JGUIScreen.w_width; i +=30) {
			g2d.drawLine(i, 0,i,JGUIScreen.w_height);
		}

		/*Draws the objects on the map */
		//g2d.drawImage(player.getImage(),player.getX(),player.getY(),this);
		for (int i = 0; i < JGameMap.mapsize; i++) {
			for (int j = 0; j < JGameMap.mapsize ; j++) {
				try {
					g2d.drawImage(gameMap.at(i,j).getImage(),i*JGUIScreen.w_height/JGameMap.mapsize,j*JGUIScreen.w_width/JGameMap.mapsize,this);
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
