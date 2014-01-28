package com.lierojava.net;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.lierojava.Constants;

public class TextureRenderProxy extends RenderProxy {
	/**
	 * The name of the textureregion of the object.
	 */
	public String textureRegion;
	
	/**
	 * The position of the object.
	 */
	public Vector2 position;
	
	/**
	 * The size of the object.
	 */
	public Vector2 size;
	
	/**
	 * The rotation of the object.
	 */
	public float angle;
	
	/**
	 * Whether to flip the texture in the X direction.
	 */
	public boolean flipX;
	
	/**
	 * Whether to flip the texture in the Y direction.
	 */
	public boolean flipY;
	
	/**
	 * Whether to center the texture on the position.
	 */
	public boolean center;
	
	/**
	 * Whether to draw the texture as repeating (as opposed to stretching it).
	 */
	public boolean repeat;
	
	public TextureRenderProxy() {
		this("", Vector2.Zero, Vector2.Zero);
	}
	public TextureRenderProxy(String textureRegion, Vector2 position) {
		this(textureRegion, position, new Vector2(Constants.TEXTURES.findRegion(textureRegion).getRegionWidth(), Constants.TEXTURES.findRegion(textureRegion).getRegionHeight()));
	}
	public TextureRenderProxy(String textureRegion, Vector2 position, Vector2 size) {
		this(textureRegion, position, size, 0);
	}
	public TextureRenderProxy(String textureRegion, Vector2 position, Vector2 size, float angle) {
		this(textureRegion, position, size, angle, false, false);
	}
	public TextureRenderProxy(String textureRegion, Vector2 position, Vector2 size, float angle, boolean flipX, boolean flipY) {
		this(textureRegion, position, size, angle, flipX, flipY, false);
	}
	public TextureRenderProxy(String textureRegion, Vector2 position, Vector2 size, float angle, boolean flipX, boolean flipY, boolean center) {
		this(textureRegion, position, size, angle, flipX, flipY, center, false);
	}
	public TextureRenderProxy(String textureRegion, Vector2 position, Vector2 size, float angle, boolean flipX, boolean flipY, boolean center, boolean repeat) {
		this.textureRegion = textureRegion;
		this.position = position;
		this.size = size;
		this.angle = angle;
		this.flipX = flipX;
		this.flipY = flipY;
		this.center = center;
		this.repeat = repeat;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion tr = new TextureRegion(Constants.TEXTURES.findRegion(textureRegion));
		tr.flip(flipX, flipY);
		Vector2 pos = this.position;
		if (center) {
			pos = new Vector2(pos);
			pos.x -= size.x / 2;
			pos.y -= size.y / 2;
		}
		if (repeat) {
			for (int x = 0; x < size.x; x += tr.getRegionWidth()) {
				for (int y = 0; y < size.y; y += tr.getRegionHeight()) {
					batch.draw(tr, pos.x + x, pos.y + y, 0, 0, Math.min(tr.getRegionWidth(), size.x - x), Math.min(tr.getRegionHeight(), size.y - y), 1, 1, angle);
				}
			}
		}
		else {
			batch.draw(tr, pos.x, pos.y, 0, 0, size.x, size.y, 1, 1, angle);
		}
	}
}
