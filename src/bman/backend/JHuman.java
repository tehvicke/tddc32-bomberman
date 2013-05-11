package bman.backend;

import java.awt.event.KeyEvent;

import bman.frontend.gui.JGUIMapObject;
import bman.frontend.gui.JGUIMapObject.Direction;
import bman.networking.UDPEvent;
import bman.networking.UDPEventInterface;

/**
 * The Local Player
 * @author Petter
 *
 */
public class JHuman extends JPlayer{
	//private enum keyMapping{};
	private boolean shiftPressed = false;

	private int [] lastMove = {0,0};
	private JGameMap map;
	private JClient client;


	/**
	 * 
	 * @param sprite
	 * @param map
	 * @param client
	 */
	public JHuman(JGUIMapObject sprite,JGameMap map, JClient client) {
		super(sprite);
	}

	/**
	 * Sent from listener in JGUIGAMEMAP, tells the player to move
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
			System.out.println(shiftPressed);
		}


		if (dx != 0 || dy != 0) {
			if (shiftPressed) {
				this.turn(dx, dy);
			} else {
				this.move(dx, dy);
			}
		}

	}


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
		if (dx > 0) {
			sprite.move(Direction.RIGHT);
			lastMove[0] = 1;
			lastMove[1] = 0;
		} else if (dx < 0) {
			sprite.move(Direction.LEFT);
			lastMove[0] = -1;
			lastMove[1] = 0;
		} else if (dy > 0) {
			sprite.move(Direction.DOWN);
			lastMove[0] = 0;
			lastMove[1] = 1;
		} else {
			sprite.move(Direction.UP);
			lastMove[0] = 0;
			lastMove[1] = -1;
		}
	}
	
	/**
	 * Moves the object and changes the sprite with appropriate direction, Lastmove så bomben kan läggas
	 * @param dx
	 * @param dy
	 */
	public void move(int dx, int dy) {
		if (dx > 0) {
			sprite.move(Direction.RIGHT);
			lastMove[0] = 1;
			lastMove[1] = 0;
		} else if (dx < 0) {
			sprite.move(Direction.LEFT);
			lastMove[0] = -1;
			lastMove[1] = 0;
		} else if (dy > 0) {
			sprite.move(Direction.DOWN);
			lastMove[0] = 0;
			lastMove[1] = 1;
		} else {
			sprite.move(Direction.UP);
			lastMove[0] = 0;
			lastMove[1] = -1;
		}
		client.sendMove(dx, dy);
	}

	/**
	 * Pubts a bomb next to the player
	 */
	public void putBomb() {
		int[] loc = map.find(this.hashCode());
		client.putBomb(loc[0]+lastMove[0], loc[1]+lastMove[1]);
	}

	@Override
	public void destroy() {
		if (this instanceof JHuman) {
			client.getUDPClient().sendEvent(
					new UDPEvent(
							UDPEventInterface.Type.player_die, 
							client.getUDPClient().hashCode()));
		} else {
			System.err.println("Jag dog inte!!");
		}	
	}

}
