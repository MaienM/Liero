package com.lierojava.net.interfaces;

import com.lierojava.participants.Player;

/**
 * The interface for communication from the participant to the host.
 * 
 * @author Michon
 */
public interface IParticipantHost {
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
	 * TODO: @see this.
	 */
	public abstract void chat(String message);
	
	/**
	 * Notify the host that you have an IHostParticipant object waiting for him.
	 * @param index The index of the object.
	 */
	public abstract void register(int index);
}
