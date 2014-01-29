package com.lierojava.server.data;

public class HostStruct {
	/**
	 * The host this game runs on
	 */
	public String host;
	
	/**
	 * The port this game runs on
	 */
	public int port;
	
	/**
	 * The name of the player running this game
	 */
	public String name;

	/**
	 * Kryonet no-arg constructor
	 */
	public HostStruct() {}
	
	/**
	 * The main constructor for this HostStruct object
	 * 
	 * @param port The port where the game is running
	 * @param name The name of the game
	 */
	public HostStruct(int port, String name) {
		this.port = port;
		this.name = name;
	}
}
