package bman.backend;

import java.util.Random;

import bman.frontend.gui.JGUIGame;
import bman.frontend.gui.JGUIMapObject;


/**
 * The GameMap
 * @author Petter
 * 
 */
public class JGameMap {
	public static final int mapsize = 15;
	private JMapObject[][] gameMap;
	private JPlayer[] players;
	int[] playerIDs = {-1,-1};


	/**
	 * Defaults constructor, creates JGameMap with default layout.
	 */
	public JGameMap() {
		gameMap = new JMapObject[mapsize][mapsize];
		players = new JPlayer[2];
	}

	/**
	 * Adds a row with objects in the map
	 * @param row String containing the layout of the row (d for destroyable, s for solid)
	 * @param rowIndex index of the row which should be added
	 */
	public void addMapRow(String row, int rowIndex) {
		if (row.length() > mapsize) {
			return;
		}

		for (int i = 0; i < row.length(); i++) {
			if (row.charAt(i) == 'd') {
				addObject(new JDestroyableBlock(),i,rowIndex);
			} else if (row.charAt(i) == 's') {
				addObject(new JSolidBlock(),i,rowIndex);
			}
		}
	}
	
	/**
	 * This randomizes where the destroyable blocks are in the game map.
	 * 
	 * @param percentFilled The amount of destoryable blocks in the map as percent.
	 * No more than 40 % recommended.
	 */
	public void randomizedMap(int percentFilled) {
		Random gen = new Random();
		for (int col = 0; col < mapsize; col++) {
			for (int row = 0; row < mapsize; row++) {
				if (
						(row == 0) || // top row solid blocks
						(col == 0) || // left row solid blocks
						(col == mapsize - 1) || // right row solid blocks
						(row == mapsize - 1) || // bottom row solid blocks
						(row % 2 == 0 && col % 2 == 0) // middle solid blocks
						) {
					addObject(new JSolidBlock(), col, row);
				} else {
					int number = gen.nextInt(100);
					if (number < percentFilled) {
						addObject(new JDestroyableBlock(), col, row);
					}
				}
			}
 		}
		}

