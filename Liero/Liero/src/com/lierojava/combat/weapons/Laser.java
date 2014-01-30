package com.lierojava.combat.weapons;

import com.lierojava.combat.bullets.LaserBullet;
import com.lierojava.participants.Player;

public class Laser extends Weapon {
	public Laser() {
		maxCharge = 1000;
		regenSpeed = 10f;
		knockbackForce = 1f;
		fireRate = 0.03f;
		//fireSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.mp3"));
		bulletClass = LaserBullet.class;
		icon = "icon_laser";
	}
	public Laser(Player p) {
		this();
		setup(p);
	}
}
