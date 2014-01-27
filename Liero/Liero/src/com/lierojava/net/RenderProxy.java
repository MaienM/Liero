package com.lierojava.net;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.lierojava.Constants;

public class RenderProxy {
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
	
	public RenderProxy() {
		this("", Vector2.Zero, Vector2.Zero);
	}
	public RenderProxy(String textureRegion, Vector2 position) {
		this(textureRegion, position, new Vector2(Constants.TEXTURES.findRegion(textureRegion).getRegionWidth(), Constants.TEXTURES.findRegion(textureRegion).getRegionHeight()));
	}
	public RenderProxy(String textureRegion, Vector2 position, Vector2 size) {
		this(textureRegion, position, size, 0);
	}
	public RenderProxy(String textureRegion, Vector2 position, Vector2 size, float angle) {
		this(textureRegion, position, size, angle, false, false);
	}
	public RenderProxy(String textureRegion, Vector2 position, Vector2 size, float angle, boolean flipX, boolean flipY) {
		this.textureRegion = textureRegion;
		this.position = position;
		this.size = size;
		this.angle = angle;
		this.flipX = flipX;
		this.flipY = flipY;
	}
	
	public void render(SpriteBatch batch) {
		TextureRegion tr = new TextureRegion(Constants.TEXTURES.findRegion(textureRegion));
		tr.flip(flipX, flipY);
		batch.draw(tr, position.x, position.y, 0, 0, size.x, size.y, 1, 1, angle);
	}
}
