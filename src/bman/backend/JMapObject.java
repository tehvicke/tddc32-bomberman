package bman.backend;

import java.awt.Image;

import bman.frontend.gui.JGUIMapObject;
/**
 * Any object which takes up room in the map
 * @author Petter
 *
 */
public abstract class JMapObject {
	protected JGUIMapObject sprite;
	protected boolean destroyable = true;
	
	protected boolean destroyable = true;
	
	/**
	 * Constructor, may be subject to change
	 * @param sprite the JGUIMapObject representing the mapobject
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
	
	/**
	 * Returns whether the object is destroyable or not.
	 */
	public boolean isDestroyable() {
		return destroyable;
	}
}
