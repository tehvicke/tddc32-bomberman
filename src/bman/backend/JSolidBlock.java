package bman.backend;

import bman.frontend.gui.JGUIGame;

/**
 * The solid block class. Are the solid blocks that cannot be destroyed.
 * @author viktordahl
 *
 */
public class JSolidBlock extends JMapObject{

	/**
	 * 
	 */
	public JSolidBlock() {
		super(JGUIGame.solidBlockGUI);
		this.destroyable = false;
	}
}
