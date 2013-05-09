package bman.backend;

import bman.backend.JMapObject;
import bman.frontend.gui.JGUIMapObject;

public class JBomb extends JMapObject implements Runnable {
	private int explosionRaidus = 2;
	private JGameMap map;
	private JPlayer owner;
	public static int timer = 1000;
	
	/**
	 * JBomb constructor without an owner
	 * @param sprite JGUIMapobject for visual representation of the bomb
	 * @param map gameMap containing the bomb
	 */
	public JBomb(JGUIMapObject sprite, JGameMap map) {
		super(sprite);
		this.map = map;
		lightFuse();
	}
	
	/**
	 * Donstructor for an own bomb, which is not used for 2 players but added for possible scalability in the future
	 * @param sprite JGUIMapObject to be associated with the JBomb (the visual representation)
	 * @param map JGameMap which will contain the bomb
	 * @param owner the JPlayer who put the bomb
	 */
	public JBomb(JGUIMapObject sprite,JGameMap map, JPlayer owner) {
		super(sprite);
		this.map = map;
		this.owner = owner;
		lightFuse();
	}
	/**
	 * Starts the fuse on the bomb (starts the countdown timer for explosion)
	 */
	private void lightFuse() {
		Thread bomb = new Thread(this);
		bomb.start();
	}
	public void explode(){
		int [] loc = map.find(this.hashCode());
		if (loc[0] != -1) {
		//owner.detonated();
		map.remove(this);
		map.explosion(loc[0],loc[1],explosionRaidus);
		}


	}
	@Override
	public void run() {
		
		try {
			Thread.sleep(timer);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		explode();

	}
}
