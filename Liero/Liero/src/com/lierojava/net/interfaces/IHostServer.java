package com.lierojava.net.interfaces;

/**
 * The interface for communication from the host to the server.
 * 
 * @author Michon
 */
public interface IHostServer {

	boolean isLoggedinPlayer(int dbId, String string);

	void savePlayerStats(int dbId, int kills, int deaths);

}
