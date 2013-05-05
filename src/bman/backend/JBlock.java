package bman.backend;

import bman.backend.JMapObject;
import bman.frontend.gui.JGUIMapObject;

public class JBlock extends JMapObject{
	private boolean isDestroyable=true;
	private int durability;
	private enum type{};
	private boolean blocksPath=true;
	
	public void destroy(){
		
	}
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public JBlock(JGUIMapObject sprite) {
		super(sprite);
	}
}
