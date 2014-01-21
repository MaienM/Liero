package com.lierojava.weapons;

import com.badlogic.gdx.Gdx;
import com.lierojava.bullets.PistolBullet;
import com.lierojava.participants.Player;

public class Pistol extends Weapon {
	public Pistol() {
		maxCharge = 4;
		regenSpeed = 0.1f;
		knockbackForce = 1f;
		fireRate = 0.1f;
		fireSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gun.mp3"));
		bulletClass = PistolBullet.class;
	}
	public Pistol(Player p) {
		this();
		setup(p);
	}
}
