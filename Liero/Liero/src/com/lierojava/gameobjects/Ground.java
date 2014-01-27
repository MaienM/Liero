package com.lierojava.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.lierojava.Constants;
import com.lierojava.GlobalState;
import com.lierojava.Utils;
import com.lierojava.bullets.Bullet;
import com.lierojava.net.RenderProxy;
import com.lierojava.net.TextureRenderProxy;
import com.lierojava.userdata.SimpleUserData;

public class Ground extends GameObject {
	/*
	 * The box2d body 
	 */
	Body body;
	
	/**
	 * Creates a ground object
	 * 
	 * @param b The body this groundObject belongs to
	 */
	public Ground(Vector2 position) {	
		health = 30;
		
		// Create the body.	
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(position);
		body = GlobalState.currentGame.world.createBody(bodyDef);
		body.setUserData(this);
				
		// Create the shape.
		PolygonShape box = new PolygonShape();
		box.setAsBox(Constants.GROUND_SIZE, Constants.GROUND_SIZE);
		
		// Create the fixture.
		body.createFixture(box, 0f);
		
		// Cleanup.
		box.dispose();
	}

	@Override
	public RenderProxy render() {
		return new TextureRenderProxy("ground", body.getPosition(), new Vector2(Constants.GROUND_SIZE, Constants.GROUND_SIZE));
	}

	@Override
	protected void die(Bullet bullet) {
		Utils.print("Marked ground ready for removal");
		body.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
	}
}
