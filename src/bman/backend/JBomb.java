package bman.backend;

import bman.backend.JMapObject;
import bman.frontend.gui.JGUIMapObject;

public class JBomb extends JMapObject implements Runnable {
	private int explosionRaidus = 3;
	private JGameMap map;
	private JPlayer owner;
	public static int timer = 1000;

	//STATICS WITH FIRE PARTS
	public static JGUIMapObject JExplosion = new JGUIMapObject("./sprites/explosion.png");
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
		if (owner != null) {
			owner.detonated();
		}
		// Fire Array: {up,down,left,right}{+/- 1, +/- 2, ..} etc.
		Boolean[][] fire = new Boolean[4][explosionRaidus];
		int [] loc = map.find(this.hashCode());

		int x = loc[0];
		int y = loc[1];
		JMapObject temp;

		// Initiating fire array
		for (int i = 0; i < explosionRaidus; i++) {
			fire[0][i] = true;
			fire[1][i] = true;
			fire[2][i] = true;
			fire[3][i] = true;
		}
		//Up check
		for (int i = 0; i< explosionRaidus ; i++) {
			try {
				temp = map.at(x,y-i);
				if (temp != null && !temp.isDestroyable()) {
					fire[0][i] = false;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				fire[0][i] = false;
				break;
			}
		}
		//Down check
		for (int i = 0; i< explosionRaidus ; i++) {
			try {
				temp = map.at(x,y+i);
				if (temp != null && !temp.isDestroyable()) {
					fire[1][i] = false;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				fire[1][i] = false;
				break;
			}
		}
		//Left check
		for (int i = 0; i< explosionRaidus ; i++) {
			try {
				temp = map.at(x-i,y);
				if (temp != null && !temp.isDestroyable()) {
					fire[2][i] = false;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				fire[2][i] = false;
				break;
			}
		}
		//Right check
		for (int i = 0; i< explosionRaidus ; i++) {
			try {
				temp = map.at(x+i,y);
				if (temp != null && !temp.isDestroyable()) {
					fire[3][i] = false;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				fire[3][i] = false;
				break;
			}
		}
		//Place Fire
		//Up & Left
		JFire fireob = new JFire(JBomb.JExplosion);
		for (int i = 0; i < explosionRaidus; i++) {
			if (fire[0][i] == true) {
				map.remove(x,y-i);
				map.addObject(fireob, x, y-i);
			}
			if (fire[2][i] == true) {
				map.remove(x-i,y);
				map.addObject(fireob, x-i, y);
			}
			if (fire[1][i] == true) {
				map.remove(x,y+i);
				map.addObject(fireob, x, y+i);
			}
			if (fire[3][i] == true) {
				map.remove(x+i,y);
				map.addObject(fireob, x+i, y);
			}
		}

	}




	@Override
	public void run() {

		try {
			Thread.sleep(timer);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		explode();

	}


	public void destroy() {
		this.explode();
	}
}
