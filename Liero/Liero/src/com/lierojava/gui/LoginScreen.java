package com.lierojava.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lierojava.Constants;
import com.lierojava.Utils;

public class LoginScreen extends BaseScreen {
	public LoginScreen(Game game) {
		super(game);
	}

	@Override
    public void show() {   
    	super.show(400);
    	
    	Utils.print("Login");

    	// Spacer;
    	table.add().expandY();
    	table.row();
    	
    	// Title.
    	final Label lblTitle = new Label("Login", Constants.SKIN, "label-title");
    	table.add(lblTitle);
    	table.row();

    	// Spacer.
    	table.add().height(10);
    	table.row();
    	
    	// Text fields.
    	final TextField tbUsername = new TextField("", Constants.SKIN);
    	tbUsername.setMessageText("Username");
    	table.add(tbUsername).fill();
    	table.row();
    	
    	final TextField tbPassword = new TextField("", Constants.SKIN);
    	tbPassword.setMessageText("Password");
    	tbPassword.setPasswordCharacter('*');
    	tbPassword.setPasswordMode(true);
    	table.add(tbPassword).fill();
    	table.row();
    	
    	// Spacer.
    	table.add().height(4);
    	table.row();
    	
    	// Login button.
    	final TextButton btnLogin = new TextButton("Login", Constants.SKIN);
        table.add(btnLogin).fill();
        table.row();
        
        btnLogin.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	login(tbUsername.getText(), tbPassword.getText());
            }
        });
        
        // Register button.
        final TextButton btnRegister = new TextButton("Register", Constants.SKIN);
        table.add(btnRegister).fill();
        table.row();
        
        btnRegister.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	register(tbUsername.getText(), tbPassword.getText());
            }
        });

    	// Spacer;
        table.add().expandY();
    }
    
    private boolean validate(String username, String password) {
    	if (username.isEmpty() || password.isEmpty()) {
    		// TODO: Scale the dialog properly.
    		stage.addActor(
    			new Dialog("Invalid username/password", Constants.SKIN) {
    				{
    					this.setBounds(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    				}
    				
    				@Override
    				protected void result(Object obj) {
    					this.setVisible(false);
    				}
    			}
	    			.text("The username and password can not be empty")
	    			.button("OK", true)
            );
    		return false;
    	}
    	return true;
    }
    
    private void login(String username, String password) {
    	if (!validate(username, password)) {
    		return;
    	}
    	
    	Gdx.app.getApplicationListener();
    	game.setScreen(new LobbyScreen(game));
    }

    private void register(String username, String password) {
    	if (!validate(username, password)) {
    		return;
    	}
    }
}