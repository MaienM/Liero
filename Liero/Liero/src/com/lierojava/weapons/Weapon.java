package com.lierojava.weapons;

import java.io.Serializable;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.lierojava.Constants;
import com.lierojava.Utils;
import com.lierojava.bullets.Bullet;
import com.lierojava.participants.Player;

public abstract class Weapon implements Serializable {
	/**
	 * The player to which this weapon belongs.
	 */
	protected Player player;
	
	/**
	 * The current charge of the weapon.
	 */
	private int currentCharge = 0;
	
	/**
	 * The maximum charge of the weapon.
	 */
	protected int maxCharge;
	
	/**
	 * The speed at which the weapon regenerates.
	 */
	protected float regenSpeed;
	
	/**
	 * The knockback power of the weapon.
	 */
	protected float knockbackForce;
	
	/**
	 * The fire rate of the weapon.
	 */
	protected float fireRate;
	
	/**
	 * The time the last shot was fired.
	 */
	private long lastFireTime = -1;
	
	/**
	 * The sound the gun makes when firing.
	 */
	protected Sound fireSound;
	
	/**
	 * The type of the bullet corresponding to this weapon.
	 */
	protected Class<?> bulletClass;
	
	/**
	 * Setup a new weapon.
	 * 
	 * @param p The player to which this weapon belongs.
	 */
	protected void setup(Player p) {
		player = p;
		currentCharge = maxCharge;
		
		// Start the regeneration timer.
		Timer.schedule(new Task() {
			@Override
			public void run() {
				currentCharge = Math.min(maxCharge, currentCharge + 1);
			}
		}, 0, regenSpeed);
	}
	
	/**
	 * Fire the weapon.
	 * 
	 * @param angle The angle at which to fire the weapon.
	 */
	public void fire(float angle) {
		// If not enough time has elapsed, don't do anything.
		long currentTime = TimeUtils.millis();
		if (lastFireTime + (long)(fireRate * 1000) > currentTime) {
			return;
		}
		lastFireTime = currentTime;
		
		// If the weapon is out of charge, don't do anything.
		if (currentCharge < 1) {
			return;
		}
		currentCharge--;
		
		// Play the sound.
		if (fireSound != null) {
			fireSound.play();
		}
		
		// Spawn a bullet.
		if (bulletClass != null) {
			Bullet bullet;
			try {
				bullet = (Bullet)bulletClass.newInstance();
			} catch (Exception e) {
				return;
			}
			bullet.setup(player.getBody().getPosition().add(Utils.angleToVector(angle).scl(Constants.WEAPON_OFFSET)), angle);
		}
		
		// Apply knockback.
		player.getBody().applyLinearImpulse(Utils.angleToVector(angle + 90).scl(knockbackForce * 1000), player.getBody().getWorldCenter(), true);
	}
}
