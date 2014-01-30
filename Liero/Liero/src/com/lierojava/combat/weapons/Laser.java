package com.lierojava.combat.weapons;

import com.lierojava.combat.bullets.LaserBullet;
import com.lierojava.participants.Player;

public class Laser extends Weapon {
	public Laser() {
		maxCharge = 25;
		regenSpeed = 0.3f;
		knockbackForce = 0f;
		fireRate = 0.07f;
		//fireSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.mp3"));
		bulletClass = LaserBullet.class;
		icon = "icon_lasergun";
	}
	public Laser(Player p) {
		this();
		setup(p);
	}
}
