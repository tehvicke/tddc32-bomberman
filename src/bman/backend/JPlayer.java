package bman.backend;

import bman.frontend.JGUIMapObject;

/**
 * The player class. Stores basic information about the players in the game.
 * @author viktordahl
 *
 */
public class JPlayer extends JMapObject {
	
	/**
	 * The ID of the player
	 */
	private int playerid = 0;
	
	/**
	 * 
	 * @param sprite The sprite for the player
	 */
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