package com.lierojava.net.handles;

import java.util.ArrayList;

import com.lierojava.client.GlobalState;
import com.lierojava.net.interfaces.IParticipantChat;
import com.lierojava.server.GlobalServerState;

public class ParticipantChat implements IParticipantChat {

	/**
	 * The ArrayList of messages, set to GlobalServerState if this is global chat, set to GlobalState if not
	 */
	private ArrayList<String> messages;
	
	/**
	 * The message we are currently at, messages before this index will not be resent
	 */
	private int messageIndex = 0;
	
	/**
	 * Default constructor
	 * @param isGlobalChat Whether this instance handles globalchat
	 */
	public ParticipantChat(boolean isGlobalChat) {
		if (isGlobalChat) {
			messages = GlobalServerState.chatMessages;
		} else {
			messages = GlobalState.chatMessages;
		}
	}
	
	/**
	 * Sends/posts a message
	 */
	@Override
	public void sendMessage(String message) {
		messages.add(message);
		
	}

	/**
	 * Gets all messages since the last call, or an empty arraylist if none were added
	 */
	@Override
	public ArrayList<String> getNewMessages() {
		//There are no new messages, return an empty arraylist
		if (messages.size() <= messageIndex) {
			return new ArrayList<String>();
		}
		//Return the new messages and set the index to the last message returned
		ArrayList<String> newMessages = new ArrayList<String>(messages.subList(messageIndex, messages.size())); 
		messageIndex = messages.size();
		return newMessages;
	}

}
