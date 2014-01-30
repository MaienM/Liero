package com.lierojava.combat.weapons;

import com.badlogic.gdx.Gdx;
import com.lierojava.combat.bullets.PistolBullet;
import com.lierojava.participants.Player;

public class Pistol extends Weapon {
	public Pistol() {
		maxCharge = 4;
		regenSpeed = 0.5f;
		knockbackForce = 1f;
		fireRate = 1f;
		fireSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gun.mp3"));
		bulletClass = PistolBullet.class;
		icon = "icon_gun";
	}
	public Pistol(Player p) {
		this();
		setup(p);
	}
}
