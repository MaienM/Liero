package com.lierojava.net.interfaces;

/**
 * The interface for communication from the host to the server.
 * 
 * @author Michon
 */
public interface IHostServer {
	/**
	 * Check whether a player is logged in from a certain host.
	 * @param dbId The player ID.
	 * @param host The host.
	 * @return True is the player is logged in.
	 */
	public boolean isLoggedinPlayer(int dbId, String host);

	/**
	 * Save player stats.
	 * @param dbId The player ID.
	 * @param kills The number of kills.
	 * @param deaths The number of deaths.
	 */
	public void savePlayerStats(int dbId, int kills, int deaths);
}
