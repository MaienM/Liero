package com.lierojava.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.lierojava.Constants;
import com.lierojava.GlobalState;
import com.lierojava.MainGame;
import com.lierojava.Utils;
import com.lierojava.participants.Player;

public class HUD {
	/**
	 * The game to which this HUD belongs.
	 */
	private MainGame game;
	
	/**
	 * The font for drawing text on the screen. 
	 */
	private static BitmapFont font = new BitmapFont();

	/**
	 * The crosshair texture.
	 */
	private static Texture crosshairTexture = new Texture(Gdx.files.internal("textures/crosshair.png"));
	
	/**
	 * The crosshair texture.
	 */
	private static Texture healthBorderTexture = new Texture(Gdx.files.internal("textures/healthBorder.png"));
	
	/**
	 * The crosshair texture.
	 */
	private static Texture healthTexture = new Texture(Gdx.files.internal("textures/healthBar.png"));
	
	public HUD(MainGame game) {
		this.game = game;
	}
	
	public void render(SpriteBatch batch) {
		for (Player player : GlobalState.currentGame.players) {
			// Render the crosshair.
			Vector2 playerPosition = player.getBody().getPosition();
			Vector2 aimOffset = Utils.angleToVector(player.getAim());
			aimOffset.scl(Constants.CROSSHAIR_OFFSET);
			Vector2 size = new Vector2(crosshairTexture.getWidth(), crosshairTexture.getHeight());
			size.scl(Constants.CROSSHAIR_SCALE);
			batch.draw(crosshairTexture, playerPosition.x + aimOffset.x - size.x / 2, playerPosition.y + aimOffset.y - size.y / 2, size.x, size.y);
			
			// TODO: Render the weapon icons.
			// TODO: Render the weapon data (reload, charge, etc).
			// TODO: Render the game data (time, etc).
			
			// Health bar.
			batch.draw(healthBorderTexture, playerPosition.x - healthBorderTexture.getWidth() / 2, playerPosition.y + healthBorderTexture.getHeight() + 10, healthBorderTexture.getWidth(), healthBorderTexture.getHeight());
			batch.draw(healthTexture, playerPosition.x - healthTexture.getWidth() / 2, playerPosition.y + healthTexture.getHeight() + 10, healthTexture.getWidth() * player.getHealth() / 100f, healthTexture.getHeight());
		}
	}
}