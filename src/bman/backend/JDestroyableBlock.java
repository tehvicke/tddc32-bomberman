package bman.backend;

import bman.frontend.gui.JGUIGame;

/**
 * The destroyable block class. Are blocks that can be destroyed by the bomb.
 * @author viktordahl
 *
 */
public class JDestroyableBlock extends JMapObject {

	public JDestroyableBlock() {
		super(JGUIGame.destroyableBlockGUI);
		destroyable = true;
	}

}
