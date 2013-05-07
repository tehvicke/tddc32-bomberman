package bman.networking;

import java.io.Serializable;
import java.net.InetAddress;

public class UDPEvent implements UDPEventInterface, Serializable {

	private static final long serialVersionUID = -7484069135812975169L;
	
	/**
	 * The origin from where the event is sent.
	 */
	private int origin;
	
	/**
	 * The arguments
	 */
	private String[] arguments;
	
	/**
	 * The event type
	 */
	public Type type;

	/**
	 * Default constructor
	 * @param type The type of event
	 * @param player_id The origin from where the event is sent. 0 if server.
	 */
	public UDPEvent(Type type, int player_id) {
		this.type = type;
		this.origin = player_id;
	}
	
	/**
	 * Constructor with arguments
	 * @param type The type of event
	 * @param player_id The origin from where the event is sent. 0 if server
	 * @param args Arguments needed for the event type
	 */
	public UDPEvent(Type type, int player_id, String[] args) {
		this.type = type;
		this.origin = player_id;
		this.arguments = args;
	}
	
	public int getOriginID() {
		return this.origin;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public String[] getArguments() {
		if (this.arguments != null && this.arguments.length > 0) {
			return this.arguments;
		}
		return null;
	}
}