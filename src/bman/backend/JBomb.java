package bman.backend;

import bman.backend.JMapObject;
import bman.frontend.gui.JGUIMapObject;

public class JBomb extends JMapObject {
	private int explosionRaidus;
	private float damage=(float) 1.0;
	private float timer=(float) 2.0;
	private JGameMap map;
	
	public JBomb(JGUIMapObject sprite,JGameMap map) {
		super(sprite);
		this.map = map;
	}
	public void explode(){
		map.remove(this);
	}
}
