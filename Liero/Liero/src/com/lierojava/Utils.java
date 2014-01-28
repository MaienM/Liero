package com.lierojava;

import java.util.ArrayList;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterable;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.LongMap.Values;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.lierojava.bullets.Bullet;
import com.lierojava.bullets.PistolBullet;
import com.lierojava.client.GlobalState;
import com.lierojava.client.MainGame;
import com.lierojava.client.MainGameContactListener;
import com.lierojava.enums.GameState;
import com.lierojava.gameobjects.GameObject;
import com.lierojava.gameobjects.StaticBarrier;
import com.lierojava.gui.HUD;
import com.lierojava.net.handles.HostServer;
import com.lierojava.net.handles.ParticipantHost;
import com.lierojava.net.handles.ParticipantServer;
import com.lierojava.net.handshake.HostHandshake;
import com.lierojava.net.handshake.ServerHandshake;
import com.lierojava.net.interfaces.IHostHandshake;
import com.lierojava.net.interfaces.IHostServer;
import com.lierojava.net.interfaces.IParticipantHost;
import com.lierojava.net.interfaces.IParticipantServer;
import com.lierojava.net.interfaces.IServerHandshake;
import com.lierojava.participants.Participant;
import com.lierojava.participants.Player;
import com.lierojava.participants.Spectator;
import com.lierojava.userdata.SimpleUserData;
import com.lierojava.weapons.Jetpack;
import com.lierojava.weapons.Pistol;
import com.lierojava.weapons.Weapon;




public class Utils {
	/**
	 * The class list to register with kryo instances
	 */
	public final static Class<?>[] classes = new Class<?>[] {
		IParticipantHost.class,
		IParticipantServer.class,
		IHostServer.class,
		IHostHandshake.class,
		IServerHandshake.class,
		
		ParticipantHost.class,
		ParticipantServer.class,
		HostServer.class,
		HostHandshake.class,
		ServerHandshake.class,

		Participant.class,
		Player.class,
		Player.PlayerState.class,
		Spectator.class,
		Chatroom.class,
		PlayerData.class,
		GameState.class,
		StaticBarrier.class,
		SimpleUserData.class,
		Weapon.class,
		Pistol.class,
		Jetpack.class,
		Bullet.class,
		PistolBullet.class,
		MainGame.class,
		MainGameContactListener.class,
		HUD.class,
		
		ArrayList.class,
		Class.class,
		StringBuffer.class,
  		Locale.class,
  		String.class,
  		byte[].class,
  		byte.class,
  		char[].class,
  		char.class,
  		short[].class,
  		short.class,
  		long[].class,
  		long.class,
  		int[].class,
  		int.class,
  		float.class,
  		float[].class,
  		boolean[].class,
  		boolean.class,
		
		World.class,
		WorldManifold.class,
		Body.class,
		Array.class,
		ArrayIterable.class,
		ArrayIterator.class,
		Fixture.class,
		Filter.class,
		Vector2.class,
		Vector2[].class,
		MassData.class,
		Transform.class,
		LongMap.class,
		Values.class,
		PolygonShape.class,
		Contact.class,
		ContactListener.class,
		Screen.class,
		
		java.lang.RuntimeException.class,
		java.lang.StackTraceElement[].class,
		java.lang.StackTraceElement.class,
		
		com.badlogic.gdx.Audio.class,
		com.badlogic.gdx.audio.Sound.class,
	};
	
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
	 * Registers classes with a kryo instance
	 * @param kryo The Kryo instance to register the classes with
	 */
	public static void setupKryo(Kryo kryo) {
		kryo.setReferences(true);
		kryo.setRegistrationRequired(false);
		ObjectSpace.registerClasses(kryo);
		for (Class<?> cls : classes) {
			kryo.register(cls);
		}
		//kryo.setReferenceResolver(new com.esotericsoftware.kryo.util.MapReferenceResolver());
		//kryo.setDefaultSerializer(com.esotericsoftware.kryo.serializers..class);
	}

	/*
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
