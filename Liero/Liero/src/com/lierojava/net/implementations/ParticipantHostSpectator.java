package com.lierojava.net.implementations;

import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.lierojava.PlayerData;
import com.lierojava.client.GlobalState;
import com.lierojava.client.render.RenderProxy;
import com.lierojava.net.interfaces.IParticipantHost;

/**
 * The implementation for communication from the participant to the host.
 * 
 * @author Michon
 */
public class ParticipantHostSpectator extends Chat implements IParticipantHost {
	
	/**
	 * The name of the spectator.
	 */
	public String name;
	
	/**
	 * The connection to this participant.
	 */
	public Connection connection;
	
	public ParticipantHostSpectator() {
		super(false);
	}
	public ParticipantHostSpectator(String name) {
		this();
		this.name = name;
	}

	@Override
	public void moveLeft() {
	}

	@Override
	public void moveRight() {
	}

	@Override
	public void aimLeft() {
	}

	@Override
	public void aimRight() {
	}

	@Override
	public void jump() {
	}

	@Override
	public void jetpack() {
	}

	@Override
	public void fire() {
	}
	
	@Override
	public void stopSidewaysMovement() {
	}

	@Override
	public void setWeaponIndex(int index) {
	}

	@Override
	public int getWeaponIndex() {
		return 0;
	}
	
	@Override
	public void sendMessage(String message) {
		super.sendMessage(this.name + "> " + message);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<RenderProxy> getRenderProxies() {
		synchronized (GlobalState.currentGame.renderProxies) {
			return (ArrayList<RenderProxy>)GlobalState.currentGame.renderProxies.clone();
		}
	}
	
	@Override
	public ArrayList<PlayerData> getScores() {
		return GlobalState.currentGame.scores;
	}
	
	@Override
	public float getTimeRemaining() {
		return GlobalState.currentGame.timeRemaining;
	}
}
