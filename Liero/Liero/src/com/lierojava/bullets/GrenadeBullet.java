package com.lierojava.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.lierojava.userdata.SimpleUserData;

public class GrenadeBullet extends Bullet {
	public GrenadeBullet() {
		size = new Vector2(4, 4);
		speed = 20;
		health = 15;
		textureRegion = "grenade";
	}
	
	@Override
	public void setup(Vector2 start, final float angle) {
		// Throw grenade.
		spawnBullet(start, angle);
		body.setBullet(false);
		textureRegion = "bullet";
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Vector2 start = body.getPosition();
				body.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
				Gdx.audio.newSound(Gdx.files.internal("sounds/grenadeExplosion.mp3")).play();
				size = new Vector2(1, 1);
				speed = 1f;
				float PI = (float)Math.PI;
				for (float x = 0, y = 2, z = 0; x < 2; x += 0.25f, y -= 0.25f, z += 0.125f) {
					spawnBullet(new Vector2(start.x + x, start.y + y), PI * z);
					spawnBullet(new Vector2(start.x - x, start.y - y), PI + PI * z);
				}
			}
		}).start();
	}
}
