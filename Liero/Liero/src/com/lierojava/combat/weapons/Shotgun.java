package com.lierojava.combat.weapons;

import com.badlogic.gdx.Gdx;
import com.lierojava.combat.bullets.ShotgunBullet;
import com.lierojava.participants.Player;

public class Shotgun extends Weapon {
	public Shotgun() {
		maxCharge = 1;
		regenSpeed = 1.5f;
		knockbackForce = 3f;
		fireRate = 0.1f;
		fireSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shotgun.mp3"));
		bulletClass = ShotgunBullet.class;
		icon = "icon_shotgun";
	}
	public Shotgun(Player p) {
		this();
		setup(p);
	}
}
