package com.lierojava.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class DynamicCircle {

	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	public DynamicCircle()
	{
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
				
		CircleShape circle = new CircleShape();
		circle.setRadius(6);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
	}
	
	public void addToWorld(World world) {
		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
	}

	public void setPosition(Vector2 position) {
		bodyDef.position.set(position);
		
	}
}
