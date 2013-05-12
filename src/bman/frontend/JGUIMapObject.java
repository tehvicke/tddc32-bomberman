package bman.frontend;

import java.awt.Image;
import java.awt.Toolkit;


/**
 * The graphical representation of a JMapObject,
 * contains Images for movement in different directions
 * and functions for switching between these.
 * @author petter
 *
 */
public class JGUIMapObject {
	
	//Enum for determining appropriate sprite to use
	public enum Direction {
		DOWN,UP,LEFT,RIGHT;
	}

	/**
	 * Members		
	 */
	Image[] sprites;
	/* The sprite currently active */
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






