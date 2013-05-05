package bman.backend;

import bman.frontend.gui.JGUIGameMap;
import bman.frontend.gui.JGUIMapObject;
import bman.frontend.gui.JGUIScreen;

/**
 * The GameMap
 * @author Petter
 * Removed setsize as this does not need to be changed and can be done in the constructor
 */
public class JGameMap {
	public static int mapsize =15;
	private JMapObject[][] gameMap;

	public JGameMap() {
		gameMap = new JMapObject[mapsize][mapsize];

		//Creates default layout
		JGUIMapObject block = new JGUIMapObject(JGUIGameMap.solidBlock); 
		for (int i = 0; i < 15; i++) {
			addObject(new JMapObject(block),0,i);
			addObject(new JMapObject(block),mapsize-1,i);
		}
		for (int i = 0; i < 15; i++) {
			addObject(new JMapObject(block),i,0);
			addObject(new JMapObject(block),i,mapsize-1);
		}
	}


	/**
	 * Adds an JMapObject at the specified location in the gameMap
	 * @param obj object to be added
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void addObject(JMapObject obj,int x, int y) {
		if (x > mapsize || y > mapsize || x < 0 || y < 0) {
			return;
		}
		if (gameMap[x][y] == null) {
			gameMap[x][y] = obj;
		}
	}

	public void moveObject(int fromx, int fromy, int tox, int toy) {
		if (tox > mapsize-1 || toy > mapsize-1 || tox < 0 || toy < 0) {
			return;
		}
		if (gameMap[tox][toy] == null) {
			gameMap[tox][toy] = gameMap[fromx][fromy];
			gameMap[fromx][fromy] = null;
		}
	}

	public void removeObject(int x, int y) {
		gameMap[x][y] = null;
	}
	/**
	 * Returns the position of a MapObject, if not found returns -1,-1
	 * @param key hashcode of object, used as search key
	 * @return location of MapObject, (-1,-1) if not found
	 */
	public int[] find(int key) {
		int[] location = {-1,-1};
		for (int i = 0; i < mapsize ; i++) {
			for (int j  = 0; j < mapsize ; j++)

				if (gameMap[i][j] != null && key == gameMap[i][j].hashCode()) {
					location[0] = i;
					location[1] = j;
					break;
				}
		}
		return location;
	}

	/**
	 * Moves a player object 
	 * @param dx 
	 * @param dy
	 * @param obj object to be moved
	 */
	public void move(int dx, int dy, JMapObject obj) {
		int [] loc = find(obj.hashCode());
		if (loc[0] != -1)
			moveObject(loc[0], loc[1], loc[0]+dx, loc[1]+dy);
	}
	
	public void remove(JMapObject obj) {
		int [] loc = find(obj.hashCode());
		if (loc[0] != -1)
			removeObject(loc[0],loc[1]);
	}


	/**
	 * Returns the object at the specified location in the gameMap
	 * @param x
	 * @param y
	 * @return
	 */
	public JMapObject at(int x, int y) {
		return gameMap[x][y];
	}
}
