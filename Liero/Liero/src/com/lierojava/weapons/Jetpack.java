package com.lierojava.weapons;

import com.lierojava.bullets.JetpackBullet;
import com.lierojava.participants.Player;

public class Jetpack extends Weapon {
	public Jetpack() {
		maxCharge = 30;
		regenSpeed = 0.5f;
		knockbackForce = 8f;
		fireRate = 0.15f;
		fireSound = null;
		bulletClass = JetpackBullet.class;
	}
	public Jetpack(Player p) {
		this();		
		setup(p);
	}
}
