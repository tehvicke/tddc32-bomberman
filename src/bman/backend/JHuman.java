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



	public JHuman(JGUIMapObject obj, JGameMap map) {
		super(obj,map);
		
	}

	/**
	 * Sent from listener in JGUIGAMEMAP, tells the player to move
	 * @param e
	 */
	public void keypress(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_L) {
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
		if (dx != 0 || dy != 0)
			super.move(dx, dy);

	}




}
