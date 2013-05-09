package bman.backend;

import java.awt.Image;

import bman.frontend.gui.JGUIMapObject;
/**
 * Any object which takes up room in the map
 * @author Petter
 *
 */
public class JMapObject {
	protected JGUIMapObject sprite;
	
	/**
	 * Constructor, may be subject to change
	 * @param sprite the JGUIMapObject representing the mapobject
	 * @param x x location
	 * @param y y location
	 */
	public JMapObject(JGUIMapObject sprite) {
		this.sprite = sprite;
	}


	/**
	 * Returns currently active sprite of the object
	 * @return the currently active Sprite
	 */
	public Image getImage() {
		return sprite.getImage();
	}
	
	
}
