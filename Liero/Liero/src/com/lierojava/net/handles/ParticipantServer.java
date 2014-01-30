package com.lierojava.net.handles;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.esotericsoftware.kryonet.Connection;
import com.lierojava.net.interfaces.IHostServer;
import com.lierojava.net.interfaces.IParticipantServer;
import com.lierojava.server.GlobalServerState;
import com.lierojava.server.data.HostStruct;
import com.lierojava.server.database.Account;

/**
 * The implementation for communication from the participant to the server.
 * 
 * @author Michon
 */
public class ParticipantServer extends Chat implements IParticipantServer {
	/*
	 * The database id of the player
	 */
	public int databaseId;
	
	/**
	 * The name of the player
	 */
	public String name;
	
	/**
	 * Kryonet no-arg constructor
	 */
	public ParticipantServer() {
		super(true);
	}
	
	/**
	 * Main constructor
	 * 
	 * @param conn The connection id of this instance
	 * 
	 * @param db The database Id of the player belonging to this instance
	 */
	public ParticipantServer(int dbid, String name) {
		this();
		this.databaseId = dbid;
		this.name = name;
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
		// Determine the host.
		for (Entry<Connection, Account> entry : GlobalServerState.connectionAccounts.entrySet()) {
			if (entry.getValue().getId() == this.databaseId) {
				game.host = entry.getKey().getRemoteAddressTCP().getAddress().getHostAddress();
			}
		}
		GlobalServerState.accountGame.put(this.databaseId, game);
		int ihpId = this.databaseId + (Integer.MAX_VALUE / 2);
		IHostServer ihs = new HostServer();
		GlobalServerState.serverObjectSpace.register(ihpId, ihs);
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

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public void sendMessage(String message) {
		super.sendMessage(this.name + "> " + message);
	}
}
