package com.lierojava.net.handles;

import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.lierojava.client.GlobalState;
import com.lierojava.net.interfaces.IParticipantHost;
import com.lierojava.participants.Player;
import com.lierojava.render.RenderProxy;

/**
 * The implementation for communication from the participant to the host.
 * 
 * @author Michon
 */
public class ParticipantHost implements IParticipantHost {
	
	/**
	 * The player object.
	 */
	public int index = 0;
	
	/**
	 * The connection to this participant.
	 */
	public Connection connection;
	
	public ParticipantHost() {}
	public ParticipantHost(Player player) {
		if (player != null) {
			this.index = GlobalState.currentGame.players.indexOf(player);
		}
	}

	@Override
	public void moveLeft() {
		GlobalState.currentGame.players.get(index).moveLeft();
	}

	@Override
	public void moveRight() {
		GlobalState.currentGame.players.get(index).moveRight();
	}

	@Override
	public void aimLeft() {
		GlobalState.currentGame.players.get(index).aimLeft();
	}

	@Override
	public void aimRight() {
		GlobalState.currentGame.players.get(index).aimRight();
	}

	@Override
	public void jump() {
		GlobalState.currentGame.players.get(index).jump();
	}

	@Override
	public void jetpack() {
		GlobalState.currentGame.players.get(index).jetpack();
	}

	@Override
	public void fire() {
		GlobalState.currentGame.players.get(index).fire();
	}
	
	@Override
	public void stopSidewaysMovement() {
		GlobalState.currentGame.players.get(index).stopSidewaysMovement();
	}

	@Override
	public void setWeaponIndex(int index) {
		GlobalState.currentGame.players.get(this.index).setWeaponIndex(index);
	}

	@Override
	public int getWeaponIndex() {
		return GlobalState.currentGame.players.get(index).getWeaponIndex();
	}

	@Override
	public void chat(String message) {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<RenderProxy> getRenderProxies() {
		synchronized (GlobalState.currentGame.renderProxies) {
			return (ArrayList<RenderProxy>)GlobalState.currentGame.renderProxies.clone();
		}
	}
}
