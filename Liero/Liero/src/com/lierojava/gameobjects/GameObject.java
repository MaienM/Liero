package com.lierojava.gameobjects;

import com.lierojava.bullets.Bullet;
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
	 * @param bullet The bullet that deals damage.
	 */
	public void damage(Bullet bullet) {
		health -= Math.max(0, bullet.health);
		if (health <= 0) {
			die(bullet);
		}
	}
	
	/**
	 * Handles the death of the game object.
	 * @param bullet The bullet by which you were killed.
	 */
	protected abstract void die(Bullet bullet);
}
