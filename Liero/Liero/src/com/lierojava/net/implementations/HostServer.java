package com.lierojava.net.implementations;

import com.lierojava.net.interfaces.IHostServer;
import com.lierojava.server.GlobalServerState;

/**
 * The implementation for communication from the host to the server.
 * 
 * @author Michon
 */
public class HostServer implements IHostServer {
	@Override
	public boolean isLoggedinPlayer(int dbId, String host) {
		return GlobalServerState.server.isLoggedInPlayer(dbId, host);
	}
	
	@Override
	public void savePlayerStats(int dbId, int kills, int deaths) {
		GlobalServerState.server.savePlayerStats(dbId, kills, deaths);
	}
}
