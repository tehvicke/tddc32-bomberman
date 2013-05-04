package bman.backend;

import bman.backend.JMapObject;
import bman.frontend.gui.JGUIMapObject;
import bman.frontend.gui.JGUIMapObject.Direction;

public class JPlayer extends JMapObject {
	 private String playerName;
	 
	 public JPlayer(JGUIMapObject sprite,int x, int y) {
		 super(sprite, x, y);
	 }
	
	public void setName(String playerName){
		this.playerName=playerName;
		
	}
	
	
	
	/**
	 * Moves the object and changes the sprite with appropriate direction
	 * @param dx
	 * @param dy
	 */
	public void move(int dx, int dy) {
		if (dx > 0) {
			sprite.move(Direction.RIGHT);
		} else if (dx < 0) {
			sprite.move(Direction.LEFT);
		} else if (dy > 0) {
			sprite.move(Direction.DOWN);
		} else {
			sprite.move(Direction.UP);
		}
		x_pos += dx;
		y_pos += dy;
	}
}
