package bman.networking;

import java.io.Serializable;
import java.net.InetAddress;

public class UDPEvent implements UDPEventInterface, Serializable {

	private static final long serialVersionUID = -7484069135812975169L;
	public String name = "Event";
	public int player_id;
	String[] things;
	public enum Type {
		// Administrative
		scan_for_open_connections,
		establish_connection,
		game_start,
		game_end,
		kick,
		win,

		// Player stuff
		player_move_up,
		player_move_down,
		player_move_right,
		player_move_left,
		//player_move_stop,
		player_die,
		player_grid_change,

		// Other
		bomb_set,
		bomb_explode,
		misc
	};
	public Type type;

	public UDPEvent(Type type, int player_id) {
		this.type = type;
		this.player_id = player_id;
	}
	
	/**
	 * Constructor for arguments
	 * @param type
	 * @param args
	 */
	public UDPEvent(Type type, int player_id, String[] args) {
		this.type = type;
		this.player_id = player_id;
		this.things = args;
	}
	
	public static void sendEvent(UDPEvent event, InetAddress ipaddr) {
		
	}
}