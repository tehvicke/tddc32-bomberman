package bman.frontend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import bman.JBomberman;
import bman.backend.JGameMap;
import bman.backend.JHuman;

/**
 * The GUI implementation of the game, Draws the game screen and contains keylistener for player control.
 * Also implements actionlistener as a way of refreshing the window 
 * @author petter
 *
 */
public class JGUIGame extends JPanel implements ActionListener {

	/* *
	 * VARIABLES	
	 */
	private static final long serialVersionUID = -5735185698246996895L;

	/* *****************************************************************************************************
	 * CONSTANTS
	 *****************************************************************************************************/
	
	private static final String player_front = "./sprites/white_front.png";
	private static final String player_back = "./sprites/white_back.png";
	private static final String player_left = "./sprites/white_left.png";
	private static final String player_right = "./sprites/white_right.png";

	private static final String p2_front = "./sprites/black_front.png";
	private static final String p2_back= "./sprites/black_back.png";;
	private static final String p2_left = "./sprites/black_left.png";;
	private static final String p2_right = "./sprites/black_right.png";;

	private static final String solidBlock = "./sprites/solid_block.png"; 
	private static final String destroyableBlock = "./sprites/destroyable_block.png";

	private static final String bomb_fire = "./sprites/bomb_fire.png";
	private static final String explosion = "./sprites/explosion.png";

	//public static final String superman = "./sprites/superman.png";

	/* ***************************************************************************************************/

	/* *
	 * Static JGUIMapObjects for usage by other classes, used to reduce unnecessary objects
	 */
	public static JGUIMapObject solidBlockGUI = new JGUIMapObject(solidBlock);
	public static JGUIMapObject destroyableBlockGUI = new JGUIMapObject(destroyableBlock);
	public static JGUIMapObject bomb = new JGUIMapObject(bomb_fire);
	public static JGUIMapObject player1 = new JGUIMapObject(player_front,player_back,player_left,player_right);
	public static JGUIMapObject player2 = new JGUIMapObject(p2_front,p2_back,p2_left,p2_right);
	public static JGUIMapObject fire = new JGUIMapObject(explosion);

	/*
	 * Contents
	 */
	JGameMap gameMap;
	JHuman player; //The player object


	/* KEY BINDING */
	InputMap myInputMap = new InputMap();
    ActionMap myActionMap = new ActionMap();
	
	/**
	 * Constructs JGUIGameMap objects
	 * @param gameMap JGameMap the JGUIGameMap is associated with
	 * @param player the player object
	 */
	public JGUIGame(JGameMap gameMap, JHuman player) {
		//Window properties
		setSize(JGUIScreen.w_width, JGUIScreen.w_height);
		setVisible(true);
		this.setBackground(Color.LIGHT_GRAY);

		/* Adds all things needed for listens on the right keys */
		initializeKeyListeners();

		setFocusable(true);
		setDoubleBuffered(true);
		//Timer which triggers actionlistener in this class
		Timer timer = new Timer(5, this);
		timer.start();
		this.gameMap = gameMap;
		this.player = player;

	}
	
	/**
	 * Initializes the key listeners for controlling the player.
	 */
	private void initializeKeyListeners() {
		if (JBomberman.debug) {
			System.out.println("Klient: Key listeners initiated.");
		}
		this.requestFocus();
		myInputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		myActionMap = this.getActionMap();
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "move_up");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "move_down");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "move_left");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "move_right");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK, false), "turn_up");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK, false), "turn_down");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK, false), "turn_left");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK, false), "turn_right");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0, false), "lay_bomb");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,InputEvent.SHIFT_DOWN_MASK, false), "lay_bomb");
		myActionMap.put("move_up", new KeyPressed(KeyEvent.VK_UP, false));
		myActionMap.put("move_down", new KeyPressed(KeyEvent.VK_DOWN, false));
		myActionMap.put("move_left", new KeyPressed(KeyEvent.VK_LEFT, false));
		myActionMap.put("move_right", new KeyPressed(KeyEvent.VK_RIGHT, false));
		myActionMap.put("turn_up", new KeyPressed(KeyEvent.VK_UP, true));
		myActionMap.put("turn_down", new KeyPressed(KeyEvent.VK_DOWN, true));
		myActionMap.put("turn_left", new KeyPressed(KeyEvent.VK_LEFT, true));
		myActionMap.put("turn_right", new KeyPressed(KeyEvent.VK_RIGHT, true));
		myActionMap.put("lay_bomb", new KeyPressed(KeyEvent.VK_SPACE, true));
	}

	/**
	 * Overwritten paint method, In addition to the paint in the superclass it also draws all objects in the gameMap and
	 * a gridline pattern.
	 */
	 public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		/* DRAWS THE GRID */
		for (int i = 0; i <= JGUIScreen.w_height; i +=30) {
			g2d.setColor(Color.black);
			g2d.drawLine(0, i, JGUIScreen.w_width,i );
		}
		for (int i = 0; i <= JGUIScreen.w_width; i +=30) {
			g2d.drawLine(i, 0,i,JGUIScreen.w_height);
		}

		/*Draws the objects on the map */
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
		repaint();
	}


	/**
	 * The key listener class.
	 * @author viktordahl
	 *
	 */
	private class KeyPressed extends AbstractAction {
        /**
		 * ID
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * The key that is being handled.
		 */
		private int key;
		
		/**
		 * Whether shift is pressed or not.
		 */
        private boolean shift = false;
        
        /**
         * 
         * @param key The key being pressed.
         * @param shift True if shift is being held.
         */
        public KeyPressed(int key, boolean shift) {
        	this.key = key;
        	this.shift = shift;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
        	if (JBomberman.debug) {
        		System.err.println("Button pressed: " + this.key);
        	}
        	if (shift && this.key != KeyEvent.VK_SPACE) { /* If shift is pressed the user shall only turn */
        		player.turnKey(key);
        	} else {
        		player.moveKey(key);
        	}
        }
        	
	}
}
