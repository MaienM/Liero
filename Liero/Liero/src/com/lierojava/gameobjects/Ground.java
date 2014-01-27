package com.lierojava.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.lierojava.Constants;
import com.lierojava.Utils;
import com.lierojava.net.RenderProxy;
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
	public Ground(Body b) {
		body = b;
		health = 30;
	}

	@Override
	public RenderProxy render() {
		return new RenderProxy("ground", body.getPosition(), new Vector2(Constants.GROUND_SIZE, Constants.GROUND_SIZE));
	}

	@Override
	protected void die() {
		Utils.print("Marked ground ready for removal");
		body.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
	}
}
