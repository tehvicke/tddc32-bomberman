package bman.backend;

import java.util.Scanner;

import bman.frontend.gui.JGUIGameMap;
import bman.frontend.gui.JGUIMapObject;
import bman.frontend.gui.JGUIScreen;
import bman.networking.UDPClient;
import bman.networking.UDPEvent;
import bman.networking.UDPEvent.Type;

public class JClient implements Runnable{
	private String playername;
	private UDPClient client;
	private String serverIP;
	private JGameMap gameMap;
	private JGUIScreen guiScreen;
	private JHuman player;
	private int id;

	public JClient(String ip, String playername) {
		this.playername = playername;
		this.serverIP = ip;
		client = new UDPClient(ip);
		id = client.hashCode();

	}



	public String getName(){
		return playername;
	}

	public void UDPEventHandler(UDPEvent event) {
		if (event.type == Type.game_start) {
			startGame();
		}
		if (event.type == Type.game_end) {
			//endGame();
		}
	}

	private void startGame() {
		gameMap = new JGameMap();
		player = new JHuman(new JGUIMapObject(JGUIGameMap.superman), gameMap);
		guiScreen = new JGUIScreen(gameMap, player);
		guiScreen.toFront();
		guiScreen.repaint();
		gameMap.addPlayer(player, id, 1, 1);

	}



	@Override
	public void run() {
		client.establishConnection(serverIP);
		startGame();
		//		while(true) {
		//			if (client.eventExists()) {
		//				UDPEventHandler(client.getEvent());
		//			}
		//		}


	}


}

