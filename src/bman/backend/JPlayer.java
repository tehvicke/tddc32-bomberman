package bman.backend;

import bman.backend.JMapObject;
import bman.frontend.gui.JGUIGameMap;
import bman.frontend.gui.JGUIMapObject;
import bman.frontend.gui.JGUIScreen;
import bman.frontend.gui.JGUIMapObject.Direction;

public class JPlayer extends JMapObject {
	private String playerName;
	JGameMap map;
	int [] lastMove = {0,0};

	public JPlayer(JGUIMapObject sprite,JGameMap map) {
		super(sprite);
		this.map = map;
	}

	public void setName(String playerName){
		this.playerName=playerName;

	}
	
	public void putBomb() {
		int[] loc = map.find(this.hashCode());
		System.out.println("x: " + loc[0] + " | y: " +loc[1]);
		map.addObject(new JMapObject(new JGUIMapObject(JGUIGameMap.bomb_nofire)), loc[0]+lastMove[0], loc[1]+lastMove[1]);
		System.out.println("kördes");
	}



	/**
	 * Moves the object and changes the sprite with appropriate direction
	 * @param dx
	 * @param dy
	 */
	public void move(int dx, int dy) {
		if (dx > 0) {
			sprite.move(Direction.RIGHT);
			lastMove[0] = 1;
			lastMove[1] = 0;
		} else if (dx < 0) {
			sprite.move(Direction.LEFT);
			lastMove[0] = -1;
			lastMove[1] = 0;
		} else if (dy > 0) {
			sprite.move(Direction.DOWN);
			lastMove[0] = 0;
			lastMove[1] = 1;
		} else {
			sprite.move(Direction.UP);
			lastMove[0] = 0;
			lastMove[1] = -1;
		}

		map.move(dx, dy, this);
	}
}