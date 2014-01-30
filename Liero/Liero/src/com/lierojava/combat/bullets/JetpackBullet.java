package com.lierojava.combat.bullets;

import com.badlogic.gdx.math.Vector2;

public class JetpackBullet extends Bullet {
	public JetpackBullet() {
		size = new Vector2(2, 2);
		speed = 1;
		health = 0;
		textureRegion = "bullet";
	}
}
