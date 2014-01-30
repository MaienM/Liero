package com.lierojava.net.implementations;

import java.util.ArrayList;

import com.lierojava.client.GlobalState;
import com.lierojava.combat.weapons.Weapon;
import com.lierojava.net.interfaces.IHostHandshake;
import com.lierojava.net.interfaces.IParticipantHost;
import com.lierojava.participants.Player;

/**
 * The implementation for handshakes with hosts.
 * 
 * @author Michon
 */
public class HostHandshake implements IHostHandshake {

	@Override
	public int requestParticipant(boolean isPlayer, int playerID, String playerName, ArrayList<Class<? extends Weapon>> weapons) {
		IParticipantHost iph;
		if (isPlayer) {
			Player p = new Player(weapons);
			p.connection = GlobalState.lastSender;
			GlobalState.currentGame.players.add(p);
			GlobalState.currentGame.scores.add(p.data);
			p.data.id = playerID;
			p.data.name = playerName;
			iph = new ParticipantHostPlayer(p);
		}
		else {
			iph = new ParticipantHostSpectator(playerName);
		}
		GlobalState.objectSpace.register(++GlobalState.objectSpaceIndex, iph);
		return GlobalState.objectSpaceIndex;
	}
}
