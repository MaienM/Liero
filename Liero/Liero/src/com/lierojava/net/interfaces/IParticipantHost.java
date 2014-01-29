package com.lierojava.net.interfaces;

import java.util.ArrayList;

import com.lierojava.PlayerData;
import com.lierojava.participants.Player;
import com.lierojava.render.RenderProxy;

/**
 * The interface for communication from the participant to the host.
 * 
 * @author Michon
 */
public interface IParticipantHost extends IChat {
	/**
	 * @see Player.moveLeft
	 */
	public abstract void moveLeft();

	/**
	 * @see Player.moveRight
	 */
	public abstract void moveRight();

	/**
	 * @see Player.stopSidewaysMovement
	 */
	public abstract void stopSidewaysMovement();
	
	/**
	 * @see Player.aimLeft
	 */
	public abstract void aimLeft();
	
	/**
	 * @see Player.aimRight
	 */
	public abstract void aimRight();
	
	/**
	 * @see Player.jump
	 */
	public abstract void jump();
	
	/**
	 * @see Player.jetpack
	 */
	public abstract void jetpack();
	
	/**
	 * @see Player.fire
	 */
	public abstract void fire();

	/**
	 * @see Player.setWeaponIndex
	 */
	public abstract void setWeaponIndex(int index);
	
	/**
	 * @see Player.getWeaponIndex
	 */
	public abstract int getWeaponIndex();

	/**
	 * Get the render proxies.
	 * @return The list of render proxies.
	 */
	public abstract ArrayList<RenderProxy> getRenderProxies();
	
	/**
	 * Get the score.
	 * @return The list of playerdata.
	 */
	public abstract ArrayList<PlayerData> getScores();

	/**
	 * Get the remaining time.
	 * @return The remaining time.
	 */
	public abstract float getTimeRemaining();
}
