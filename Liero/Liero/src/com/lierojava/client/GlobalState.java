package com.lierojava.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.lierojava.enums.GameState;

public class GlobalState {
	/**
	 * The current gamestate.
	 */
	public static GameState gameState;
	
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
}
