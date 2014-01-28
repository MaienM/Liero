package com.lierojava.net.interfaces;

import java.util.ArrayList;

import com.lierojava.server.data.HostStruct;

/**
 * The interface for communication from the participant to the server.
 * 
 * @author Michon
 */
public interface IParticipantServer {

	/**
	 * Adds a game to the current game list
	 * @param game
	 * @return 
	 */
	int addGame(HostStruct game);
	
	/**
	 * Refreshes the current game list
	 * 
	 * @return Array containing all current games
	 */
	ArrayList<HostStruct> getGames();
	int getDatabaseId();

}
