package bman.backend;

import java.awt.Image;

import bman.frontend.gui.JGUIMapObject;
/**
 * Any object which takes up room in the map
 * @author Petter
 *
 */
public class JMapObject {
	// I WANNA REMOVE POSITION AND HAVE 2 VALUES //Petter
	private int[][] position;
	protected int x_pos;
	protected int y_pos;
	protected JGUIMapObject sprite;
	
	/**
	 * Constructor, may be subject to change
	 * @param sprite the JGUIMapObject representing the mapobject
	 * @param x x location
	 * @param y y location
	 */
	public JMapObject(JGUIMapObject sprite,int x, int y) {
		setPosition(x, y);
		this.sprite = sprite;
	}
	/**
	 * Changes the position of the object
	 * @param x
	 * @param y
	 */
	public void setPosition(int x,int y){
		x_pos = x;
		y_pos = y;
		
	}
	/**
	 * Can we remove this?
	 * @return
	 */
	public int[][] getPosition(){
		return position;
		
	}
	/**
	 * Returns the objects X position
	 * @return the mapObjects x position
	 */
	public int getX() {
		return x_pos;
	}
	/**
	 * Return the objects Y position
	 * @return the mapObjects y position
	 */
	public int getY() {
		return y_pos;
	}
	/**
	 * Returns currently active sprite of the object
	 * @return the currently active Sprite
	 */
	public Image getImage() {
		return sprite.getImage();
	}
}
