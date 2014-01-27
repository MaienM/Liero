package com.lierojava.gameobjects;

import com.lierojava.net.RenderProxy;

public abstract class GameObject {
	/**
	 * The health of the game object.
	 */
	public int health;
	
	/**
	 * Returns the renderproxy for the game object.
	 * 
	 * @return The renderproxy.
	 */
	public abstract RenderProxy render();
	
	/**
	 * Deal damage to the game object.
	 * @param damage The amount of damage to deal.
	 */
	public void damage(int damage) {
		health -= damage;
		if (health <= 0) {
			die();
		}
	}
	
	/**
	 * Handles the death of the game object.
	 */
	protected abstract void die();
}
