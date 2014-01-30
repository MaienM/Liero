package com.lierojava.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.lierojava.net.interfaces.IParticipantServer;

public class GlobalState {
	/**
	 * The currently running game.
	 */
	public static MainGame currentGame;
	
	/**
	 * The current objectspace.
	 */
	public static ObjectSpace objectSpace;
	
	/**
	 * The next free objectspace index.
	 */
	public static int objectSpaceIndex = 0;
	
	/**
	 * The last connection that sent us something.
	 */
	public static Connection lastSender;
	
	/**
	 * The handle to the global server.
	 */
	public static IParticipantServer ips;
}
