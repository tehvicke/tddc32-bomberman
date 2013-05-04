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
	int dx =  0;
	int dy =  0;

	public JHuman(JGUIMapObject obj, int x, int y) {
		super(obj, x, y);
	}
	
	/**
	 * Sent from listener in JGUIGAMEMAP, tells the player to move
	 * @param e
	 */
	public void keypress(KeyEvent e) {
		int key = e.getKeyCode();
		int dx = 0;
		int dy = 0;
		if (key == KeyEvent.VK_LEFT) {
			dx = -10;
		} else if (key == KeyEvent.VK_RIGHT) {
			dx = 10;
		} else if (key == KeyEvent.VK_UP) {
			dy = -10;
		} else if (key == KeyEvent.VK_DOWN) {
			dy = 10;
		}
		super.move(dx, dy);
		
	}

	


}
