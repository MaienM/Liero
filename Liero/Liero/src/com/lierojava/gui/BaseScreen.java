package com.lierojava.gui;

import java.io.IOException;

import org.apache.commons.lang3.text.WordUtils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.esotericsoftware.kryonet.Client;
import com.lierojava.Constants;
import com.lierojava.Utils;

public abstract class BaseScreen implements Screen {
	/**
	 * The spritebatch, for rendering.
	 */
    protected SpriteBatch batch;
    
    /**
     * The stage for this ui.
     */
    protected Stage stage;
    
    /**
     * The main layout table.
     */
    protected Table table;
    
    /**
     * The game.
     */
    protected Game game;
    
    /**
     * The scaled screen size.
     */
    protected Vector2 screen;
    
    /**
     * The background image.
     */
    protected Image bg;
    
    public BaseScreen(Game game) {
    	this.game = game;
    }
    
    public void show(float factor) {
    	// Setup.
    	batch = new SpriteBatch();
    	stage = new Stage();
    	   	
        // Setup the background.
        bg = new Image(Constants.TEXTURE_BACKGROUND);
        bg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(bg);
        
        // Setup the table.        
    	table = new Table();
        screen = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	float scale = screen.x / factor;
    	table.setFillParent(true);
    	table.setTransform(true);
    	table.setOrigin(screen.x / 2, screen.y / 2);
    	table.setScale(scale);
    	stage.addActor(table);
        screen.scl(1 / scale);
        
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {                
        stage.act(delta);
        batch.begin();
        stage.draw();
        batch.end();
    }
    
    @Override
    public void dispose() {
        batch.dispose();
    }

	@Override
	public void hide() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}
    
    /**
     * Show a simple dialog.
     * @param title The dialog title.
     * @param text The dialog text.
     */
    public void showDialog(String title, String text) {
    	showDialog(title, text, "OK");
    }
    
    /**
     * Show a simple dialog.
     * @param title The dialog title.
     * @param text The dialog text.
     * @param buttons The buttons to show.
     */
    public void showDialog(String title, String text, String... buttons) {    	
    	// Create the dialog.
    	Dialog d = new Dialog(title, Constants.SKIN) {
			@Override
			protected void result(Object obj) {
				this.setVisible(false);
			}
		};
		d.setModal(true);
		d.setMovable(false);
		
		// Create the label.
		text = WordUtils.wrap(text, 30);
		LabelStyle textStyle = Constants.SKIN.get("label-dialog", LabelStyle.class);
		d.text(new Label(text, textStyle));
		
		// Add the buttons.
		for (int i = 0; i < buttons.length; i++) {
			TextButton b = new TextButton(buttons[i], Constants.SKIN, "dialog");
			d.button(b, i);
		}
		
		// Set the size.
		TextBounds size = textStyle.font.getMultiLineBounds(text);
		size.width = Math.max(size.width, d.getTitleWidth());
		d.setBounds((Gdx.graphics.getWidth() - size.width - 20) / 2 , (Gdx.graphics.getHeight() - size.height - 120) / 2, size.width + 20, size.height + 120);
		
		stage.addActor(d);
    }
    
	/**
	 * Get a connection to the server.
	 * @throws IOException 
	 */
	public Client connectServer() {
		Client kryoClient = new Client();
		kryoClient.start();
		try {
			kryoClient.connect(5000, Constants.SERVER_HOST, Constants.SERVER_PORT);
		} 
		catch (IOException e) {
			showDialog("Server not reachable", "The server is not responding. Please try again later.");
		}
		kryoClient.setTimeout(0);
		Utils.setupKryo(kryoClient.getKryo());
		return kryoClient;
	}
}