package bman.networking;

/**
 * This is how communication is done. Send it with an object of this class.
 * @author viktordahl
 *
 */
public interface UDPEventInterface {
	
	/**
	 * The type of event.
	 * @author viktordahl
	 *
	 */
	public enum Type {
		// Administrative
//		scan_for_open_connections,
		establish_connection,
		game_start,
		player_join,
		game_end,
		kick,
		win,

		// Player stuff
		player_move_up,
		player_move_down,
		player_move_right,
		player_move_left,
//		player_move_stop,
		player_die,
		player_grid_change,

		// Other
		bomb_set,
		bomb_explode,
		misc,
//		add_object
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
