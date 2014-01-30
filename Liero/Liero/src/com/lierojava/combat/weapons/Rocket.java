package com.lierojava.combat.weapons;

import com.badlogic.gdx.Gdx;
import com.lierojava.combat.bullets.RocketBullet;
import com.lierojava.participants.Player;

public class Rocket extends Weapon {
	public Rocket() {
		maxCharge = 1;
		regenSpeed = 10f;
		knockbackForce = 10f;
		fireRate = 1f;
		fireSound = Gdx.audio.newSound(Gdx.files.internal("sounds/grenadeExplosion.mp3"));
		bulletClass = RocketBullet.class;
		icon = "icon_rocket";
	}
	public Rocket(Player p) {
		this();
		setup(p);
	}
}
