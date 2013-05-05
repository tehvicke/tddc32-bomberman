package bman.backend;

import bman.backend.JMapObject;
import bman.frontend.gui.JGUIMapObject;

public class JBomb extends JMapObject implements Runnable {
	private int explosionRaidus = 2;
	private float damage=(float) 1.0;
	private float timer=(float) 2.0;
	private JGameMap map;
	private JPlayer owner;

	public JBomb(JGUIMapObject sprite,JGameMap map, JPlayer owner) {
		super(sprite);
		this.map = map;
		this.owner = owner;
	}
	public void explode(){
		int [] loc = map.find(this.hashCode());
		if (loc[0] != -1) {
		owner.detonated();
		map.remove(this);
		map.explosion(loc[0],loc[1],explosionRaidus);
		}


	}
	@Override
	public void run() {
		System.out.println("kördes igång");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		explode();

	}
}
