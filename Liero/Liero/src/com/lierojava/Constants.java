package com.lierojava;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class Constants {
	/**
	 * The port over which the server communicates.
	 */
	public static final int PORT = 29992;
	
	/**
	 * The port the main/master server runs on
	 */
	public static final int SERVER_PORT = 34534;
	
	/**
	 * The host where the master server is located
	 */
	public static final String SERVER_HOST = "127.0.0.1";
	
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
	public static final float PLAYER_ANIMATION_SPEED = 0.03f;
	
	/**
	 * The animation speed of the moving worm.
	 */
	public static final int PLAYER_ANIMATION_SIZE = 13;

	/**
	 * The scale of the player textures on screen.
	 */
	public static final float PLAYER_TEXTURE_SCALE = 2f;

	/**
	 * The main textureatlas.
	 */
	public static final TextureAtlas TEXTURES = new TextureAtlas(Gdx.files.internal("textures/textures.pack"));
	
	/**
	 * The main skin.
	 */
	public static final Skin SKIN = new Skin() {{	
		// Insert fonts.
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Cousine-Regular-Latin.ttf"));
		FreeTypeFontGenerator boldGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Cousine-Bold-Latin.ttf"));
		add("default-font", generator.generateFont(14));
		add("font-gui-window-title", generator.generateFont(50));
		add("font-gui-window-title-small", generator.generateFont(30));
		add("font-gui-dialog-title", generator.generateFont(20));
		add("font-hud-playername", generator.generateFont(12)); 
		add("font-hud-clock", generator.generateFont(36)); 
		add("font-score-header", boldGenerator.generateFont(16));
		add("font-score-data", generator.generateFont(16));
		generator.dispose();
		boldGenerator.dispose();
		
		// Load rest of the skin.
		FileHandle skinFile = Gdx.files.internal("ui/skin.json");
		FileHandle atlasFile = skinFile.sibling(skinFile.nameWithoutExtension() + ".atlas");
		if (atlasFile.exists()) {
			TextureAtlas atlas = new TextureAtlas(atlasFile);
			addRegions(atlas);
		}
		load(skinFile);
	}};
	//public static Skin SKIN = new Skin(Gdx.files.internal("ui/skin.json"));
	
	/**
	 * The background texture.
	 */
	public static final Texture TEXTURE_BACKGROUND = new Texture(Gdx.files.internal("textures/background.png"));
	
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
	 * The dimensions of a ground block for both x and y
	 */
	public static final int GROUND_SIZE = 25;
	
	/**
	 * The length of the password salt
	 */
	public static final int SALT_LENGTH = 32;
}
