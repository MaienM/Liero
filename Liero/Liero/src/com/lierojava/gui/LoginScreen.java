package com.lierojava.gui;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.lierojava.Constants;
import com.lierojava.Utils;
import com.lierojava.client.GlobalState;
import com.lierojava.enums.AccountState;
import com.lierojava.net.interfaces.IParticipantChat;
import com.lierojava.net.interfaces.IParticipantServer;
import com.lierojava.net.interfaces.IServerHandshake;
import com.lierojava.server.data.ParticipantIdentifier;

public class LoginScreen extends BaseScreen {
	public LoginScreen(Game game) {
		super(game);
	}

	@Override
    public void show() {   
    	super.show(340);

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
    	stage.setKeyboardFocus(tbUsername);
    	table.add(tbUsername).fill();
    	table.row();
    	
    	final TextField tbPassword = new TextField("", Constants.SKIN);
    	tbPassword.setMessageText("Password");
    	tbPassword.setPasswordCharacter('*');
    	tbPassword.setPasswordMode(true);
    	table.add(tbPassword).fill();
    	table.row();
    	
    	// Enter key.
    	InputListener enterListener = new InputListener() {
    		@Override
    		public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Keys.ENTER) {
					login(tbUsername.getText(), tbPassword.getText());
				}
				return false;
			}
    	};
    	
    	tbUsername.addListener(enterListener);
    	tbPassword.addListener(enterListener);
    	
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
    		showDialog("Username/password empty", "You have to enter an username and password.");
    		return false;
    	}
    	return true;
    }
    
	/**
	 * Connects to the server
	 * 
	 * @throws IOException
	 */
	public void login(String username, String password) {
		if (!validate(username, password)) {
    		return;
    	}

		// Connect to the server.
		Client kryoClient = connectServer();
		if (kryoClient == null) {
			return;
		}
		
		// Get server handshake
		IServerHandshake ish = ObjectSpace.getRemoteObject(kryoClient, 0, IServerHandshake.class);
		
		// Try to login, dbId is -1 if login fails
		int dbId = ish.login(username, password);
		
		// We managed to login, set some values
		if (dbId != -1) {
			// Get our interface to the server
			GlobalState.ips = ObjectSpace.getRemoteObject(kryoClient, dbId, IParticipantServer.class);
			
			// Workaround to be able to link a connection to an account
			ParticipantIdentifier ident = new ParticipantIdentifier();
			ident.dbId = dbId;
			kryoClient.sendTCP(ident);
			
			// Get the global chat handle
			int index = GlobalState.ips.getChatInstance();
			GlobalState.ipc = ObjectSpace.getRemoteObject(kryoClient, index, IParticipantChat.class);
			
			// Go to the lobby.
			Gdx.app.getApplicationListener();
	    	game.setScreen(new LobbyScreen(game));
		}
		
		// We failed to login
		else  {
			kryoClient.close();
			showDialog("Login failed", "The entered username and/or password are not known, or you are already logged in elsewhere.");
		}
	}

    private void register(String username, String password) {
    	if (!validate(username, password)) {
    		return;
    	}
    	
    	Client kryoClient = new Client();
		kryoClient.start();
		try {
			kryoClient.connect(5000, Constants.SERVER_HOST, Constants.SERVER_PORT);
		} catch (IOException e) {
			showDialog("Server not reachable", "The server is not responding. Please try again later.");
			return;
		}
		kryoClient.setTimeout(0);
		Utils.setupKryo(kryoClient.getKryo());
		
		// Get server handshake
		IServerHandshake ish = ObjectSpace.getRemoteObject(kryoClient, 0, IServerHandshake.class);
		
		// Try to register.
		AccountState result = ish.register(username, password);
		
		// Close the connection.
		kryoClient.close();
		
		// Display the result.
		if (result == AccountState.USERNAMETAKEN) {
			showDialog("Username is taken", "The given username is already in use. Please choose another username.");
		}
		else {
			login(username, password);
			((BaseScreen)game.getScreen()).showDialog("Account created", "Your account was successfully created.");
		}
    }
}