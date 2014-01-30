package com.lierojava.combat.bullets;

import com.badlogic.gdx.math.Vector2;

public class LaserBullet extends Bullet {
	public LaserBullet() {
		size = new Vector2(2, 2);
		speed = 50;
		gravity = 0;
		health = 5;
		textureRegion = "laser";
	}
}
