package com.lierojava.bullets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.lierojava.GlobalState;
import com.lierojava.Utils;

public abstract class Bullet {
	/**
	 * The body of this bullet.
	 */
	private Body body;
	
	/**
	 * The size of the bullet.
	 */
	protected Vector2 bulletSize;
	
	/**
	 * The speed of the bullet.
	 */
	protected float bulletSpeed;

	/**
	 * Setup the bullet.
	 * 
	 * @param start The start position of the bullet.
	 * @param angle The angle at which the bullet is fired.
	 */
	public void setup(Vector2 start, float angle) {		
		// Create the body.
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(start);
		bodyDef.bullet = true;
		body = GlobalState.currentGame.world.createBody(bodyDef);

		// Create the bullet shape.
		PolygonShape box = new PolygonShape();
		box.setAsBox(bulletSize.x, bulletSize.y);
		
		// Create the fixture.
		FixtureDef fixture = new FixtureDef();
		fixture.shape = box;
		fixture.density = 1.0f;
		fixture.friction = 0.0f;
		fixture.restitution = 0.2f;
		body.createFixture(fixture);
		
		// Apply force.
		body.applyForceToCenter(Utils.angleToVector(angle).scl(10000 * bulletSpeed), true);
	}
}
