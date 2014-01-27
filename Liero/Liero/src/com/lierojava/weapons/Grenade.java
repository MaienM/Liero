package com.lierojava.weapons;

import com.badlogic.gdx.Gdx;
import com.lierojava.bullets.GrenadeBullet;
import com.lierojava.participants.Player;

public class Grenade extends Weapon {
	public Grenade() {
		maxCharge = 1;
		regenSpeed = 10f;
		knockbackForce = 0f;
		fireRate = 0.1f;
		fireSound = Gdx.audio.newSound(Gdx.files.internal("sounds/grenadeThrow.mp3"));
		bulletClass = GrenadeBullet.class;
		icon = "icon_grenade";
	}
	public Grenade(Player p) {
		this();
		setup(p);
	}
}
