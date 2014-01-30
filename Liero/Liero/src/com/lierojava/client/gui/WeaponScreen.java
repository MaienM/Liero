package com.lierojava.client.gui;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lierojava.Constants;
import com.lierojava.client.MainGame;
import com.lierojava.combat.weapons.Grenade;
import com.lierojava.combat.weapons.Laser;
import com.lierojava.combat.weapons.Pistol;
import com.lierojava.combat.weapons.Rocket;
import com.lierojava.combat.weapons.Shotgun;
import com.lierojava.combat.weapons.Weapon;

public class WeaponScreen extends BaseScreen {	
	/**
	 * The guns that have been picked.
	 */
	private ArrayList<Class<? extends Weapon>> pickedGuns = new ArrayList<Class<? extends Weapon>>();
	
	/**
	 * The host we're joining.
	 */
	private String host;
	
	/**
	 * The port at which we're joining the host.
	 */
	private int port;
	
	public WeaponScreen(Game game, String host, int port) {
		super(game);
		this.host = host;
		this.port = port;
	}
	
	@Override
	public void show() {
		show(1200);
		
		// Title.
		table.add(new Label("Pick 3", Constants.SKIN, "label-title")).colspan(11).fillY().expandY();
		table.row();
		
		// HSpacer.
		table.add().fillX().expandX();
		
		// Pistol.
		final Image btnPistol = new Image(Constants.TEXTURES.findRegion("icon_gun"));
		btnPistol.addListener(new ClickListener() {
			@Override 
            public void clicked(InputEvent event, float x, float y){
				handleClick(event, Pistol.class);
            }
		});
		table.add(btnPistol).fill().expand();
		
		// HSpacer.
		table.add().fillX().expandX();
		
		// Shotgun.
		final Image btnShotgun = new Image(Constants.TEXTURES.findRegion("icon_shotgun"));
		btnShotgun.addListener(new ClickListener() {
			@Override 
            public void clicked(InputEvent event, float x, float y){
				handleClick(event, Shotgun.class);
            }
		});
		table.add(btnShotgun).fill().expand();	
		
		// HSpacer.
		table.add().fillX().expandX();
		
		// Grenade
		final Image btnGrenade = new Image(Constants.TEXTURES.findRegion("icon_grenade"));
		btnGrenade.addListener(new ClickListener() {
			@Override 
            public void clicked(InputEvent event, float x, float y){
				handleClick(event, Grenade.class);
            }
		});
		table.add(btnGrenade).fill().expand();
		
		// HSpacer.
		table.add().fillX().expandX();
		
		// Laser
		final Image btnLaser = new Image(Constants.TEXTURES.findRegion("icon_lasergun"));
		btnLaser.addListener(new ClickListener() {
			@Override 
            public void clicked(InputEvent event, float x, float y){
				handleClick(event, Laser.class);
            }
		});
		table.add(btnLaser).fill().expand();
		
		// HSpacer.
		table.add().fillX().expandX();
		
		// Laser
		final Image btnRocket = new Image(Constants.TEXTURES.findRegion("icon_rocket"));
		btnRocket.addListener(new ClickListener() {
			@Override 
            public void clicked(InputEvent event, float x, float y){
				handleClick(event, Rocket.class);
            }
		});
		table.add(btnRocket).fill().expand();
		
		// HSpacer.
		table.add().fillX().expandX();
		table.row();		
		
		// VSpacer.
    	table.add().colspan(3).fillY().expandY();
		
		// Start button.
    	final TextButton btnStart = new TextButton("Start", Constants.SKIN);
        table.add(btnStart).colspan(5).fillX().height(screen.y / 10);
        
        btnStart.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	if (pickedGuns.size() != 3) {
            		showDialog("Pick 3 weapons", "You must pick exactly 3 weapons.");
            		return;
            	}
            	
            	game.setScreen(new MainGame(game, host, port, pickedGuns));
            }
        });
	}
	
	private void handleClick(InputEvent event, Class<? extends Weapon> weaponClass) {
		if (pickedGuns.contains(weaponClass)) {
			pickedGuns.remove(weaponClass);
			event.getListenerActor().setColor(Color.WHITE);
		}
		else if (pickedGuns.size() < 3) {
			pickedGuns.add(weaponClass);
			event.getListenerActor().setColor(Color.YELLOW);
		}
	}
}
