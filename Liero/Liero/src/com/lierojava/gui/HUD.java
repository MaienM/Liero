package com.lierojava.gui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.lierojava.Constants;
import com.lierojava.GlobalState;
import com.lierojava.Utils;
import com.lierojava.net.RenderProxy;
import com.lierojava.participants.Player;

public class HUD {	
	public HUD() {
	}
	
	public ArrayList<RenderProxy> render() {
		ArrayList<RenderProxy> proxies = new ArrayList<RenderProxy>();
		
		TextureRegion crosshairTexture = Constants.TEXTURES.findRegion("crosshair");
		TextureRegion healthBorderTexture = Constants.TEXTURES.findRegion("health_border");
		TextureRegion healthBarTexture = Constants.TEXTURES.findRegion("health_bar");
		
		for (Player player : GlobalState.currentGame.players) {
			// Render the crosshair.
			Vector2 playerPosition = player.getBody().getPosition();
			Vector2 aimOffset = Utils.angleToVector(player.getAim());
			aimOffset.scl(Constants.CROSSHAIR_OFFSET);
			Vector2 size = new Vector2(crosshairTexture.getRegionWidth(), crosshairTexture.getRegionHeight());
			
			proxies.add(new RenderProxy("crosshair", new Vector2(playerPosition.x + aimOffset.x - size.x / 2, playerPosition.y + aimOffset.y - size.y / 2), size));
			
			// TODO: Render the weapon data (reload, charge, etc).
			// TODO: Render the game data (time, etc).
			
			// Health bar.
			proxies.add(new RenderProxy("health_border", new Vector2(playerPosition.x - healthBorderTexture.getRegionWidth() / 2, playerPosition.y + healthBorderTexture.getRegionHeight() + 10)));
			proxies.add(new RenderProxy("health_bar", new Vector2(playerPosition.x - healthBarTexture.getRegionWidth() / 2, playerPosition.y + healthBarTexture.getRegionHeight() + 10), new Vector2(healthBarTexture.getRegionWidth() * player.getHealth() / 100f, healthBarTexture.getRegionHeight())));
			
			// Weapon icon.
			if (player.showWeapon) { 
				TextureRegion t = Constants.TEXTURES.findRegion(player.currentWeapon.icon);
				proxies.add(new RenderProxy(player.currentWeapon.icon, new Vector2(playerPosition.x - t.getRegionWidth() / 2, playerPosition.y + 20)));
			}
		}
		
		return proxies;
	}
}