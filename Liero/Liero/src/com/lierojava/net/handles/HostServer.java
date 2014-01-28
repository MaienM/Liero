package com.lierojava.net.handles;

import com.lierojava.net.interfaces.IHostServer;
import com.lierojava.server.GlobalServerState;

/**
 * The implementation for communication from the host to the server.
 * 
 * @author Michon
 */
public class HostServer implements IHostServer {
	
	/**
	 * Checks whether a player with dbId is actually logged in
	 */
	@Override
	public boolean isLoggedinPlayer(int dbId, String host) {
		return GlobalServerState.server.isLoggedInPlayer(dbId, host);
	}
	
	/**
	 * Saves the stats of a player
	 */
	@Override
	public void savePlayerStats(int dbId, int kills, int deaths) {
		GlobalServerState.server.savePlayerStats(dbId, kills, deaths);
	}

}
