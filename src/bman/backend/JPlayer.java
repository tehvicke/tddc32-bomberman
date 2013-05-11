package bman.backend;

import bman.frontend.gui.JGUIMapObject;
import bman.frontend.gui.JGUIMapObject.Direction;
import bman.networking.UDPEvent;
import bman.networking.UDPEventInterface;

/**
 * The player class. Stores basic information about the players in the game.
 * @author viktordahl
 *
 */
public class JPlayer extends JMapObject {
	
	private int playerid = 0;
	

	public JPlayer(JGUIMapObject sprite) {
		super(sprite);
		super.destroyable = true;
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
}