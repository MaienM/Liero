package com.lierojava.combat.bullets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.lierojava.Constants;
import com.lierojava.client.GlobalState;
import com.lierojava.gameobjects.GameObject;
import com.lierojava.gameobjects.userdata.SimpleUserData;

public class RocketBullet extends Bullet {
	public RocketBullet() {
		size = new Vector2(7, 4);
		speed = 40;
		textureRegion = "rocket";
	}
	
	@Override 
	public void collision(GameObject other, Body ownBody, Body otherBody) {	
		// Damage all blocks in a certain radius.
		Array<Body> bodies = new Array<Body>();
		GlobalState.currentGame.world.getBodies(bodies);
		for (Body b : bodies) {
			double distance = Math.sqrt(Math.pow(b.getWorldCenter().x - ownBody.getWorldCenter().x, 2) + 
									   Math.pow(b.getWorldCenter().y - ownBody.getWorldCenter().y, 2));
			if (distance < Constants.BLAST_RADIUS) {
				this.health = (int)(130 - (distance / Constants.BLAST_RADIUS) * 100);
				((GameObject)b.getUserData()).damage(this);
			}
		}
		ownBody.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
	}
}
