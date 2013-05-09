package bman;

import bman.backend.JGameMap;
import bman.backend.JHuman;
import bman.backend.JPlayer;
import bman.frontend.gui.JGUIGameMap;
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
								JGUIGameMap.player_front,
								JGUIGameMap.player_back,
								JGUIGameMap.superman,
								JGUIGameMap.player_front),
								gameMap, null);
		gameMap.addPlayer(player, 5,1,1);
		JGUIScreen guigamemap = new JGUIScreen(gameMap, player);
		JPlayer player2 = new JPlayer(new JGUIMapObject(JGUIGameMap.superman), gameMap, null);
		
		UDPServerInterface serv = new UDPServer(1,gameMap);
		Thread t = new Thread(serv);
		t.start();
	}
}
