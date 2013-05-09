package bman.backend;

import bman.frontend.gui.JGUIGameMap;


public class JBlock extends JMapObject{
	private int durability;
	private enum type{};
	private boolean blocksPath=true;
	

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public JBlock() {
		super(JGUIGameMap.solidBlockGUI);
	}
}
