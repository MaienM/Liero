package com.lierojava.net.interfaces;

import com.lierojava.server.data.AccountState;

/**
 * The interface for handshakes with the server.
 * 
 * @author Michon
 */
public interface IServerHandshake {
	public int login(String username, String password);
	public AccountState register(String username, String password);

}
