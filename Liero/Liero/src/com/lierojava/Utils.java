package com.lierojava;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Array;
import com.lierojava.client.GlobalState;
import com.lierojava.gameobjects.GameObject;

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
	public static ArrayList<Contact> getContacts(Body b) {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		for (Contact c : GlobalState.currentGame.world.getContactList()) {
			if (c.getFixtureA().getBody() == b || c.getFixtureB().getBody() == b) {
				contacts.add(c);
			}
		}
		return contacts;
	}
	
	/**
	 * Get the bodies that are in contact with a body.
	 * 
	 * @param b The body.
	 * @return A list of all the bodies that are in contact with this body.
	 */
	public static ArrayList<Body> getContactBodies(Body b) {
		ArrayList<Body> bodies = new ArrayList<Body>();
		for (Contact c : Utils.getContacts(b)) {
			if (c.getFixtureA().getBody() == b) {
				bodies.add(c.getFixtureB().getBody());
			}
			else {
				bodies.add(c.getFixtureA().getBody());
			}
		}
		return bodies;
	}
	
	/**
	 * Get all game objects.
	 * @return A list of all game objects.
	 */
	public static ArrayList<GameObject> getGameObjects() {
		Array<Body> bodies = new Array<Body>();
		GlobalState.currentGame.world.getBodies(bodies);
		ArrayList<GameObject> objects = new ArrayList<GameObject>();
		for (Body b : bodies) {
			if (b.getUserData() instanceof GameObject) {
				GameObject obj = (GameObject)b.getUserData();
				if (!objects.contains(obj)) {
					objects.add(obj);
				}
			}
		}
		return objects;
	}
}
