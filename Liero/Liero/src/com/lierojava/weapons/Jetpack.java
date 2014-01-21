package com.lierojava.weapons;

import com.lierojava.bullets.PistolBullet;
import com.lierojava.participants.Player;

public class Jetpack extends Weapon {
	public Jetpack() {
		maxCharge = 10;
		regenSpeed = 0.5f;
		knockbackForce = 2f;
		fireRate = 0.15f;
		fireSound = null;
		bulletClass = PistolBullet.class;
	}
	public Jetpack(Player p) {
		this();		
		setup(p);
	}
}
