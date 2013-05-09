package bman.backend;

import bman.frontend.gui.JGUIGame;


public class JSolidBlock extends JMapObject{
	private int durability;
	private enum type{};
	private boolean blocksPath=true;
	

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public JSolidBlock() {
		super(JGUIGame.solidBlockGUI);
	}
}
