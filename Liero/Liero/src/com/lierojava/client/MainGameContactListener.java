package com.lierojava.client;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MainGameContactListener implements ContactListener {	
	@Override
	public void beginContact(Contact contact) {}

	@Override
	/**
	 * On contact between two fixtures.
	 * 
	 * @param contact The contact.
	 */
	public void endContact(Contact contact) {
		GlobalState.currentGame.endContact(contact);
	}	

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {}
}