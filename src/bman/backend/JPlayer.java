package bman.backend;

import bman.frontend.gui.JGUIMapObject;
import bman.frontend.gui.JGUIMapObject.Direction;
import bman.networking.UDPEvent;
import bman.networking.UDPEventInterface;

/**
 * 
 * @author viktordahl
 *
 */
public class JPlayer extends JMapObject {
	private int playerid = 0;
	JGameMap map;
	JClient client;
	int [] lastMove = {0,0};

	public JPlayer(JGUIMapObject sprite,JGameMap map, JClient client) {
		super(sprite);
		this.map = map;
		this.client = client;
		destroyable = true;
	}

	/**
	 * 
	 * @param playerid The ID of the player
	 */
	public void setID(int playerid){
		this.playerid = playerid;
	}

	/**
	 * 
	 * @return The ID of the player
	 */
	public int getID() {
		return playerid;
	}
	/**
	 * Pubts a bomb next to the player
	 */
	public void putBomb() {
		int[] loc = map.find(this.hashCode());
		client.putBomb(loc[0]+lastMove[0], loc[1]+lastMove[1]);
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
		client.sendMove(dx, dy);
	}

	
	public void destroy() {
		if (this instanceof JHuman) {
			client.getUDPClient().sendEvent(
					new UDPEvent(
							UDPEventInterface.Type.player_die, 
							client.getUDPClient().hashCode()));
		} else {
			System.err.println("Jag dog inte!!");
		}
	}

	/**
	 * Allows the player to turn without moving, allowing him to lay bombs
	 * in different directions.
	 * @param dx x coord
	 * @param dy y coord
	 */
	public void turn(int dx, int dy) {
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
	}
	
}