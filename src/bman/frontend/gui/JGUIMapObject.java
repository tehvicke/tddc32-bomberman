package bman.frontend.gui;

import java.awt.Image;
import java.awt.Toolkit;


/**
 * Class containing the graphical representation of a JMapObject
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
	 * Constructor for objects requiring only one sprite
	 * @param sprite sprite which should be used for the object
	 */
	public JGUIMapObject(String sprite) {
		
		active = Toolkit.getDefaultToolkit().getImage(sprite);
	}
	/**
	 * Constructor for moving objects
	 * @param frontsp Front sprite
	 * @param backsp back sprite
	 * @param leftsp left sprite
	 * @param rightsp right sprite
	 */
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
	 * @param dir Move direction
	 */
	public void move(Direction dir) {
		if (sprites != null) {
		active = sprites[dir.ordinal()];
		}
	}

	/**
	 * Returns the active Image/Sprite of the JGUIMapObject
	 * @return active Image
	 */
	public Image getImage() {
		return active;
	}

}






