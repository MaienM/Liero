package com.lierojava.combat.bullets;

import com.badlogic.gdx.math.Vector2;

public class LaserBullet extends Bullet {
	public LaserBullet() {
		size = new Vector2(2, 2);
		speed = 40;
		health = 1;
		textureRegion = "bullet";
	}
}
