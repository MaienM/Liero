package com.lierojava;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Array;

public class Utils {
	/**
	 * Get the offset for the camera.
	 * 
	 * @return The offset for the camera.
	 */
	public static Vector2 getCameraOffset() {
		return new Vector2(Gdx.graphics.getWidth() / -2, Gdx.graphics.getHeight() / -2);
	}
	
	/**
	 * Convert an angle to a vector2.
	 * 
	 * @param angle The angle.
	 * @return The vector2.
	 */
	public static Vector2 angleToVector(float angle) {
		return new Vector2((float)Math.sin(angle), (float)Math.cos(angle));
	}
	
	/**
	 * Print function, cause we're lazy.
	 */
	public static void print(Object o) {
		System.out.println(o);
	}
	
	/**
	 * Get the contacts with a body.
	 * 
	 * @param b The body.
	 * @return A list of all the contacts for this body.
	 */
	public static Array<Contact> getContacts(Body b) {
		Array<Contact> contacts = new Array<>();
		for (Contact c : GlobalState.currentGame.world.getContactList()) {
			if (c.getFixtureA().getBody() == b || c.getFixtureB().getBody() == b) {
				contacts.add(c);
			}
		}
		return contacts;
	}
}
