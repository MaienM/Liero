package com.lierojava.client;

import com.badlogic.gdx.Game;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.lierojava.client.gui.LoginScreen;


public class Liero extends Game {	
	@Override
	public void create() {
		GlobalState.objectSpace = new ObjectSpace();
		setScreen(new LoginScreen(this));
	}
}
