package bman.networking;

import java.io.Serializable;

public class UDPEvent implements UDPEventInterface, Serializable {
	public String name = "Event!";
	public int id = 24;
	
	public UDPEvent() {
		
	}
	
	public UDPEvent(byte[] bytes) {
		
	}
}
