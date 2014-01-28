package com.lierojava.net.handshake;

import com.lierojava.client.GlobalState;
import com.lierojava.net.handles.ParticipantHost;
import com.lierojava.net.interfaces.IHostHandshake;
import com.lierojava.participants.Player;

/**
 * The implementation for handshakes with hosts.
 * 
 * @author Michon
 */
public class HostHandshake implements IHostHandshake {

	@Override
	public int requestParticipant(boolean isPlayer, int playerID) {
		if (isPlayer) {
			Player p = new Player(playerID);
			GlobalState.currentGame.players.add(p);
			GlobalState.currentGame.stats.put(playerID, p.stats);
			ParticipantHost ph = new ParticipantHost(p);
			ph.connection = GlobalState.lastSender;
			GlobalState.objectSpace.register(++GlobalState.objectSpaceIndex, ph);
			return GlobalState.objectSpaceIndex;
		}
		else {
			return -1;
		}
	}
}
