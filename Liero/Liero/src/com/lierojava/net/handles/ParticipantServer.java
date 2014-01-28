package com.lierojava.net.handles;

import java.util.ArrayList;

import com.lierojava.Utils;
import com.lierojava.net.interfaces.IHostServer;
import com.lierojava.net.interfaces.IParticipantServer;
import com.lierojava.server.GlobalServerState;
import com.lierojava.server.data.HostStruct;

/**
 * The implementation for communication from the participant to the server.
 * 
 * @author Michon
 */
public class ParticipantServer implements IParticipantServer {
	public int databaseId;
	
	/**
	 * Kryonet no-arg constructor
	 */
	public ParticipantServer() {};
	
	/**
	 * Main constructor
	 * 
	 * @param conn The connection id of this instance
	 * 
	 * @param db The database Id of the player belonging to this instance
	 */
	public ParticipantServer(int dbid) {
		this.databaseId = dbid;
	}
	
	/**
	 * Refreshes the game list
	 * 
	 * @return The complete list of current games
	 */
	@Override
	public ArrayList<HostStruct> getGames() {
		return new ArrayList<HostStruct>(GlobalServerState.accountGame.values());
	}
	
	/**
	 * Adds a game to the server, if this client is not already running a game
	 * 
	 * @param game The game to add
	 * @return the id of the IHostServer object for this game
	 */
	@Override
	public int addGame(HostStruct game) {
		if (GlobalServerState.accountGame.containsKey(this.databaseId)) {
			return -1;
		}
		GlobalServerState.accountGame.put(this.databaseId, game);
		int ihpId = this.databaseId + (Integer.MAX_VALUE / 2);
		IHostServer ihs = new HostServer();
		GlobalServerState.serverObjectSpace.register(ihpId, ihs);
		Utils.print("Game Added:" + game.host);
		return ihpId;
	}
	
	/**
	 * Returns the database id of this participant
	 * @return int the id of the participant
	 */
	@Override
	public int getDatabaseId() {
		return this.databaseId;
	}
}
