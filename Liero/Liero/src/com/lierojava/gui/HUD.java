package com.lierojava.gui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.lierojava.Constants;
import com.lierojava.Utils;
import com.lierojava.client.GlobalState;
import com.lierojava.net.FontRenderProxy;
import com.lierojava.net.RenderProxy;
import com.lierojava.net.TextureRenderProxy;
import com.lierojava.participants.Player;

public class HUD {	
	public HUD() {
	}
	
	public ArrayList<RenderProxy> render() {
		ArrayList<RenderProxy> proxies = new ArrayList<RenderProxy>();
		
		// Render the game time.
		proxies.add(new FontRenderProxy(String.format("%.0f", GlobalState.currentGame.timeRemaining), "HUD_CLOCK", new Vector2(0, Gdx.graphics.getHeight() * 0.5f - 5), true));
		
		TextureRegion crosshairTexture = Constants.TEXTURES.findRegion("crosshair");
		TextureRegion healthBorderTexture = Constants.TEXTURES.findRegion("health_border");
		TextureRegion healthBarTexture = Constants.TEXTURES.findRegion("health_bar");
		
		for (Player player : GlobalState.currentGame.players) {
			Vector2 playerPosition = player.getBody().getPosition();
			
			// Player name.
			proxies.add(new FontRenderProxy(player.stats.name, "HUD_PLAYERNAME", new Vector2(playerPosition.x, playerPosition.y + 36), true));
			
			// Health bar.
			proxies.add(new TextureRenderProxy("health_border", new Vector2(playerPosition.x - healthBorderTexture.getRegionWidth() / 2, playerPosition.y + healthBorderTexture.getRegionHeight() + 10)));
			proxies.add(new TextureRenderProxy("health_bar", new Vector2(playerPosition.x - healthBarTexture.getRegionWidth() / 2, playerPosition.y + healthBarTexture.getRegionHeight() + 10), new Vector2(healthBarTexture.getRegionWidth() * player.getHealth() / 100f, healthBarTexture.getRegionHeight())));
			
			// Weapon icon.
			if (player.showWeapon) { 
				TextureRegion t = Constants.TEXTURES.findRegion(player.currentWeapon.icon);
				proxies.add(new TextureRenderProxy(player.currentWeapon.icon, new Vector2(playerPosition.x - t.getRegionWidth() / 2, playerPosition.y + 20)));
			}
			
			// Crosshair.
			Vector2 aimOffset = Utils.angleToVector(player.getAim());
			aimOffset.scl(Constants.CROSSHAIR_OFFSET);
			Vector2 size = new Vector2(crosshairTexture.getRegionWidth(), crosshairTexture.getRegionHeight());
			proxies.add(new TextureRenderProxy("crosshair", new Vector2(playerPosition.x + aimOffset.x - size.x / 2, playerPosition.y + aimOffset.y - size.y / 2), size));
		}
		
		// Set z-index.
		for (RenderProxy rp : proxies) {
			rp.zindex = 10;
		}
		
		return proxies;
	}
}