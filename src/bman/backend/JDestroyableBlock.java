package bman.backend;

import bman.frontend.gui.JGUIGame;

public class JDestroyableBlock extends JMapObject {

	public JDestroyableBlock() {
		super(JGUIGame.destroyableBlockGUI);
		destroyable = true;
	}

}
