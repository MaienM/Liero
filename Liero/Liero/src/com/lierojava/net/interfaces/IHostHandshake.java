package com.lierojava.net.interfaces;


/**
 * The interface for handshakes with hosts.
 * 
 * @author Michon
 */
public interface IHostHandshake {
	/**
	 * Perform a handshake between a participant and the host. 
	 * @param isPlayer True if the participant wants to play, false otherwise.
	 * @param playerID The ID of the player that wants to play. May be invalid for non-players.
	 * @return The index of the IParticipantHost object (for communication), if successful.
	 */
	public int requestParticipant(boolean isPlayer, int playerID);
}
