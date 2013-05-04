package bman.frontend.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import bman.backend.JMapObject;


/**
 * Underliggande beh�ver l�nkas in f�r verifiering av move, kan �ven beh�va en ref till underliggande objekt
 * @author petter
 *
 */
public class JGUIMapObject {
	
	public enum Direction {
		DOWN,UP,LEFT,RIGHT;
	}

	/**
	 * Members
	 */
	Image[] sprites;
	Image active;

	/**
	 * 
	 * @param obj
	 */
	public JGUIMapObject() {
		
	}
	
	public JGUIMapObject(String sprite) {
		
		active = Toolkit.getDefaultToolkit().getImage(sprite);
	}
	public JGUIMapObject(String frontsp, String backsp, String leftsp, String rightsp) {
		sprites = new Image[4];
		
		sprites[0] = Toolkit.getDefaultToolkit().getImage(frontsp);
		sprites[1] = Toolkit.getDefaultToolkit().getImage(backsp);
		sprites[2]= Toolkit.getDefaultToolkit().getImage(leftsp);
		sprites[3]= Toolkit.getDefaultToolkit().getImage(rightsp);
		active = sprites[0];
		

	}
	/**
	 * Changes the MapObject to use appropriate sprite;
	 * @param dir Enum containing move direction
	 */
	public void move(Direction dir) {
		if (sprites != null) {
		active = sprites[dir.ordinal()];
		}
	}


	public Image getImage() {
		return active;
	}

}






