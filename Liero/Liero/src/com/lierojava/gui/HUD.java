package com.lierojava.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.lierojava.Constants;
import com.lierojava.GlobalState;
import com.lierojava.Utils;
import com.lierojava.participants.Player;

public class HUD {
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
	
	public HUD() {
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
			
			// TODO: Render the weapon data (reload, charge, etc).
			// TODO: Render the game data (time, etc).
			
			// Health bar.
			batch.draw(healthBorderTexture, playerPosition.x - healthBorderTexture.getWidth() / 2, playerPosition.y + healthBorderTexture.getHeight() + 10, healthBorderTexture.getWidth(), healthBorderTexture.getHeight());
			batch.draw(healthTexture, playerPosition.x - healthTexture.getWidth() / 2, playerPosition.y + healthTexture.getHeight() + 10, healthTexture.getWidth() * player.getHealth() / 100f, healthTexture.getHeight());
			
			// Weapon icon.
			if (player.showWeapon) {
				Texture t = player.currentWeapon.icon; 
				batch.draw(t, playerPosition.x - t.getWidth() / 2, playerPosition.y + 20, t.getWidth(), t.getHeight());
			}
		}
	}
}