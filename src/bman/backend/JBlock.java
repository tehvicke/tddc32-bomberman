package bman.backend;

import bman.backend.JMapObject;
import bman.frontend.gui.JGUIMapObject;

public class JBlock extends JMapObject{
	private int durability;
	private enum type{};
	private boolean blocksPath=true;
	

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public JBlock(JGUIMapObject sprite) {
		super(sprite);
	}
}
