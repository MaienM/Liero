package com.lierojava.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class PistolBullet extends Bullet {
	public PistolBullet() {
		size = new Vector2(1, 1);
		speed = 1;
		damage = 20;
		texture = new Texture(Gdx.files.internal("textures/bullet.png"));
	}
}
