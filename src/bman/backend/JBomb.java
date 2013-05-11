package bman.backend;

import bman.frontend.gui.JGUIMapObject;

public class JBomb extends JMapObject implements Runnable {
	private int explosionRaidus = 3;
	private JGameMap map;
	private JPlayer owner;
	public static int timer = 1500;


	Thread fuse;
	protected static JFire fire = new JFire();


	/**
	 * JBomb constructor without an owner
	 * @param sprite JGUIMapobject for visual representation of the bomb
	 * @param map gameMap containing the bomb
	 */
	public JBomb(JGUIMapObject sprite, JGameMap map) {
		super(sprite);
		this.map = map;
		lightFuse();
		destroyable= true;
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
		destroyable = true;
	}
	/**
	 * Starts the fuse on the bomb (starts the countdown timer for explosion)
	 */
	private void lightFuse() {
		fuse = new Thread(this);
		fuse.start();
	}


	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean explode(int x, int y) {
		boolean retur = false;
		try {
			JMapObject temp = map.at(x,y);
			if (temp != null && !temp.isDestroyable()) {
				return false;
			} else if (temp == null) {
				retur = true;
			}


			map.destroy(x, y);

			map.addObject(JBomb.fire, x, y);
			return retur;
		} catch(ArrayIndexOutOfBoundsException e) {
			return false;
		}


	}
	public void explode() {
		int[] loc = map.find(this.hashCode());
		Boolean left = true;
		Boolean right = true;
		Boolean up = true;
		Boolean down = true;

		map.remove(loc[0],loc[1]);
		map.addObject(JBomb.fire, loc[0],loc[1]);


		for (int i = 1; i < explosionRaidus; i++) {
			if (left) {
				left = explode(loc[0]-i,loc[1]);
			}
			if (right) {
				right = explode(loc[0]+i,loc[1]);
			}
			if (up) {
				up = explode(loc[0],loc[1]-i);
			}
			if (down) {
				down = explode(loc[0],loc[1]+i);
			}
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.remove(loc[0], loc[1]);
		left = true;
		right = true;
		up = true;
		down = true;
		for (int i = 1; i < explosionRaidus ; i++) {
			if (right && map.at(loc[0]+i, loc[1]) instanceof JFire) {
				map.remove(loc[0]+i,loc[1]);
			} else {
				right = false;
			}
			if (left && map.at(loc[0]-i, loc[1]) instanceof JFire) {
				map.remove(loc[0]-i,loc[1]);
			} else {
				left = false;
			}
			if (down && map.at(loc[0], loc[1]+i) instanceof JFire) {
				map.remove(loc[0],loc[1]+i);
			} else {
				down = false;
			}
			if (up && map.at(loc[0], loc[1]-i) instanceof JFire) {
				map.remove(loc[0],loc[1]-i);
			} else {
				up = false;
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
		 fuse.interrupt();
		}
	}




