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

	public JClient(String ip) {
		this.serverIP = ip;
		client = new UDPClient(ip);
		id = client.hashCode();
		System.out.println(this);

	}

	public String getName() {
		return playername;
	}

	public void UDPEventHandler(UDPEvent event) {
		System.err.println("Event handled: " + event.toString());
		
		if (event.type == UDPEventInterface.Type.player_move) {
			String [] args = event.getArguments();
			movePlayer(event.getOriginID(), Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		}
		if (event.type == UDPEventInterface.Type.game_start) {
			startGame();

			/* Positionen */
			String[] args = {Integer.toString(3), Integer.toString(3)};		
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
			movePlayerRelative(event.getOriginID(), 0, -1);
		}
		if (event.type == UDPEventInterface.Type.player_move_down) {
			movePlayerRelative(event.getOriginID(), 0, 1);
		}
		if (event.type == UDPEventInterface.Type.player_move_left) {
			movePlayerRelative(event.getOriginID(), -1, 0);
		}
		if (event.type == UDPEventInterface.Type.player_move_right) {
			movePlayerRelative(event.getOriginID(), 1, 0);
		}
		if (event.type == UDPEventInterface.Type.game_map) {
			System.out.println("LOL");
			String[] args = event.getArguments();
			for (int i = 0; i < 15; i++) {
				gameMap.addMapRow(args[i], i);
				System.out.println(args[i] + ", " + i);
			}
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
		String[] arg = {Integer.toString(x),Integer.toString(y)};
		client.sendEvent(new UDPEvent(Type.bomb_set, this.id,arg));
	}

	protected void sendMoveRelative(int dx, int dy) {
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
	
	protected void sendMove(int dx, int dy) {
		if (!gameMap.validMove(this.id, dx, dy)) {
			return;
		}
		int [] loc = gameMap.find(player.hashCode());
		String[] arg = {Integer.toString(loc[0]+dx),Integer.toString(loc[1]+dy)};
		client.sendEvent(new UDPEvent(UDPEventInterface.Type.player_move, this.id,arg));
		
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

	private void movePlayerRelative(int id, int dx, int dy) {
		gameMap.move(dx, dy, id);
	}
	
	private void movePlayer(int id, int x, int y) {
		
		if (gameMap.at(x, y) != null) {
			return;
		}
		
		if (id == this.id) {
			gameMap.remove(player);
			gameMap.addObject(player, x, y);
		} else {
			gameMap.remove(player_2);
			gameMap.addObject(player_2, x, y);
		}
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
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


	}


}

