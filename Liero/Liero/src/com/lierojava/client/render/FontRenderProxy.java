package com.lierojava.client.render;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.lierojava.Constants;

public class FontRenderProxy extends RenderProxy {
	/**
	 * The text to render.
	 */
	public String text;
	
	/**
	 * The font to render it with.
	 * This font must be declared in the Constants.FONTS hashmap.
	 */
	public String font;
	
	/**
	 * The position at which to render the text.
	 */
	public Vector2 position;
	
	/**
	 * Whether to put the center of the text at this position.
	 */
	public boolean center;
	
	public FontRenderProxy() {
		this("", "", Vector2.Zero);
	}
	public FontRenderProxy(String text, String font, Vector2 position) {
		this(text, font, position, false);
	}
	public FontRenderProxy(String text, String font, Vector2 position, boolean center) {
		this.text = text;
		this.font = font;
		this.position = position;
		this.center = center;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		BitmapFont font = Constants.SKIN.getFont(this.font);
		Vector2 pos = position;
		if (center) {
			TextBounds tb = font.getBounds(text);
			pos = new Vector2(position);
			pos.x -= tb.width / 2;
			pos.y -= tb.height / 2;
		}
		font.draw(batch, text, pos.x, pos.y);
	}
}
