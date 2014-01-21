package com.lierojava.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.lierojava.Constants;
import com.lierojava.GlobalState;
import com.lierojava.userdata.SimpleUserData;

public class StaticBarrier {
	/**
	 * The Box2D body for the floor. 
	 * 
	 * This stores data such as position and velocity.
	 */
	private Body floorBody;
	
	/**
	 * The Box2D body for the ceiling. 
	 * 
	 * This stores data such as position and velocity.
	 */
	private Body ceilingBody;

	/**
	 * The Box2D body for the left wall. 
	 * 
	 * This stores data such as position and velocity.
	 */
	private Body leftWallBody;
	
	/**
	 * The Box2D body for the right wall. 
	 * 
	 * This stores data such as position and velocity.
	 */
	private Body rightWallBody;
	
	public StaticBarrier() {
		// Create the floor.
		floorBody = createBody(new Vector2(-Gdx.graphics.getWidth() / 2 + 1, -Gdx.graphics.getHeight() / 2), new Vector2(Gdx.graphics.getWidth(), Constants.BARRIER_SIZE));
		floorBody.setUserData(SimpleUserData.FLOOR);
		
		// Create the ceiling.
		ceilingBody = createBody(new Vector2(-Gdx.graphics.getWidth() / 2 + 1, Gdx.graphics.getHeight() / 2), new Vector2(Gdx.graphics.getWidth(), Constants.BARRIER_SIZE));
		ceilingBody.setUserData(SimpleUserData.CEILING);
		
		// Create the walls.
		leftWallBody = createBody(new Vector2(-Gdx.graphics.getWidth() / 2 + Constants.BARRIER_SIZE, -Gdx.graphics.getHeight() / 2), new Vector2(0, Gdx.graphics.getHeight()));
		rightWallBody = createBody(new Vector2(Gdx.graphics.getWidth() / 2 - Constants.BARRIER_SIZE, -Gdx.graphics.getHeight() / 2), new Vector2(0, Gdx.graphics.getHeight()));
		leftWallBody.setUserData(SimpleUserData.WALL);
		rightWallBody.setUserData(SimpleUserData.WALL);
	}
	
	
	private Body createBody(Vector2 position, Vector2 size) {
		// Create the body.
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.fixedRotation = true;
		bodyDef.position.x = position.x;
		bodyDef.position.y = position.y;
		Body body = GlobalState.currentGame.world.createBody(bodyDef);
				
		// Create the shape.
		PolygonShape box = new PolygonShape();
		// Set the polygon shape as a box which is twice the size of our view
		// port and 20 high
		// (setAsBox takes half-width and half-height as arguments)
		box.setAsBox(size.x, size.y);
		
		// Create the fixture.
		body.createFixture(box, 0f);
		
		// Cleanup.
		box.dispose();
		
		return body;
	}
}
