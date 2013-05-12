package bman.backend;



/**
 * The class containing the matrix on which the game is played.
 * The class is responsible for the position of every object in the game,
 * and implements functions for finding, moving, removing etc. MapObjects.
 * @author Petter
 * 
 */
public class JGameMap {
	public static final int mapsize = 15;
	
	private JMapObject[][] gameMap;
	
	//References to the players
	private JPlayer[] players;
	private int[] playerIDs = {-1,-1};


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
	 * Adds a player to gameMap and stores references to them in 
	 * private variables for faster access.
	 * @param player player object to be added
	 * @param id id of the player
	 * @param x start x position
	 * @param y start y position
	 */
	public void addPlayer(JPlayer player, int id, int x, int y) {

		addObject(player, x, y);
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
	 * Adds an JMapObject at the specified location in the gameMap, if the
	 * location is occupied it does nothing.
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

	/** Moves an object in the gameMap, if invalid coordinates or
	 *  nonempty target location does nothing.
	 * 
	 * @param fromx x coord to move from
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
	 * Moves a player object in the gameMap
	 * @param dx relative movement in x
	 * @param dy relative movement in y
	 * @param id id of the player to move
	 */
	public void movePlayer(int dx, int dy, int id) {
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
	 * Removes specified object from the gameMap, does nothing if the object is not found.
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
	 * Returns whether or not a position in the gameMap is empty and thus movable to
	 * @param x x coord of the position
	 * @param y y coord of the position
	 * @return true if empty
	 */
	public boolean validMove(int x, int y) {
		System.out.println(x + " " + y);
		if (gameMap[x][y] == null) {
			return true;
		}
		return false;
	}

	/**
	 * Removes object at specified position and calls the objects
	 * destroy() function, used by bombs when exploding.
	 * @param x x coord of the object
	 * @param y y coord of the object
	 */
	public void destroy(int x, int y) {
		if (gameMap[x][y] != null) {
			gameMap[x][y].destroy();
		}
		gameMap[x][y] = null;
	}
	
	/**
	 * Removes an object from the grid. Similar to destroy(x, y) but without calling
	 * the objects destroy() function. Mainly used for player movement.
	 * @param x x coord
	 * @param y y coord
	 */
	public void remove(int x, int y) {
		gameMap[x][y] = null;
	}
	

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

		if (
				loc[0] != -1 && 
				loc[0]+dx > 0 && 
				loc[0]+dx < mapsize && 
				loc[1]+dy > 0 && 
				loc[1]+dy < mapsize && 
				gameMap[loc[0]+dx][loc[1]+dy] == null
			) {
			return true;
		}
		else if (gameMap[loc[0]+dx][loc[1]+dy] instanceof JFire) {
			gameMap[loc[0]][loc[1]].destroy();
		}
		return false;
	}

}
