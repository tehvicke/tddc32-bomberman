package bman.backend;

import bman.frontend.gui.JGUIGameMap;
import bman.frontend.gui.JGUIMapObject;
import bman.frontend.gui.JGUIScreen;
import bman.networking.UDPEvent;

/**
 * The GameMap
 * @author Petter
 * Removed setsize as this does not need to be changed and can be done in the constructor
 */
public class JGameMap {
	public static final int mapsize =15;
	private JMapObject[][] gameMap;
	private JPlayer[] players;
	int[] playerIDs = {-1,-1};


	/**
	 * Defaults constructor, creates JGameMap with default layout.
	 */
	public JGameMap() {
		gameMap = new JMapObject[mapsize][mapsize];
		players = new JPlayer[2];

		//Creates default layout
//		JGUIMapObject block = new JGUIMapObject(JGUIGameMap.solidBlock); 
//		JGUIMapObject dblock = new JGUIMapObject(JGUIGameMap.destroyableBlock);
//		for (int i = 0; i < 15; i++) {
//			addObject(new JDestroyableBlock(dblock),0,i);
//			addObject(new JDestroyableBlock(dblock),mapsize-1,i);
//		}
//		for (int i = 0; i < 15; i++) {
//			addObject(new JMapObject(block),i,0);
//			addObject(new JMapObject(block),i,mapsize-1);
//		}

//		gameMapRow("sssssssssssssss", 0);
//		gameMapRow("s             s", 1);
//		gameMapRow("s             s", 2);
//		gameMapRow("s             s", 3);
//		gameMapRow("s             s", 4);
//		gameMapRow("s             s", 5);
//		gameMapRow("s             s", 6);
//		gameMapRow("s             s", 7);
//		gameMapRow("s ddd    d d  s", 8);
//		gameMapRow("s d d    d d  s", 9);
//		gameMapRow("s d d    ddd  s", 10);
//		gameMapRow("s             s", 11);
//		gameMapRow("s             s", 12);
//		gameMapRow("s             s", 13);
//		gameMapRow("sssssssssssssss", 14);
	}

	public void addMapRow(String row, int rowIndex) {
		JGUIMapObject block = new JGUIMapObject(JGUIGameMap.solidBlock); 
		JGUIMapObject dblock = new JGUIMapObject(JGUIGameMap.destroyableBlock);
		
		for (int i = 0; i < row.length(); i++) {
			if (row.charAt(i) == 'd') {
				addObject(new JDestroyableBlock(dblock),i,rowIndex);
			} else if (row.charAt(i) == 's') {
				addObject(new JMapObject(block),i,rowIndex);
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
	 */
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

			if (gameMap[x+i][y] instanceof JBomb)
				((JBomb)gameMap[x+i][y]).explode();
			if (gameMap[x][y+i] instanceof JBomb)
				((JBomb)gameMap[x][y+i]).explode();
			if (gameMap[x-i][y] instanceof JBomb)
				((JBomb)gameMap[x-i][y]).explode();
			if (gameMap[x][y-i] instanceof JBomb)
				((JBomb)gameMap[x][y-i]).explode();

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		removeObject(x, y);
	}
	/**
	 * Check if a move is valid
	 * @param id id of the moving player
	 * @return true if empty
	 */
	public boolean validMove(int id, int dx, int dy) {
		int[] loc;
		if (id == playerIDs[0]) {
			loc = find(players[0].hashCode());
		} else {
			loc = find(players[1].hashCode());
		}
//		System.out.println("player id är: " + playerIDs[0] + " | " + playerIDs[1]);
//		System.out.println("loc 0 är: " +loc[0] + " dx: " +dx + " dy: " + dy);
		if (loc[0] != -1 && gameMap[loc[0]+dx][loc[1]+dy] == null) {
			return true;
		}
//		System.out.println("returnar false");
		return false;
	}

	/**
	 * Method for handling event
	 * @param event udpevent to be handled
	 */
	public void handleEvent(UDPEvent event) {
		if (event.type == UDPEvent.Type.establish_connection) {
			JPlayer player = new JPlayer(new JGUIMapObject(JGUIGameMap.superman), this);
			player.setID(event.getOriginID());
			addPlayer(player, player.getID(), 4, 4);
		}

		if (event.type == UDPEvent.Type.player_move_up) {
			move(0, -1, event.getOriginID());
		}
		if (event.type == UDPEvent.Type.player_move_down) {
			move(0, 1, event.getOriginID());
		}
		if (event.type == UDPEvent.Type.player_move_right) {
			move(1, 0, event.getOriginID());
		}
		if (event.type == UDPEvent.Type.player_move_left) {
			move(-1,0,event.getOriginID());
		}
	}





}
