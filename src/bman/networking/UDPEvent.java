package bman.networking;

import java.io.Serializable;
import java.util.Random;

public class UDPEvent implements UDPEventInterface, Serializable {
	
	private static final long serialVersionUID = -7484069135812975169L;
	public String name = "Event";
	public int id;
	int player_id;
	public enum Type {
		// Administrative
		scan_for_open_connections,
		establish_connection,
		game_start,
		game_end,
		kick,
		
		// Player stuff
		player_move_up,
		player_move_down,
		player_move_right,
		player_move_left,
		player_move_stop,
		player_die,
		player_grid_change,
		
		// Other
		bomb_set
	};
	Type type;
	
	public UDPEvent(Type type) {
		this.type = type;
		switch(type) {
			case player_move_up: {
				System.out.println(type);
				break;
			} case player_move_down: {
				System.out.println(type);
				break;
			} case player_move_right: {
				System.out.println(type);
				break;
			} case player_move_left: {
				System.out.println(type);
				break;
			} case player_move_stop: {
				System.out.println(type);
				break;
			} case player_die: {
				System.out.println(type);
				break;
			} case player_grid_change: {
				System.out.println(type);
			} default: {
				System.out.println("Default.");
			}
		}
	}
	public UDPEvent(Type type, String[] args) {
		this.type = type;
		this.player_id = Integer.parseInt(args[0]);
		switch(type) {
		case player_move_up: {
			System.out.println(type);
			break;
		} case player_move_down: {
			System.out.println(type);
			break;
		} case player_move_right: {
			System.out.println(type);
			break;
		} case player_move_left: {
			System.out.println(type);
			break;
		} case player_move_stop: {
			System.out.println(type);
			break;
		} case player_die: {
			System.out.println(type);
			break;
		} case player_grid_change: {
			System.out.println(type);
		} default: {
			System.out.println("Default.");
		}
	}
	}
}