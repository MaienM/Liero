package com.lierojava.net.handshake;

import java.util.ArrayList;

import com.lierojava.client.GlobalState;
import com.lierojava.net.handles.ParticipantHost;
import com.lierojava.net.interfaces.IHostHandshake;
import com.lierojava.participants.Player;
import com.lierojava.weapons.Weapon;

/**
 * The implementation for handshakes with hosts.
 * 
 * @author Michon
 */
public class HostHandshake implements IHostHandshake {

	@Override
	public int requestParticipant(boolean isPlayer, int playerID, ArrayList<Class<? extends Weapon>> weapons) {
		if (isPlayer) {
			Player p = new Player(playerID, weapons);
			GlobalState.currentGame.players.add(p);
			GlobalState.currentGame.scores.put(playerID, p.data);
			// TODO: p.data.name 
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
