package com.lierojava.userdata;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class PendingAction {
	public abstract void run(Body body);
}
