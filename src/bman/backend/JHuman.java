package bman.backend;

import java.awt.event.KeyEvent;

import bman.frontend.JGUIMapObject;
import bman.frontend.JGUIMapObject.Direction;
import bman.networking.UDPEvent;
import bman.networking.UDPEventInterface;

/**
 * The player controlled by the user. Is responsible for everything the user
 * can control such as movement and laying bombs.
 * @author Petter
 *
 */
public class JHuman extends JPlayer{
	//private enum keyMapping{};
	private boolean shiftPressed = false;

	/**
	 * The last position of the player
	 */
	private int [] lastMove = {0,0};
	
	/**
	 * The current gamemap
	 */
	private JGameMap map;
	
	/**
	 * The client associated with the player
	 */
	private JClient client;


	/**
	 * 
	 * @param spriteThe sprite of the object
	 * @param map The current gamemap
	 * @param client The client class
	 */
	public JHuman(JGUIMapObject sprite,JGameMap map, JClient client) {
		super(sprite);
		this.map = map;
		this.client = client;
	}

	/**
	 * Sent from listener in JGUIGameMap, tells the player to move
	 * @param e
	 */
	public void keypress(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SPACE) {
			putBomb();
			return;
		}
		int dx = 0;
		int dy = 0;
		if (key == KeyEvent.VK_LEFT) {
			dx = -1;
		} else if (key == KeyEvent.VK_RIGHT) {
			dx = 1;
		} else if (key == KeyEvent.VK_UP) {
			dy = -1;
		} else if (key == KeyEvent.VK_DOWN) {
			dy = 1;
		} else if (key == KeyEvent.VK_SHIFT) {
			if (shiftPressed) {
				shiftPressed = false;
			} else if (!shiftPressed){
				shiftPressed = true;
			}
		}
		if (dx != 0 || dy != 0) {
			if (shiftPressed) {
				this.turn(dx, dy);
			} else {
				this.move(dx, dy);
			}
		}
	}


	/**
	 * Decides what to from which key is pressed.
	 * @param key The key pressed.
	 */
	public void moveKey(int key) {
		if (key == KeyEvent.VK_SPACE) {
			putBomb();
			return;
		}
		int dx = 0;
		int dy = 0;
		if (key == KeyEvent.VK_LEFT) {
			dx = -1;
		} else if (key == KeyEvent.VK_RIGHT) {
			dx = 1;
		} else if (key == KeyEvent.VK_UP) {
			dy = -1;
		} else if (key == KeyEvent.VK_DOWN) {
			dy = 1;
		}
		if (dx != 0 || dy != 0) {
			this.move(dx, dy);
		}
	}
	
	/**
	 * Decides what to from which key is pressed.
	 * @param key The key pressed.
	 */
	public void turnKey(int key) {
		int dx = 0;
		int dy = 0;
		if (key == KeyEvent.VK_LEFT) {
			dx = -1;
		} else if (key == KeyEvent.VK_RIGHT) {
			dx = 1;
		} else if (key == KeyEvent.VK_UP) {
			dy = -1;
		} else if (key == KeyEvent.VK_DOWN) {
			dy = 1;
		}
		if (dx != 0 || dy != 0) {
			this.turn(dx, dy);
		}
	}

	/**
	 * Allows the player to turn without moving, allowing him to lay bombs
	 * in different directions.
	 * @param dx x coord
	 * @param dy y coord
	 */
	private void turn(int dx, int dy) {
		Direction dir = Direction.DOWN;
		if (dx > 0) {
			dir = Direction.RIGHT;
			//sprite.move(Direction.RIGHT);
			lastMove[0] = 1;
			lastMove[1] = 0;
		} else if (dx < 0) {
			dir = Direction.LEFT;
			//sprite.move(Direction.LEFT);
			lastMove[0] = -1;
			lastMove[1] = 0;
		} else if (dy > 0) {
			dir = Direction.DOWN;
			//sprite.move(Direction.DOWN);
			lastMove[0] = 0;
			lastMove[1] = 1;
		} else {
			dir = Direction.UP;
			//sprite.move(Direction.UP);
			lastMove[0] = 0;
			lastMove[1] = -1;
		}
		client.sendTurn(dir.ordinal());
	}

	/**
	 * Moves the object and changes the sprite with appropriate direction,
	 * Saves the last place so direction can be calculated for the bomb laying.
	 * @param dx x coord
	 * @param dy y coord
	 */
	private void move(int dx, int dy) {
		Direction dir = Direction.DOWN;
		if (dx > 0) {
			dir = Direction.RIGHT;
			//sprite.move(dir);
			lastMove[0] = 1;
			lastMove[1] = 0;
		} else if (dx < 0) {
			dir = Direction.LEFT;
			//sprite.move(dir);
			lastMove[0] = -1;
			lastMove[1] = 0;
		} else if (dy > 0) {
			dir = Direction.DOWN;
			//sprite.move(dir);
			lastMove[0] = 0;
			lastMove[1] = 1;
		} else {
			dir = Direction.UP;
			//sprite.move(dir);
			lastMove[0] = 0;
			lastMove[1] = -1;
		}
		client.sendMove(dx, dy,dir.ordinal());
	}

	/**
	 * Pubs a bomb in front of the player
	 */
	private void putBomb() {
		int[] loc = map.find(this.hashCode());
		client.putBomb(loc[0]+lastMove[0], loc[1]+lastMove[1]);
	}

	@Override
	public void destroy() {
		client.getUDPClient().sendEvent(
				new UDPEvent(
						UDPEventInterface.Type.player_die, 
						client.getUDPClient().hashCode()));
	}

}
