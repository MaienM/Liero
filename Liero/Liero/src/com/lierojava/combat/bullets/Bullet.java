package com.lierojava.combat.bullets;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.lierojava.Utils;
import com.lierojava.client.GlobalState;
import com.lierojava.client.render.RenderProxy;
import com.lierojava.client.render.TextureRenderProxy;
import com.lierojava.gameobjects.GameObject;
import com.lierojava.gameobjects.userdata.SimpleUserData;
import com.lierojava.participants.Player;

public abstract class Bullet extends GameObject {
	/**
	 * The body of this bullet.
	 */
	protected ArrayList<Body> bodies = new ArrayList<Body>();
	
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
	 * The gravity of the bullet.
	 */
	protected float gravity = 1;
	
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
		//Wait till the world is available
		while(GlobalState.currentGame.world.isLocked());
		// Create the body.
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(start);
		bodyDef.bullet = true;
		bodyDef.gravityScale = gravity;
		Body body = GlobalState.currentGame.world.createBody(bodyDef);
		body.setUserData(this);
		bodies.add(body);

		// Create the bullet shape.
		PolygonShape box = new PolygonShape();
		box.setAsBox(size.x, size.y);
		
		// Create the fixture.
		FixtureDef fixture = new FixtureDef();
		fixture.shape = box;
		fixture.density = 1;
		fixture.friction = 3.0f;
		fixture.restitution = 0.2f;
		body.createFixture(fixture);
		
		// Apply force.
		body.applyForceToCenter(Utils.angleToVector(angle).scl(10000 * speed), true);
	}
	
	@Override
	public ArrayList<RenderProxy> render() {
		ArrayList<RenderProxy> proxies = new ArrayList<RenderProxy>();
		for (Body b : bodies) {
			proxies.add(new TextureRenderProxy(textureRegion, b.getPosition(), new Vector2(size).scl(3), b.getAngle()));
		}
		return proxies;
	}
	
	@Override
	protected void die(Bullet bullet) {
	}
	
	@Override
	public void collision(GameObject other, Body ownBody, Body otherBody) {
		other.damage(this);
		ownBody.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
	}
}
