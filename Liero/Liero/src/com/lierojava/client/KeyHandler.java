package com.lierojava.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

public class KeyHandler {
	/**
	 * The keys to which this handler will respond.
	 */
	public int[] keys = new int[0];
	
	/**
	 * The minimum interval between two key presses.
	 */
	public float interval = 0;
	
	/**
	 * The last time this key was fired.
	 */
	private long lastFireTime = 0;
	
	public KeyHandler(int... keys) {
		this(0, keys);
	}
	public KeyHandler(float interval, int... keys) {
		this.interval = interval;
		this.keys = keys;
	}
	
	/**
	 * Whether the key is pressed.
	 * @return
	 */
	public boolean isPressed() {
		long currentTime = TimeUtils.millis();
		if (lastFireTime + (long)(interval * 1000) > currentTime) {
			return false;
		}
		for (int key : keys) {
			if (Gdx.input.isKeyPressed(key)) {
				lastFireTime = currentTime;
				return true;
			}
		}
		return false;
	}
}
