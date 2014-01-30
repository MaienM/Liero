package com.lierojava.client.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class RenderProxy implements Comparable<RenderProxy> {
	/**
	 * The Z-index of this renderproxy.
	 * Higher values are drawn later = on top.
	 */
	public int zindex = 0;
	
	/**
	 * Render this object.
	 * @param batch The spritebatch to render it with.
	 */
	public abstract void render(SpriteBatch batch);
	
	@Override
    public int compareTo(RenderProxy other) {
        return Double.compare(this.zindex, other.zindex);
    }
}
