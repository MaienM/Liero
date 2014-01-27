package com.lierojava.net;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class RenderProxy {
	/**
	 * Render this object.
	 * @param batch The spritebatch to render it with.
	 */
	public abstract void render(SpriteBatch batch);
}
