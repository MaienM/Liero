package com.lierojava.gui;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lierojava.Constants;
import com.lierojava.client.GlobalState;
import com.lierojava.client.MainGame;
import com.lierojava.server.data.HostStruct;

public class LobbyScreen extends BaseScreen {
	/**
	 * The currently active games.
	 */
	private ArrayList<HostStruct> games;
	
	public LobbyScreen(Game game) {
		super(game);
	}
	
	@Override
    public void show() {   
    	super.show(600);
    	
    	// Left panel: stats + chat.
    	final Table tblLeft = new Table();
    	table.add(tblLeft).size(screen.x / 2, screen.y);
    
    	// Title.
    	final Label lblTitle = new Label("Welcome, " + GlobalState.ips.getName(), Constants.SKIN, "label-title-small");
    	tblLeft.add(lblTitle).colspan(2);
    	tblLeft.row();
    	
    	// Spacer.
    	tblLeft.add().fillY();
    	tblLeft.row();
    	
    	// Chat box.
    	final TextArea taChat = new TextArea("", Constants.SKIN);
    	taChat.setDisabled(true);
    	tblLeft.add(taChat).colspan(2).fill().expand();
    	tblLeft.row();
    	
    	final TextField tbChat = new TextField("", Constants.SKIN);
    	tbChat.setMessageText("Enter a chat message here");
    	tblLeft.add(tbChat).fillX().expandX();
    	
    	final TextButton btnChat = new TextButton("Send", Constants.SKIN);
    	tblLeft.add(btnChat);
    	tblLeft.row();
    	
    	btnChat.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y) {
            	String message = tbChat.getText();
            	if (message.isEmpty()) {
            		return;
            	}
            	tbChat.setText("");
            	taChat.setText(taChat.getText() + "\n" + message);
            }
        });
    	
    	// Right panel: game listing.
    	final Table tblRight = new Table();
    	table.add(tblRight).size(screen.x / 2, screen.y);
    	
    	// Game listing header.
    	final Label lblName = new Label("Select a game to join", Constants.SKIN);
    	tblRight.add(lblName);
    	tblRight.row();
    	
    	// Game listing.    	
    	final List listGames = new List(refreshHosts(), Constants.SKIN);
    	listGames.setColor(Color.BLACK);
    	
    	final ScrollPane scrollGames = new ScrollPane(listGames);
    	scrollGames.setScrollingDisabled(true, false);
    	
    	tblRight.add(scrollGames).fill().expand();
    	tblRight.row();
    	
    	// Join button.
    	final TextButton btnJoin = new TextButton("Join", Constants.SKIN);
        tblRight.add(btnJoin).fill();
        tblRight.row();
        
        btnJoin.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y) {
            	if (listGames.getSelectedIndex() < 0 || games.isEmpty()) {
            		showDialog("Select a game", "You must first select a game to join");
            		return;
            	}
            	
            	HostStruct host = games.get(listGames.getSelectedIndex());
            	game.setScreen(new WeaponScreen(game, host.host, host.port));
            }
        });
        
        // Spectate button.
    	final TextButton btnSpectate = new TextButton("Spectate", Constants.SKIN);
        tblRight.add(btnSpectate).fill();
        tblRight.row();
        
        btnSpectate.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y) {
            	if (listGames.getSelectedIndex() < 0 || games.isEmpty()) {
            		showDialog("Select a game", "You must first select a game to spectate");
            		return;
            	}
            	
            	HostStruct host = games.get(listGames.getSelectedIndex());
            	game.setScreen(new MainGame(game, host.host, host.port, null));
            }
        });
        
        // Host button.
    	final TextButton btnHost = new TextButton("Host", Constants.SKIN);
        tblRight.add(btnHost).fill();
        tblRight.row();
        
        btnHost.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	// TODO: Weapon select screen first.
				game.setScreen(new WeaponScreen(game, null, -1));
            }
        });
        
        // Refresh button.
    	final TextButton btnRefresh = new TextButton("Refresh", Constants.SKIN);
        tblRight.add(btnRefresh).fill();
        tblRight.row();
        
        btnRefresh.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	listGames.setItems(refreshHosts());
            }
        });
    }
	
	public String[] refreshHosts() {
		games = GlobalState.ips.getGames();
		String[] names = new String[games.size()];
		for (int i = 0; i < games.size(); i++) {
			names[i] = games.get(i).name;
		}
		return names;
	}
}
