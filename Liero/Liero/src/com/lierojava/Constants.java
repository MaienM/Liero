package com.lierojava;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class Constants {
	/**
	 * Conversion from screen units to Box2D units.
	 */
	public static final float WORLD_TO_BOX = 0.01f;
	
	/**
	 * Conversion from Box2D units to screen units.
	 */
	public static final float BOX_TO_WORLD = 100f;
	
	/**
	 * The players horizontal movement speed.
	 */
	public static final float PLAYER_MOVEMENT_SPEED = 40f;
	
	/**
	 * The players jump speed.
	 */
	public static final float PLAYER_JUMP_SPEED = 35f;
	
	/**
	 * The players aim speed.
	 */
	public static final float PLAYER_AIM_SPEED = 0.05f;
	
	/**
	 * The animation speed of the moving worm.
	 */
	public static final float PLAYER_ANIMATION_SPEED = 0.1f;
	
	/**
	 * The main textureatlas.
	 */
	public static TextureAtlas TEXTURES = new TextureAtlas(Gdx.files.internal("textures/textures.pack"));
	
	/**
	 * The scale of the textures on screen.
	 */
	public static final float TEXTURE_SCALE = 2f;

	/**
	 * The scale of the crosshair on screen.
	 */
	public static final float CROSSHAIR_SCALE = 0.25f;
	
	/**
	 * The distance between the player center and the croshair position.
	 */
	public static final float CROSSHAIR_OFFSET = 25f;
	
	/**
	 * The maximum number of weapons.
	 */
	public static final short MAX_WEAPONS = 3;
	
	/**
	 * The distance between the player center and the bullet spawn position.
	 */
	public static final float WEAPON_OFFSET = 22f;
	
	/**
	 * The size of the static barrier around the level.
	 */
	public static final float BARRIER_SIZE = 10f;
	
	/**
	 * The port over which the server communicates.
	 */
	public static final int PORT = 29992;
	
	/**
	 * The KryoNet buffer size.
	 */
	public static final int BUFFER_SIZE = 512 * 1024 * 1024;
	
	/**
	 * The texture used to display the ground
	 */
	public static Texture GROUND_TEXTURE = new Texture(Gdx.files.internal("textures/ground.jpg"));
	
	/**
	 * The Dimensions of a ground block for both x and y
	 */
	public static final int GROUND_DIM = 25;
	
	public static final Texture ICON_PISTOL = new Texture(Gdx.files.internal("textures/icon_gun.png"));
	public static final Texture ICON_SHOTGUN = new Texture(Gdx.files.internal("textures/icon_shotgun.png"));
	public static final Texture ICON_GRENADE = new Texture(Gdx.files.internal("textures/icon_grenade.png"));
}
