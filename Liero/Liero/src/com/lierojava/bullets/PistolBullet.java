package com.lierojava.bullets;

import com.badlogic.gdx.math.Vector2;

public class PistolBullet extends Bullet {
	public PistolBullet() {
		size = new Vector2(2, 2);
		speed = 3;
		health = 20;
		textureRegion = "bullet";
	}
}
