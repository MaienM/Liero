package com.lierojava.net.implementations;

import java.util.ArrayList;

import com.lierojava.PlayerData;
import com.lierojava.client.GlobalState;
import com.lierojava.client.render.RenderProxy;
import com.lierojava.net.interfaces.IParticipantHost;
import com.lierojava.participants.Player;

/**
 * The implementation for communication from the participant to the host.
 * 
 * @author Michon
 */
public class ParticipantHostPlayer extends Chat implements IParticipantHost {
	
	/**
	 * The player object.
	 */
	public int index = 0;
	
	public ParticipantHostPlayer() {
		super(false);
	}
	public ParticipantHostPlayer(Player player) {
		this();
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
		if (GlobalState.currentGame.world.isLocked()) return;
		synchronized (GlobalState.currentGame.world) {
			GlobalState.currentGame.players.get(index).jetpack();
		}
	}

	@Override
	public void fire() {
		synchronized (GlobalState.currentGame.world) {
			GlobalState.currentGame.players.get(index).fire();
		}
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
	public void sendMessage(String message) {
		super.sendMessage(GlobalState.currentGame.players.get(index).data.name + "> " + message);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<RenderProxy> getRenderProxies() {
		if (GlobalState.currentGame.world.isLocked()) return null;
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
