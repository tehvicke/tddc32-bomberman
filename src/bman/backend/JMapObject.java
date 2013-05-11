package bman.backend;

import java.awt.Image;

import bman.frontend.gui.JGUIMapObject;
/**
 * Any object which takes up room in the map and has a Graphical representation
 * in the form of a JGUIMapObject. JMapObjects can be destroyable or not.
 * @author Petter
 *
 */
public abstract class JMapObject {
	protected JGUIMapObject sprite;
	protected boolean destroyable = false;
	
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
	
	/**
	 * A function for destroying an object. Default is to do nothing but it
	 * can be overridden if needed.
	 */
	public void destroy() {
		
	}
}
