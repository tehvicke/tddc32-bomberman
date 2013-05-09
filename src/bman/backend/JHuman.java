package bman.backend;

import java.awt.event.KeyEvent;

import bman.frontend.gui.JGUIMapObject;

/**
 * The Local Player
 * @author Petter
 *
 */
public class JHuman extends JPlayer{
	//private enum keyMapping{};
	private boolean shiftPressed = false;



	public JHuman(JGUIMapObject obj, JGameMap map,JClient cclient) {
		super(obj,map,cclient);
		
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
				super.turn(dx, dy);
			} else {
				super.move(dx, dy);
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
				super.move(dx, dy);
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
				super.turn(dx, dy);
		}
	}


}
