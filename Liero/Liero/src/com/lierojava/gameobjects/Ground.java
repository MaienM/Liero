package com.lierojava.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lierojava.Constants;
import com.lierojava.GlobalState;
import com.lierojava.Utils;

public class Ground {
	/*
	 * The box2d body 
	 */
	Body body;
	
	/*
	 * The damage this ground object can still take
	 */
	public int damage = 30;
	
	/**
	 * The groundObjects that still exist
	 */
	public static ArrayList<Ground> groundObjects = new ArrayList<Ground>();
	
	/**
	 * Fills the world with groundObject bodies
	 * 
	 * @param world The world to fill
	 */
	public static void fillWorld(World world){
		for (int i = 0; i < Gdx.graphics.getWidth(); i = i + Constants.GROUND_DIM) {
			for (int j = 0; j < Gdx.graphics.getHeight(); j = j + Constants.GROUND_DIM) {
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyType.StaticBody;
				bodyDef.fixedRotation = true;
				bodyDef.position.x = i + Utils.getCameraOffset().x;
				
				bodyDef.position.y = j + Utils.getCameraOffset().y;
				
				Body body = GlobalState.currentGame.world.createBody(bodyDef);
				Ground currGround = new Ground(body);
				body.setUserData(currGround);
				groundObjects.add(currGround);
						
				// Create the shape.
				PolygonShape box = new PolygonShape();
				// Set the polygon shape as a box which is twice the size of our view
				// port and 20 high
				// (setAsBox takes half-width and half-height as arguments)
				box.setAsBox(Constants.GROUND_DIM, Constants.GROUND_DIM);
				
				// Create the fixture.
				body.createFixture(box, 0f);
				
				// Cleanup.
				box.dispose();
			}
		}
	}
	
	/**
	 * Creates a ground object
	 * 
	 * @param b The body this groundObject belongs to
	 */
	public Ground(Body b) {
		this.body = b;
	}
	
	/*
	 * Renders this groundObject
	 */
	public void render(SpriteBatch batch) {
		Vector2 position = this.body.getPosition();
		Vector2 size = new Vector2(Constants.GROUND_TEXTURE.getWidth() * Constants.TEXTURE_SCALE, Constants.GROUND_TEXTURE.getHeight() * Constants.TEXTURE_SCALE);
		batch.draw(Constants.GROUND_TEXTURE, position.x - size.x / 2, position.y - size.y / 2, size.x, size.y);
	}
}
