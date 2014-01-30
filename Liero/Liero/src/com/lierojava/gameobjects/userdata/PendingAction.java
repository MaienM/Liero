package com.lierojava.gameobjects.userdata;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class PendingAction {
	/**
	 * The old user data of the body. 
	 * Will be restored once this action has been run.
	 */
	public Object userData;
	
	/**
	 * The body to which this action belongs.
	 */
	public Body body;
	
	public PendingAction(Body body) {
		this.body = body;
		this.userData = body.getUserData();
		body.setUserData(this);
	}

	/**
	 * The action that will be performed.
	 */
	protected abstract void run();
	
	/**
	 * Perform the action.
	 */
	public void start() {
		run();
		body.setUserData(userData);
	}
}
