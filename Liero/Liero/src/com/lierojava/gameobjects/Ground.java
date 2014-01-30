package com.lierojava.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.lierojava.Constants;
import com.lierojava.client.GlobalState;
import com.lierojava.client.render.RenderProxy;
import com.lierojava.client.render.TextureRenderProxy;
import com.lierojava.combat.bullets.Bullet;
import com.lierojava.gameobjects.userdata.SimpleUserData;

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
		box.setAsBox(Constants.GROUND_SIZE / 2, Constants.GROUND_SIZE / 2);
		
		// Create the fixture.
		body.createFixture(box, 0f);
		
		// Cleanup.
		box.dispose();
	}

	@SuppressWarnings("serial")
	@Override
	public ArrayList<RenderProxy> render() {
		return new ArrayList<RenderProxy>() {{
			add(new TextureRenderProxy("ground", body.getPosition(), new Vector2(Constants.GROUND_SIZE + 1, Constants.GROUND_SIZE + 1), 0, false, false, true));
		}};
	}

	@Override
	protected void die(Bullet bullet) {
		body.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
	}

	@Override
	public void collision(GameObject other, Body ownBody, Body otherBody) {
	}
}
