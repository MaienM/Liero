package com.lierojava.net.handshake;

import com.lierojava.GlobalState;
import com.lierojava.Utils;
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
	public int requestParticipant(boolean isPlayer) {
		if (isPlayer) {
			Utils.print("Creating p");
			Player p = new Player();
			Utils.print("adding p");
			GlobalState.currentGame.players.add(p);
			Utils.print("Creating iph");
			ParticipantHost ph = new ParticipantHost(p);
			Utils.print("Created iph");
			ph.connection = GlobalState.lastSender;
			GlobalState.objectSpace.register(++GlobalState.objectSpaceIndex, ph);
			return GlobalState.objectSpaceIndex;
		}

		return -1;
	}
}
