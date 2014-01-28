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
	 * @param host The host where the game is running
	 * 
	 * @param port The port where the game is running
	 * 
	 * @param name The name where the game is running
	 */
	public HostStruct(String host, int port, String name) {
		this.host = host;
		this.port = port;
		this.name = name;
	}
}
