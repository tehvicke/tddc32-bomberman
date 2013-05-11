package bman.backend;

import java.util.ArrayList;
import java.util.Random;

import bman.JBomberman;
import bman.frontend.gui.JGUIGame;
import bman.frontend.gui.JGUIScreen;
import bman.networking.UDPClient;
import bman.networking.UDPEvent;
import bman.networking.UDPEventInterface;
import bman.networking.UDPEventInterface.Type;

/**
 * This is the client and handles all the calculations. The reason for having
 * the calculations on client side is for minimizing the network utilization. 
 * It’s run in its separate thread and uses a UDPClient to get and send 
 * instructions to the server
 * @author viktordahl
 *
 */
public class JClient implements Runnable{
	
	private UDPClient client;
	private JGameMap gameMap;
	private JGUIScreen guiScreen;
	private JHuman player;
	private JPlayer player_2;
	private int id;
	
	private ArrayList<Integer> players = new ArrayList<Integer>();
	
	/**
	 * Constructor with IP argument
	 * @param ip IP address of the server.
	 */
	public JClient(String ip,JGUIScreen guiScreen) {
		this.guiScreen = guiScreen;
		client = new UDPClient(ip);
		id = client.hashCode();
		System.out.println(this);

	}


	/**
	 * Function which handles Events broadcasted from the server
	 * @param event event to be handled
	 */
	public void UDPEventHandler(UDPEvent event) {
		if (JBomberman.debug) {
			System.err.println("Event handled: " + event.toString());
		}
		
		if (event.type == UDPEventInterface.Type.player_move) {
			String [] args = event.getArguments();
			movePlayer(event.getOriginID(), Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		} else if (event.type == UDPEventInterface.Type.game_start) {
			startGame();
		} else if (event.type == UDPEventInterface.Type.game_end) {
			//endGame();
		} else if (event.type == UDPEventInterface.Type.bomb_set) {
			String [] args = event.getArguments();
			gameMap.addObject(new JBomb(JGUIGame.bomb, gameMap), Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		} else if (event.type == UDPEventInterface.Type.player_join) {
			String[] arg = event.getArguments();
			addPlayer(event.getOriginID(),Integer.parseInt(arg[0]),Integer.parseInt(arg[1]));
		} else if (event.type == UDPEventInterface.Type.game_map) {
			String[] args = event.getArguments();
			for (int i = 0; i < 15; i++) {
				gameMap.addMapRow(args[i], i);
			}
			randomizePlayerPosition();
			
		} else if (event.type == UDPEventInterface.Type.player_die) {
			if (event.getOriginID() == client.hashCode()) {
				System.err.println("Du dog! :(");
			}
		} else if (event.type == UDPEventInterface.Type.player_win) {
			System.out.println("Du vann!");
		}
	}
	
	/**
	 * Adds the player on a random place on the map. Exits game if no place is found.
	 */
	private void randomizePlayerPosition() {
		
		/* 
		 * Check if there's a free space for the player on the grid. 
		 * This function can be more advanced to calculate a place where
		 * the player can lay a bomb without committing suicide. 
		 */
		int count = 0;
		for (int i = 0; i < JGameMap.mapsize; i++) {
			for (int j = 0; j < JGameMap.mapsize; j++) {
				if (this.gameMap.at(i, j) == null) {
					count++;
				}
			}
		}
		if (count == 0) {
			System.err.println("Nowhere to put player " + player.getID() + ". Exits game.");
			client.sendEvent(new UDPEvent(UDPEventInterface.Type.player_leave, client.hashCode()));
			System.exit(0);
		}
		
		
		/* Random position */
		Random gen = new Random();
		int x_rand;
		int y_rand;
		while(true) {
			x_rand = gen.nextInt(JGameMap.mapsize);
			y_rand = gen.nextInt(JGameMap.mapsize);
			if (this.gameMap.at(x_rand, y_rand) == null) {
				String[] argss = {Integer.toString(x_rand), Integer.toString(y_rand)};
				client.sendEvent(new UDPEvent(Type.player_join, this.id, argss));
				break;
			}
		}
		if (JBomberman.debug) {
			System.out.println("Klient: Player " + this.id + " added at (" + x_rand + ", " + y_rand + ").");
		}
	}
	/**
	 * Creates a player with id at specified location
	 * @param id2
	 * @param x
	 * @param y
	 */
	private void addPlayer(int id, int x, int y) {
		System.out.println("x:" + x + " y:" + y);
		if (id == this.id) {
			gameMap.addPlayer(player, id, x, y);
		} else {
			player_2 = new JPlayer(JGUIGame.player2,gameMap,this);
			gameMap.addPlayer(player_2, id, x, y);
		}

	}


	protected void putBomb(int x, int y) {
		if (gameMap.validMove(x, y)) {
			String[] arg = {Integer.toString(x),Integer.toString(y)};
			client.sendEvent(new UDPEvent(Type.bomb_set, this.id,arg));
		}
	}

	/**
	 * Sends an UDPEvent containing absolute move information for a player to the server
	 * @param dx relative x position to be moved
	 * @param dy relative y position to be moved
	 */
	protected void sendMove(int dx, int dy) {
		if (!gameMap.validMove(this.id, dx, dy)) {
			return;
		}
		int [] loc = gameMap.find(player.hashCode());
		String[] arg = {Integer.toString(loc[0]+dx),Integer.toString(loc[1]+dy)};
		client.sendEvent(new UDPEvent(UDPEventInterface.Type.player_move, this.id,arg));

	}

	/**
	 * Starts the game and initiates the gameMap, the player and the GUI
	 */
	private void startGame() {
		gameMap = new JGameMap();
		player = new JHuman(JGUIGame.player1, gameMap,this);
		guiScreen.removeContent();
		guiScreen.addContent(new JGUIGame(gameMap, player));
	}

	/**
	 * function for moving a player, sends absolute coordinates instead of relative to keep clients synced
	 * @param id player to be moved
	 * @param x x location to move to
	 * @param y y location to move to
	 */
	private void movePlayer(int id, int x, int y) {
		if (x < 0 || x > JGameMap.mapsize || y < 0 || y > JGameMap.mapsize || gameMap.at(x, y) != null) {
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
		Thread clientThread = new Thread(client);
		clientThread.start();
		while(true) {  /* NOTE: This is done with busy wait (polling) and are thus CPU inefficient.
		 				* This would have been changed but there wasn't time.
		 				*/
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
	
	/**
	 * 
	 * @return The UDP Client
	 */
	public UDPClient getUDPClient() {
		return client;
	}
}

