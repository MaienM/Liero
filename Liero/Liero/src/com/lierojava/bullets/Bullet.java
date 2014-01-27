package com.lierojava.bullets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.lierojava.GlobalState;
import com.lierojava.Utils;
import com.lierojava.gameobjects.GameObject;
import com.lierojava.net.RenderProxy;
import com.lierojava.net.TextureRenderProxy;
import com.lierojava.participants.Player;

public abstract class Bullet extends GameObject {
	/**
	 * The body of this bullet.
	 */
	protected Body body;
	
	/**
	 * The player that fired this bullet.
	 */
	public Player player;
	
	/**
	 * The size of the bullet.
	 */
	protected Vector2 size;
	
	/**
	 * The speed of the bullet.
	 */
	protected float speed;
	
	/**
	 * The texture region of the bullet.
	 */
	protected String textureRegion;

	/**
	 * @see spawnBullet.
	 */
	public void setup(Vector2 start, float angle) {
		spawnBullet(start, angle);
	}
	
	/**
	 * Spawn a bullet.
	 * 
	 * @param start The start position of the bullet.
	 * @param angle The angle at which the bullet is fired.
	 */
	protected void spawnBullet(Vector2 start, float angle) {
		// Create the body.
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(start);
		bodyDef.bullet = true;
		body = GlobalState.currentGame.world.createBody(bodyDef);
		body.setUserData(this);

		// Create the bullet shape.
		PolygonShape box = new PolygonShape();
		box.setAsBox(size.x, size.y);
		
		// Create the fixture.
		FixtureDef fixture = new FixtureDef();
		fixture.shape = box;
		fixture.density = 1.0f;
		fixture.friction = 3.0f;
		fixture.restitution = 0.2f;
		body.createFixture(fixture);
		
		// Apply force.
		body.applyForceToCenter(Utils.angleToVector(angle).scl(10000 * speed), true);
	}
	
	@Override
	public RenderProxy render() {
		return new TextureRenderProxy(textureRegion, body.getPosition(), size, body.getAngle());
	}
	
	@Override
	protected void die(Bullet bullet) {
	}
}
