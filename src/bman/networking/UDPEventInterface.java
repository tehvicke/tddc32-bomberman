package bman.networking;

import java.io.Serializable;

/**
 * This is how communication is done. Send it with an object of this class.
 * Is serializible for making it sendable through UDP. Contains info about
 * the origin and everything the server and client need to act properly.
 * @author Viktor Dahl
 */
public interface UDPEventInterface extends Serializable {
	
	/**
	 * The type of event.
	 * @author viktordahl
	 */
	public enum Type {
		// Administrative
//		scan_for_open_connections,
		establish_connection,
		game_start,
		game_end,
		game_map,
		kick,
		is_alive,

		// Player stuff
		player_join,
		player_move,
		player_die,
		player_leave,
		player_win,
		player_turn,
		// Other
		bomb_set,
		bomb_explode,
		misc,
	};
	
	/**
	 * 
	 * @return The origin from where the event is sent.
	 */
	public int getOriginID();
	
	/**
	 * 
	 * @return The event type.
	 */
	public Type getType();
	
	/**
	 * 
	 * @return The arguments passed.
	 */
	public String[] getArguments();
}