	/**
	 * Adds a player to gameMap
	 * @param player player mapobject to be added
	 * @param id id of the player
	 * @param x start x position
	 * @param y start y position
	 */
	public void addPlayer(JPlayer player, int id, int x, int y) {
		Random gen = new Random();
		int maxCount = 1000;
		while(true) {
			int x_rand = gen.nextInt(mapsize);
			int y_rand = gen.nextInt(mapsize);
			if (this.gameMap[x_rand][y_rand] == null) { // Only add if space is empty
				addObject(player, x_rand, y_rand);
				break;
			}
			if (maxCount-- < 0) {
				System.err.println("Nowhere to put player " + player.getID() + ". Exits game.");
				System.exit(0);
			}
		}
		
//		addObject(player, x, y);
		if (playerIDs[0] == -1) {
			playerIDs[0] = id;
			players[0] = player;
		}
		else {
			players[1] = player;
			playerIDs[1] = id;
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

	/** Moves an object in the gameMap.
	 * 
	 * @param fromx x coord to move from2
	 * @param fromy y coord to move from
	 * @param tox   x coord to move to
	 * @param toy   y coord to move to
	 */
	public void moveObject(int fromx, int fromy, int tox, int toy) {
		if (tox > mapsize-1 || toy > mapsize-1 || tox < 0 || toy < 0) {
			return;
		}
		if (gameMap[tox][toy] == null) {
			gameMap[tox][toy] = gameMap[fromx][fromy];
			gameMap[fromx][fromy] = null;
		}
	}
	/**
	 * Removes an object at a given coord
	 * @param x x coord
	 * @param y y coord
	 * @throws ArrayIndexOutOfBoundsException 
	 */


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
	 * Relative move for a JMapObject in the gamemap
	 * @param dx relative movement in x
	 * @param dy relative movement in x
	 * @param obj object to be moved
	 */
	public void move(int dx, int dy, JMapObject obj) {
		int [] loc = find(obj.hashCode());
		if (loc[0] != -1)
			moveObject(loc[0], loc[1], loc[0]+dx, loc[1]+dy);
	}
	/**
	 * 
	 * @param dx
	 * @param dy
	 * @param id
	 */
	public void absoluteMove(int dx, int dy, int id) {
		int [] loc;
		if (id == playerIDs[0]) {
			loc = find(players[0].hashCode());
		} else {
			loc = find(players[1].hashCode());
		}
		moveObject(loc[0], loc[1],loc[0]+dx,loc[1]+dx);
	}

	/**
	 * Relative move for player with id
	 * @param dx relative movement in x
	 * @param dy relative movement in y
	 * @param id id of the player to move
	 */
	public void move(int dx, int dy, int id) {
		if (playerIDs[0] == id) {
			move(dx,dy,players[0]);
		} else {
			move(dx,dy,players[1]);
		}
	}

	/**
	 * Removes specified object from gamemap
	 * @param obj JMapObject to be removed
	 */
	public void remove(JMapObject obj) {
		int [] loc = find(obj.hashCode());
		if (loc[0] != -1)
			remove(loc[0],loc[1]);
	}

	/**
	 * Returns the object at the specified location in the gameMap
	 * @param x x coord of object
	 * @param y y coord of object
	 * @return object at specified location
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public JMapObject at(int x, int y) throws ArrayIndexOutOfBoundsException {
		return gameMap[x][y];
	}
	
	/**
	 * Removes object at specified position
	 * @param x x coord of the object
	 * @param y y coord of the object
	 */
	public void destroy(int x, int y) {
		if (gameMap[x][y] != null) {
			gameMap[x][y].destroy();
		}
		gameMap[x][y] = null;
	}
	public void remove(int x, int y) {
		gameMap[x][y] = null;
	}


	/**
	 * Create an explosion centered at given coordinates and with specified radius
	 * @param x center x coord
	 * @param y center y coord
	 * @param radius radius of the explosion
	 */
//	public void explosion(int x, int y, int radius) {
//		addObject(new JFire(new JGUIMapObject(JGUIGame.fireCenter)), x, y);
//
//		for (int i = 1; i < radius ; i++) {
//			try {
//				Thread.sleep(50);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			if (gameMap[x+i][y] instanceof JDestroyableBlock) {
//				removeObject(x+i,y);
//			}
//			if (gameMap[x][y+i] instanceof JDestroyableBlock) {
//				removeObject(x, y+i);
//			}
//
//			addObject(new JFire(new JGUIMapObject(JGUIGame.fireHoriz)), x+i, y);
//			addObject(new JFire(new JGUIMapObject(JGUIGame.fireVert)), x, y+i);
//			addObject(new JFire(new JGUIMapObject(JGUIGame.fireHoriz)), x-i, y);
//			addObject(new JFire(new JGUIMapObject(JGUIGame.fireVert)), x, y-i);
//		}
//		addObject(new JFire(new JGUIMapObject(JGUIGame.fireRight)),x+radius,y);
//		addObject(new JFire(new JGUIMapObject(JGUIGame.fireLeft)),x-radius,y);
//		addObject(new JFire(new JGUIMapObject(JGUIGame.fireUp)),x,y-radius);
//		addObject(new JFire(new JGUIMapObject(JGUIGame.fireDown)),x,y+radius);
//		try {
//			Thread.sleep(200);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		for (int i = radius; i >0 ; i--) {
//			if (gameMap[x+i][y] instanceof JDestroyableBlock || gameMap[x+i][y] instanceof JFire)
//				removeObject(x+i, y);
//			if (gameMap[x][y+i] instanceof JDestroyableBlock || gameMap[x][y+i] instanceof JFire)
//				removeObject(x, y+i);
//			if (gameMap[x-i][y] instanceof JDestroyableBlock || gameMap[x-i][y] instanceof JFire)
//				removeObject(x-i,y);
//			if (gameMap[x][y-i] instanceof JDestroyableBlock || gameMap[x][y-i] instanceof JFire)
//				removeObject(x, y-i);
//
//			if (gameMap[x+i][y] instanceof JBomb)
//				((JBomb)gameMap[x+i][y]).explode();
//			if (gameMap[x][y+i] instanceof JBomb)
//				((JBomb)gameMap[x][y+i]).explode();
//			if (gameMap[x-i][y] instanceof JBomb)
//				((JBomb)gameMap[x-i][y]).explode();
//			if (gameMap[x][y-i] instanceof JBomb)
//				((JBomb)gameMap[x][y-i]).explode();
//
//			try {
//				Thread.sleep(50);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		removeObject(x, y);
//	}

	/**
	 *  Check if a move is valid, 
	 * @param id id of the moving player
	 * @param dx x direction of the move
	 * @param dy y direction of the move
	 * @return True if move is valid
	 */
	public boolean validMove(int id, int dx, int dy) {
		int[] loc;
		if (id == playerIDs[0]) {
			loc = find(players[0].hashCode());
		} else {
			loc = find(players[1].hashCode());
		}

		if (loc[0] != -1 && loc[0]+dx > 0 && loc[0]+dx < mapsize && loc[1]+dy > 0 && loc[1]+dy < mapsize && gameMap[loc[0]+dx][loc[1]+dy] == null) {
			return true;
		}
		return false;
	}
	


}
