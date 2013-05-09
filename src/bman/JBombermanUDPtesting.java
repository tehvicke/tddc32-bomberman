package bman;

import bman.backend.JGameMap;
import bman.backend.JHuman;
import bman.backend.JPlayer;
import bman.frontend.gui.JGUIGame;
import bman.frontend.gui.JGUIMapObject;
import bman.frontend.gui.JGUIScreen;
import bman.networking.UDPServer;
import bman.networking.UDPServerInterface;

public class JBombermanUDPtesting {

	public static void main(String[] args) {

		JGameMap gameMap = new JGameMap();
		JHuman player = 
				new JHuman(
						new JGUIMapObject(
								JGUIGame.player_front,
								JGUIGame.player_back,
								JGUIGame.superman,
								JGUIGame.player_front),
								gameMap, null);
		gameMap.addPlayer(player, 5,1,1);
		JGUIScreen guigamemap = new JGUIScreen(gameMap, player);
		JPlayer player2 = new JPlayer(new JGUIMapObject(JGUIGame.superman), gameMap, null);
		
		UDPServerInterface serv = new UDPServer(1);
		Thread t = new Thread(serv);
		t.start();
	}
}
