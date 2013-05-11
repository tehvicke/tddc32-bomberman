package bman.backend;

import bman.frontend.gui.JGUIGame;


public class JSolidBlock extends JMapObject{
	
	

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public JSolidBlock() {
		super(JGUIGame.solidBlockGUI);
		this.destroyable = false;
	}
}
