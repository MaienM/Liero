package com.lierojava.net.interfaces;

import java.util.ArrayList;

/**
 * Handles chat
 * @author Subhi
 *
 */
public interface IChat {
	/**
	 * Returns all messages since the last request
	 * @return ArrayList<String> of messages, or an empty ArrayList<String> if none were added
	 */
	public ArrayList<String> getNewMessages();
	
	/**
	 * Adds a message to the messagelist
	 * @param message The message to add
	 */
	void sendMessage(String message);
}
