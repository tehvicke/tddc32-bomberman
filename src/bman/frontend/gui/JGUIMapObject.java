package bman.frontend.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;


/**
 * Underliggande behöver länkas in för verifiering av move, kan även behöva en ref till underliggande objekt
 * @author petter
 *
 */
public class JGUIMapObject {

	/**
	 * Members
	 */

	Image front;
	Image back;
	Image left;
	Image right;
	Image active;
	int x_pos;
	int y_pos;

	/**
	 * 
	 * @param frontsp
	 * @param backsp
	 * @param leftsp
	 * @param rightsp
	 * @param xpos
	 * @param ypos
	 */
	public JGUIMapObject() {
		
	}
	
	public JGUIMapObject(String sprite, int xpos, int ypos) {
		x_pos = xpos;
		y_pos = ypos;
		active = Toolkit.getDefaultToolkit().getImage(sprite);
	}
	public JGUIMapObject(String frontsp, String backsp, String leftsp, String rightsp, int xpos, int ypos) {
		front = Toolkit.getDefaultToolkit().getImage(frontsp);
		back = Toolkit.getDefaultToolkit().getImage(backsp);
		left = Toolkit.getDefaultToolkit().getImage(leftsp);
		right = Toolkit.getDefaultToolkit().getImage(rightsp);
		active = front;
		x_pos = xpos;
		y_pos = ypos;

	}


	protected Image getImage() {
		return active;
	}

	protected int getX() {
		return x_pos;
	}
	protected int getY() {
		return y_pos;
	}




}






