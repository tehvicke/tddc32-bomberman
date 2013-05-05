package bman.backend;

import bman.backend.JMapObject;
import bman.frontend.gui.JGUIGameMap;
import bman.frontend.gui.JGUIMapObject;
import bman.frontend.gui.JGUIScreen;
import bman.frontend.gui.JGUIMapObject.Direction;

public class JPlayer extends JMapObject {
	private String playerName;
	JGameMap map;
	int active_bombs = 0;
	int max_bombs = 2;
	int [] lastMove = {0,0};

	public JPlayer(JGUIMapObject sprite,JGameMap map) {
		super(sprite);
		this.map = map;
	}

	public void setName(String playerName){
		this.playerName=playerName;

	}
	
	public void putBomb() {
		if (active_bombs >= max_bombs)
			return;
		int[] loc = map.find(this.hashCode());
		JBomb bomb = new JBomb(new JGUIMapObject(JGUIGameMap.bomb_nofire),map,this);
		Thread t = new Thread(bomb, "t2");
		map.addObject(bomb,loc[0]+lastMove[0], loc[1]+lastMove[1]);
		t.start();
		active_bombs++;
	}
	
	public void detonated() {
		active_bombs--;
	}



	/**
	 * Moves the object and changes the sprite with appropriate direction, Lastmove så bomben kan läggas
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