package com.lierojava.combat.bullets;

import com.badlogic.gdx.math.Vector2;
import com.lierojava.Utils;

public class ShotgunBullet extends Bullet {
	public ShotgunBullet() {
		size = new Vector2(0.5f, 0.5f);
		speed = 1;
		health = 7;
		textureRegion = "bullet";
	}
	
	@Override
	public void setup(Vector2 start, float angle) {
		Vector2 lead = Utils.angleToVector(angle);
		spawnBullet(start, angle);
		spawnBullet(new Vector2(start.x + lead.y * 2, start.y + lead.x * 2), angle + 0.05f);
		spawnBullet(new Vector2(start.x - lead.y * 2, start.y - lead.x * 2), angle - 0.05f);
		spawnBullet(new Vector2(start.x + lead.y * 4, start.y + lead.x * 4), angle + 0.1f);
		spawnBullet(new Vector2(start.x - lead.y * 4, start.y - lead.x * 4), angle - 0.1f);
		spawnBullet(new Vector2(start.x + lead.y * 6, start.y + lead.x * 6), angle + 0.15f);
		spawnBullet(new Vector2(start.x - lead.y * 6, start.y - lead.x * 6), angle - 0.15f);
		spawnBullet(new Vector2(start.x + lead.y * 8, start.y + lead.x * 8), angle + 0.2f);
		spawnBullet(new Vector2(start.x - lead.y * 8, start.y - lead.x * 8), angle - 0.2f);
	}
}
