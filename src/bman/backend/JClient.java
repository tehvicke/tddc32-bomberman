package bman.backend;

import bman.frontend.gui.JGUIGameMap;
import bman.frontend.gui.JGUIScreen;
import bman.networking.UDPClient;
import bman.networking.UDPEvent;
import bman.networking.UDPEventInterface;
import bman.networking.UDPEventInterface.Type;

public class JClient implements Runnable{
	private String playername;
	private UDPClient client;
	private String serverIP;
	private JGameMap gameMap;
	private JGUIScreen guiScreen;
	private JHuman player;
	private JPlayer player_2;
	private int id;
	private int player2ID = 0;

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
		if (event.type == UDPEventInterface.Type.game_start) {
			startGame();

			/* Positionen */
			String[] args = {Integer.toString(2), Integer.toString(10)};		
			client.sendEvent(new UDPEvent(Type.player_join, this.id, args));


		}
		if (event.type == UDPEventInterface.Type.game_end) {
			//endGame();
		}

		if (event.type == UDPEventInterface.Type.bomb_set) {
			String [] args = event.getArguments();
			JBomb bomb;
			if (event.getOriginID() == id) {
				bomb = new JBomb(JGUIGameMap.bomb, gameMap, player);
				gameMap.addObject(bomb, Integer.parseInt(args[0]),Integer.parseInt(args[1]));
			} else {
				bomb = new JBomb(JGUIGameMap.bomb, gameMap, player_2);
				gameMap.addObject(bomb, Integer.parseInt(args[0]),Integer.parseInt(args[1]));
			}
			Thread bombThread = new Thread(bomb);
			bombThread.start();
		}
		if (event.type == UDPEventInterface.Type.player_join) {
			String[] arg = event.getArguments();
			addPlayer(event.getOriginID(),Integer.parseInt(arg[0]),Integer.parseInt(arg[1]));
		}
		if (event.type == UDPEventInterface.Type.player_move_up) {
			movePlayer(event.getOriginID(), 0, -1);
		}
		if (event.type == UDPEventInterface.Type.player_move_down) {
			movePlayer(event.getOriginID(), 0, 1);
		}
		if (event.type == UDPEventInterface.Type.player_move_left) {
			movePlayer(event.getOriginID(), -1, 0);
		}
		if (event.type == UDPEventInterface.Type.player_move_right) {
			movePlayer(event.getOriginID(), 1, 0);
		}
		switch (event.type){
		case game_map:
			
		}
	}
	/**
	 * Creates a player with id at specified location
	 * @param id2
	 * @param x
	 * @param y
	 */
	private void addPlayer(int id, int x, int y) {
		if (id == this.id) {
			gameMap.addPlayer(player, id, x, y);
		} else {
			player2ID = id;
			player_2 = new JPlayer(JGUIGameMap.player2,gameMap,this);
			gameMap.addPlayer(player_2, id, x, y);
		}

	}

	protected void putBomb(int x, int y) {
		client.sendEvent(new UDPEvent(Type.bomb_set, this.id));
	}

	protected void sendMove(int dx, int dy) {
		if (!gameMap.validMove(this.id,dx,dy)) {
			System.out.println("klient har id: " + this.id);
			return;
		}
		if (dx > 0) {
			client.sendEvent(new UDPEvent(Type.player_move_right, this.id));
		}
		if (dx < 0) {
			client.sendEvent(new UDPEvent(Type.player_move_left, this.id));
		}
		if (dy > 0) {
			client.sendEvent(new UDPEvent(Type.player_move_down, this.id));
		}
		if (dy < 0) {
			client.sendEvent(new UDPEvent(Type.player_move_up, this.id));
		}
	}



	/**
	 * Creates a game
	 * @param x Starting x position
	 * @param y Starting y position
	 */
	private void startGame() {
		gameMap = new JGameMap();
		player = new JHuman(JGUIGameMap.player1, gameMap,this);
		guiScreen = new JGUIScreen(gameMap, player);

	}

	private void movePlayer(int id, int dx, int dy) {
		gameMap.move(dx, dy, id);
	}



	@Override
	public void run() {
		//		client.establishConnection(serverIP);


		Thread clientThread = new Thread(client);
		clientThread.start();


		//Test code
		//		UDPEventHandler(new UDPEvent(UDPEventInterface.Type.game_start, 0));
		//		String [] apa = {"1","1"};
		//		UDPEventHandler(new UDPEvent(UDPEventInterface.Type.player_join,this.id,apa));
		//		for (int i = 0; i < 3 ; i++) {
		//			UDPEventHandler(new UDPEvent(UDPEventInterface.Type.player_move_right, this.id));
		//		}
		while(true) {
			if (client.eventExists()) {
				UDPEventHandler(client.getEvent());
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


	}


}

