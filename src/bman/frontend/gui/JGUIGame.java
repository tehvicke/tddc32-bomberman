package bman.frontend.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import com.sun.xml.internal.dtdparser.InputEntity;

import bman.backend.JGameMap;
import bman.backend.JHuman;

public class JGUIGame extends JPanel implements ActionListener {

	/* *
	 * VARIABLES
	 */
	private static final long serialVersionUID = -5735185698246996895L;

	/* *****************************************************************************************************
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

	public static final String bomb_fire = "./sprites/bomb_fire.png";
	public static final String bomb_nofire = "./sprites/solid_block.png";;

	public static final String explosion = "./sprites/destroyable_block.png";
	public static final String fireCenter = "./sprites/ugly_fire_center.png";
	public static final String fireUp = "./sprites/ugly_fire_up.png";
	public static final String fireDown = "./sprites/ugly_fire_down.png";
	public static final String fireLeft = "./sprites/ugly_fire_left.png";
	public static final String fireRight = "./sprites/ugly_fire_right.png";
	public static final String fireHoriz = "./sprites/ugly_fire_center_horiz.png";
	public static final String fireVert = "./sprites/ugly_fire_center_vert.png";

	public static final String superman = "./sprites/superman.png";

	/* ***************************************************************************************************/

	/* *
	 * Contents
	 */
	public static JGUIMapObject solidBlockGUI = new JGUIMapObject(solidBlock);
	public static JGUIMapObject destroyableBlockGUI = new JGUIMapObject(destroyableBlock);
	public static JGUIMapObject bomb = new JGUIMapObject(bomb_fire);
	public static JGUIMapObject player1 = new JGUIMapObject(superman);
	public static JGUIMapObject player2 = new JGUIMapObject(player_front);

	/*
	 * Contents
	 */
	JGameMap gameMap;
	JHuman player;


	/* KEY BINDING */
	InputMap myInputMap = new InputMap();
    ActionMap myActionMap = new ActionMap();
	
	/**
	 * 
	 * @param gameMap
	 * @param player
	 */
	public JGUIGame(JGameMap gameMap, JHuman player) {
		//Window properties
		setSize(JGUIScreen.w_width, JGUIScreen.w_height);
		setVisible(true);
		this.setBackground(Color.LIGHT_GRAY);

		//VIKTOR
		initializeKeyListeners();
		
		System.out.println("Tjohohlo");

		setFocusable(true);
		setDoubleBuffered(true);
		//Timer which triggers actionlistener in this class
		Timer timer = new Timer(6, this);
		timer.start();
		this.gameMap = gameMap;
		this.player = player;

	}
	
	/**
	 * Initializes the key listeners for controlling the player.
	 */
	private void initializeKeyListeners() {
		myInputMap = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "move_up");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "move_down");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "move_left");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "move_right");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK, false), "turn_up");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK, false), "turn_down");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK, false), "turn_left");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK, false), "turn_right");
		myInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0, false), "lay_bomb");
		myActionMap = this.getActionMap();
		myActionMap.put("move_up", new KeyPressed(KeyEvent.VK_UP));
		myActionMap.put("move_down", new KeyPressed(KeyEvent.VK_DOWN));
		myActionMap.put("move_left", new KeyPressed(KeyEvent.VK_LEFT));
		myActionMap.put("move_right", new KeyPressed(KeyEvent.VK_RIGHT));
		myActionMap.put("turn_up", new KeyPressed(KeyEvent.VK_UP, true));
		myActionMap.put("turn_down", new KeyPressed(KeyEvent.VK_DOWN, true));
		myActionMap.put("turn_left", new KeyPressed(KeyEvent.VK_LEFT, true));
		myActionMap.put("turn_right", new KeyPressed(KeyEvent.VK_RIGHT, true));
		myActionMap.put("lay_bomb", new KeyPressed(KeyEvent.VK_SPACE));

	}

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

	public class KAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			player.keypress(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				player.keypress(e);
			}
		}
	}

	public class KBinding extends AbstractAction {
		KeyEvent actionIndex;
		public KBinding() { 
			KeyboardFocusManager manager =  
					KeyboardFocusManager.getCurrentKeyboardFocusManager();  
			manager.addKeyEventDispatcher(new KeyEventDispatcher() {
				public boolean dispatchKeyEvent(KeyEvent e) {
					actionIndex = e;  
					return false;
				}
			});  
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			player.keypress(actionIndex);

		}
	}
	
	final class KeyPressed extends AbstractAction {
        /**
		 * ID
		 */
		private static final long serialVersionUID = 1L;
		
		int key;
        boolean shift = false;
//		
        public KeyPressed(int key) {
        	this.key = key;
        }
        
        public KeyPressed(int key, boolean shift) {
        	this.key = key;
        	this.shift = shift;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
    		System.err.println("EEEEEEE KNAPP NERE! " + this.key);
        	if (shift) {
        		System.out.println("shift");
        		player.turnKey(key);
        	} else {
        		player.moveKey(key);
        	}
        }
        	
	}
}
