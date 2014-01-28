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
	public int addGame(HostStruct game);
	
	/**
	 * Refreshes the current game list
	 * 
	 * @return Array containing all current games
	 */
	public ArrayList<HostStruct> getGames();
	
	/**
	 * Returns the database id of the player
	 * @return int the databaseId
	 */
	public int getDatabaseId();
	
	/**
	 * Returns the name of the player
	 * 
	 * @return String the name
	 */
	public String getName();

	/**
	 * Gets the index of the chat object for this player
	 * 
	 * @return the index of the chat object
	 */
	public int getChatInstance();
}
