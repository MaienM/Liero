package com.lierojava.net.handshake;

import com.lierojava.enums.AccountState;
import com.lierojava.net.interfaces.IServerHandshake;
import com.lierojava.server.GlobalServerState;

/**
 * The implementation for handshakes with the server.
 * 
 * @author Michon
 */
public class ServerHandshake implements IServerHandshake {
	/**
	 * Kryonet no-arg constructor
	 */
	public ServerHandshake() {}
	
	/**
	 * Tries to login the player
	 * @param username The player's username
	 * @param password The player's password
	 * @return AccountState.SUCCESS if the login was successful, AccountState.INVALIDCREDENTIALS otherwise
	 */
	@Override
	public int login(String username, String password) {
		return GlobalServerState.server.login(username, password);
	}
	
	/**
	 * Tries to register a new account
	 * 
	 * @param username The username to use
	 * @param password The password to use
	 * @return USERNAMETAKEN if the username is unavailable, SUCCESS if everything went allright, FAILURE if something went wrong
	 */
	@Override
	public AccountState register(String username, String password) {
		if (GlobalServerState.server.isUsernameTaken(username)) {
			return AccountState.USERNAMETAKEN;
		} 
		if (GlobalServerState.server.register(username, password)) {
			return AccountState.SUCCESS;
		}
		return AccountState.FAILURE;
	}

}
