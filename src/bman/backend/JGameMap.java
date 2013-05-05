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
	public static final int mapsize =15;
	private JMapObject[][] gameMap;

	public JGameMap() {
		gameMap = new JMapObject[mapsize][mapsize];

		//Creates default layout
		JGUIMapObject block = new JGUIMapObject(JGUIGameMap.solidBlock); 
		JGUIMapObject dblock = new JGUIMapObject(JGUIGameMap.destroyableBlock);
		for (int i = 0; i < 15; i++) {
			addObject(new JDestroyableBlock(dblock),0,i);
			addObject(new JDestroyableBlock(dblock),mapsize-1,i);
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


	/**
	 * Create an explosion centered at given coordinates and with specified radius
	 * @param x center x coord
	 * @param y center y coord
	 * @param radius radius of the explosion
	 */
	public void explosion(int x, int y, int radius) {
		addObject(new JFire(new JGUIMapObject(JGUIGameMap.fireCenter)), x, y);





		for (int i = 1; i < radius ; i++) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (gameMap[x+i][y] instanceof JDestroyableBlock) {
				removeObject(x+i,y);
			}
			if (gameMap[x][y+i] instanceof JDestroyableBlock) {
				removeObject(x, y+i);
			}

			addObject(new JFire(new JGUIMapObject(JGUIGameMap.fireHoriz)), x+i, y);
			addObject(new JFire(new JGUIMapObject(JGUIGameMap.fireVert)), x, y+i);
			addObject(new JFire(new JGUIMapObject(JGUIGameMap.fireHoriz)), x-i, y);
			addObject(new JFire(new JGUIMapObject(JGUIGameMap.fireVert)), x, y-i);
		}
		addObject(new JFire(new JGUIMapObject(JGUIGameMap.fireRight)),x+radius,y);
		addObject(new JFire(new JGUIMapObject(JGUIGameMap.fireLeft)),x-radius,y);
		addObject(new JFire(new JGUIMapObject(JGUIGameMap.fireUp)),x,y-radius);
		addObject(new JFire(new JGUIMapObject(JGUIGameMap.fireDown)),x,y+radius);
		try {
			Thread.sleep(800);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = radius; i >0 ; i--) {
			if (gameMap[x+i][y] instanceof JDestroyableBlock || gameMap[x+i][y] instanceof JFire)
				removeObject(x+i, y);
			if (gameMap[x][y+i] instanceof JDestroyableBlock || gameMap[x][y+i] instanceof JFire)
				removeObject(x, y+i);
			if (gameMap[x-i][y] instanceof JDestroyableBlock || gameMap[x-i][y] instanceof JFire)
				removeObject(x-i,y);
			if (gameMap[x][y-i] instanceof JDestroyableBlock || gameMap[x][y-i] instanceof JFire)
				removeObject(x, y-i);

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		removeObject(x, y);
	}
}
