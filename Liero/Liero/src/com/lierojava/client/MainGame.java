package com.lierojava.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.kryonet.rmi.TimeoutException;
import com.lierojava.Constants;
import com.lierojava.PlayerData;
import com.lierojava.Utils;
import com.lierojava.bullets.Bullet;
import com.lierojava.gameobjects.GameObject;
import com.lierojava.gameobjects.Ground;
import com.lierojava.gameobjects.StaticBarrier;
import com.lierojava.gui.BaseScreen;
import com.lierojava.gui.HUD;
import com.lierojava.gui.LobbyScreen;
import com.lierojava.net.handles.ParticipantHostPlayer;
import com.lierojava.net.handshake.HostHandshake;
import com.lierojava.net.interfaces.IHostHandshake;
import com.lierojava.net.interfaces.IHostServer;
import com.lierojava.net.interfaces.IParticipantHost;
import com.lierojava.participants.Player;
import com.lierojava.render.RenderProxy;
import com.lierojava.server.data.HostStruct;
import com.lierojava.server.data.ParticipantIdentifier;
import com.lierojava.userdata.PendingAction;
import com.lierojava.userdata.SimpleUserData;
import com.lierojava.weapons.Weapon;

public class MainGame extends BaseScreen {
	/**
	 * The Box2D world.
	 */
	public volatile World world;
	
	/**
	 * Debug crap.
	 */
	private Box2DDebugRenderer debugRenderer;
	private boolean debug;
	
	/**
	 * The viewport camera.
	 */
	private OrthographicCamera camera;
	
	/**
	 * The HUD.
	 */
	private HUD hud = new HUD();
	
	/**
	 * The players.
	 */
	public ArrayList<Player> players = new ArrayList<Player>();
	
	/**
	 * The scores.
	 */
	public ArrayList<PlayerData> scores = new ArrayList<PlayerData>();
	
	/**
	 * The host. 
	 * If this is null it means we are the host.
	 */
	private String host;
	
	/**
	 * The port.
	 */
	private int port;
	
	/**
	 * The desired weapons.
	 */
	private ArrayList<Class<? extends Weapon>> weapons;

	/**
	 * Renders sprites.
	 */
	private SpriteBatch batch;
	
	/**
	 * The render proxies.
	 */
	public ArrayList<RenderProxy> renderProxies = new ArrayList<RenderProxy>();
	
	/**
	 * The IParticipantHost object, for communication to the host.
	 */
	public IParticipantHost iph;
	
	/**
	 * The IHostServer object, for communication to the global server.
	 */
	private IHostServer ihs;

	/**
	 * Whether we want the score window to be visible.
	 */
	private boolean showScore;
	
	/**
	 * How much time is remaining before the round ends.
	 */
	public float timeRemaining = Constants.GAME_DURATION;
	
	/**
	 * The key handlers.
	 */
	private KeyHandler nextWeaponKey = new KeyHandler(1f, Keys.PAGE_UP);
	private KeyHandler prevWeaponKey = new KeyHandler(1f, Keys.PAGE_DOWN);
	private KeyHandler toggleDebugKey = new KeyHandler(1f, Keys.F12);

	/**
	 * The chat.
	 */
	public ArrayList<String> chatMessages;

	/**
	 * Create a new game.
	 */
	public MainGame(Game game, String host, int port, ArrayList<Class<? extends Weapon>> weapons) {
		super(game);
		
		// Update global state.
		GlobalState.currentGame = this;
		
		// Save host info.
		this.host = host;
		this.port = port;
		this.weapons = weapons;
		
		// Create a new sprite batch.
		this.batch = new SpriteBatch();
	}

	/**
	 * Start a new game as host.
	 * @throws IOException
	 */
	private void startServer() {
		// Get a free port.
		try {
			ServerSocket s = new ServerSocket();
			s.bind(null);
			port = s.getLocalPort();
			s.close();
		} catch (IOException e1) {
			BaseScreen screen = new LobbyScreen(game);
			screen.showDialog("Could not find port", "Could not find a free port to use for hosting. Aborting.");
			game.setScreen(screen);
			return;
		}
		
		Server server = new Server(12800 * 1024, 1600 * 1024);
		Utils.setupKryo(server.getKryo());
		server.start();
		try {
			server.bind(port, port);
		} 
		catch (IOException e) {
			BaseScreen screen = new LobbyScreen(game);
			screen.showDialog("Port in use", "The port Liero uses for hosting servers is already in use. Are you already running a server? Aborting.");
			game.setScreen(screen);
			return;
		}
		
		server.addListener(new Listener() {
			@Override
			public void connected(final Connection connection) {
            	GlobalState.objectSpace.addConnection(connection);
			}

            @Override
			public void disconnected(Connection connection) {
            	Utils.print(connection);
            	Player leftPlayer = null;
            	for (Player p : players) {
            		Utils.print(p.connection);
            		if (p.connection == connection) {
            			leftPlayer = p;
            			break;
            		}
            	}
            	if (leftPlayer != null) {
            		players.remove(leftPlayer);
            		leftPlayer.getBody().setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
            	}
            }

            @Override
			public void received(Connection connection, Object object) {
            	GlobalState.lastSender = connection;
            	if (object instanceof ParticipantIdentifier) {
            		ParticipantIdentifier pi = (ParticipantIdentifier)object;
            		for (Player p : players) {
            			if (p.data.id == pi.dbId) {
            				p.connection = connection;
            			}
            		}
            	}
            }
        });
		
		// Setup the handshake object.
		IHostHandshake ihh = new HostHandshake();
		GlobalState.objectSpace.register(0, ihh);
		
		// Get an IParticipantHost object for the local player.
		ihh.requestParticipant(true, GlobalState.ips.getDatabaseId(), GlobalState.ips.getName(), weapons);
		iph = new ParticipantHostPlayer(players.get(0));
		
		// Connect to the global server.
		Client kryoClient = connectServer();
		if (kryoClient == null) {
			return;
		}
		
		// Register the host.
		HostStruct hs = new HostStruct(port, GlobalState.ips.getName());
		int ihsId = GlobalState.ips.addGame(hs);
		
		if (ihsId == -1) {
			BaseScreen screen = new LobbyScreen(game);
			screen.showDialog("Something went wrong", "Something went wrong trying to register with the server. Aborting.");
			game.setScreen(screen);
			return;
		}
		
		// Get the IHostServer object.
		ihs = ObjectSpace.getRemoteObject(kryoClient, ihsId, IHostServer.class);
	}
	
	/**
	 * Join an existing game as client. 
	 */
	private void startClient() {
		Client client = new Client(6400 * 1024, 1600 * 1024);
		Utils.setupKryo(client.getKryo());
		client.start();
		try {
			client.connect(5000, host, port, port);
		} catch (IOException e) {
			BaseScreen screen = new LobbyScreen(game);
			screen.showDialog("Conection failed", "Could not connect to host. Aborting.");
			game.setScreen(screen);
			return;
		}
		client.setTimeout(0);

		IHostHandshake ihh = ObjectSpace.getRemoteObject(client, 0, IHostHandshake.class);
		int index = ihh.requestParticipant(weapons != null, GlobalState.ips.getDatabaseId(), GlobalState.ips.getName(), weapons);
		if (index > 0) {
			iph = ObjectSpace.getRemoteObject(client, index, IParticipantHost.class);
			
			// Workaround to be able to link a connection to an account
			ParticipantIdentifier ident = new ParticipantIdentifier();
			ident.dbId = GlobalState.ips.getDatabaseId();
			client.sendTCP(ident);
			
			// Update time.
			timeRemaining = iph.getTimeRemaining();
		}
	}
	
	/**
	 * Gets executed once every frame.
	 * 
	 * Handles game updates, input and rendering.
	 */	
	@Override
	public void render(float delta) {
		// Update the time remaining.
		timeRemaining -= delta;
			
		if (timeRemaining > 0) {
			// Handle input.
			try {
				handleInput();
			}
			catch (TimeoutException e) {}
			
			// Perform a game step.
			if (host == null) {
				update();
			} 
			else {
				updateData();
			}
								
			// Render. 
			render();
		} 
		else {
			if (host == null) {
				update();
				for (PlayerData pd : scores) {
					ihs.savePlayerStats(pd.id, pd.kills, pd.deaths);
				}
			}
			super.render(delta);
		}
	}

	/**
	 * Process user input.
	 */
	private void handleInput() {
		// Score display.
		showScore = Gdx.input.isKeyPressed(Keys.TAB);
		
		if (iph == null) {
			return;
		}
		
		// Movement.
		if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
			iph.moveRight();
		}
		else if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
			iph.moveLeft();
		}
		else {
			iph.stopSidewaysMovement();
		}
		
		// Jumping.
		if (Gdx.input.isKeyPressed(Keys.DPAD_UP) || Gdx.input.isKeyPressed(Keys.W)) {
			iph.jump();
		}
		
		// Jetpack.
		if (Gdx.input.isKeyPressed(Keys.DPAD_DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
			iph.jetpack();
		}
		
		// Weapon switching.
		if (nextWeaponKey.isPressed()) {
			iph.setWeaponIndex((iph.getWeaponIndex() + 1) % Constants.MAX_WEAPONS);
		}
		if (prevWeaponKey.isPressed()) {
			iph.setWeaponIndex((Constants.MAX_WEAPONS + iph.getWeaponIndex() - 1) % Constants.MAX_WEAPONS);
		}
		if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
			iph.setWeaponIndex(0);
		}
		if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
			iph.setWeaponIndex(1);
		}
		if (Gdx.input.isKeyPressed(Keys.NUM_3)) {
			iph.setWeaponIndex(2);
		}
		
		// Weapon aiming.		
		if (Gdx.input.isKeyPressed(Keys.END) || Gdx.input.isKeyPressed(Keys.Q)) {
			iph.aimLeft();
		}
		if (Gdx.input.isKeyPressed(Keys.DEL) || Gdx.input.isKeyPressed(Keys.E)) {
			iph.aimRight();
		}
		
		// Weapon firing.
		if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.ENTER)) {
			iph.fire();
		}
		
		// Toggle the debug renderer.
		if (toggleDebugKey.isPressed()) {
			debug = !debug;
		}
	}

	/**
	 * Peform a game step.
	 */
	private void update() {
		// Update the time remaining.
		timeRemaining -= Gdx.graphics.getDeltaTime();
		
		synchronized(this) {
		// Perform a physics step.
			world.step(1f/30f, 8, 3);
			while(world.isLocked());
		}
		
		// Perform pending actions.
		executeActions();
		
		// Remove unwanted objects.
		removeBodies();

		// Update the list of render proxies.
		synchronized (renderProxies) {
			renderProxies.clear();
			for (GameObject obj : Utils.getGameObjects()) {
				ArrayList<RenderProxy> rp = obj.render();
				renderProxies.addAll(rp);
			}
			renderProxies.addAll(hud.render());
			Collections.sort(renderProxies);
		}
	}
	
	/**
	 * Get updated data from the host.
	 */
	private void updateData() {
		// Get the render data.
		ArrayList<RenderProxy> newRenderProxies = iph.getRenderProxies();
		if (newRenderProxies != null) {
			renderProxies = newRenderProxies;
		}
		
		// Get the scores.
		scores = iph.getScores();
	}

	/**
	 * Render the game.
	 */
	private void render() {
		// Clear the screen.
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Recalculate camera properties.
        camera.update();
        
        // Set the rendering offset.
        batch.setProjectionMatrix(camera.combined);
		
		// Begin rendering.
		batch.begin();
		
		// Draw the background.
		batch.draw(Constants.TEXTURE_BACKGROUND, -Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Render game objects.
		for (RenderProxy rp : renderProxies) {
			rp.render(batch);
		}
		
		// Render the score view.
		if (showScore) {
			renderScore(batch);
		}
		
		// Finish rendering.
		batch.end();	

		// Draw the debug view.
		if (debug) {
			debugRenderer.render(world, camera.combined);
		}
	}			
	
	/**
	 * Render the score.
	 * @param batch The spritebatch to use for rendering
	 */
	private void renderScore(SpriteBatch batch) {
		// Get the textures.		
		TextureRegion bg = Constants.TEXTURES.findRegion("background_score");
		TextureRegion even = Constants.TEXTURES.findRegion("background_score_even");
		TextureRegion odd = Constants.TEXTURES.findRegion("background_score_odd");
		
		// Get the fonts.
		BitmapFont headerFont = Constants.SKIN.getFont("font-score-header");
		BitmapFont dataFont = Constants.SKIN.getFont("font-score-data");
		
		// Get width and height.
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		
		// Draw background and title.
		batch.draw(bg, width * -0.4f, height * -0.4f, width * 0.8f, height * 0.8f);
		headerFont.draw(batch, "Player", width * -0.4f + 20, height * 0.4f - 20);
		headerFont.draw(batch, "Kills", width * 0.2f, height * 0.4f - 20);
		headerFont.draw(batch, "Deaths", width * 0.3f, height * 0.4f - 20);
		
		// Draw the scores.
		int i = 0;
		for (PlayerData pd : scores) {
			batch.draw(i % 2 == 0 ? even : odd, width * -0.4f + 16, height * 0.4f - 60 - 20 * i, width * 0.8f - 32, 20);
			dataFont.draw(batch, pd.name, width * -0.4f + 20, height * 0.4f - 44 - 20 * i);
			dataFont.draw(batch, pd.kills + "", width * 0.2f, height * 0.4f - 44 - 20 * i);
			dataFont.draw(batch, pd.deaths + "", width * 0.3f, height * 0.4f - 44 - 20 * i);
			
			i++;
		}
	}		

	@Override
	public void show() {
		// Setup GUI.
		super.show(1024);
		setupUI();
		
		// Create the camera.
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// If not host, connect to the host and stop.
		if (host != null) {
			startClient();
			return;
		}
		
		// Create the world.
		world = new World(new Vector2(0, -10), true);
		new StaticBarrier();
		fillWorld();
		
		// Create the debug renderer.
		debugRenderer = new Box2DDebugRenderer();
		debugRenderer.setDrawAABBs(false);
		debugRenderer.setDrawBodies(true);
		debugRenderer.setDrawContacts(false);
		debugRenderer.setDrawInactiveBodies(false);
		debugRenderer.setDrawJoints(false);
		debugRenderer.setDrawVelocities(false);
		
		// Set the contact listener for the world, for collisions.
		world.setContactListener(new ContactListener() {	
			@Override
			public void beginContact(Contact contact) {}

			@Override
			/**
			 * On contact between two fixtures.
			 * 
			 * @param contact The contact.
			 */
			public void endContact(Contact contact) {
				GlobalState.currentGame.endContact(contact);
			}	

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {}
		});
		
		startServer();
		
		// Handle all input.
		Gdx.input.setInputProcessor(stage);
	}
	
	private void setupUI() {
		
	}
	
	public void endContact(Contact contact) {
		// Get the colliding fixtures.
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		if (fixtureA == null || fixtureB == null) {
			contact.setEnabled(false);
			return;
		}
		
		// Get the corresponding bodies.
		Body bodyA = fixtureA.getBody();
		Body bodyB = fixtureB.getBody();
		
		if (bodyA == null || bodyB == null) {
			return;
		}
		
		// For this collision to be interesting, one of the objects has to be a bullet.
		if (!bodyA.isBullet() && !bodyB.isBullet()) {
			return;
		}
		
		// Get the userdata.
		Object dataA = bodyA.getUserData();
		Object dataB = bodyB.getUserData();
		
		// If either body has a pending action, get the underlying userdata.
		while (dataA instanceof PendingAction) {
			dataA = ((PendingAction)dataA).userData;
		}
		while (dataB instanceof PendingAction) {
			dataB = ((PendingAction)dataB).userData;
		}
		
		// If either body is marked for removal, do nothing.
		if (dataA == SimpleUserData.MARKED_FOR_REMOVAL || dataB == SimpleUserData.MARKED_FOR_REMOVAL) {
			return;
		}
				
		// Get the gameobjects.
		GameObject objA = (GameObject)dataA;
		GameObject objB = (GameObject)dataB;
		if (objA == objB) {
			return;
		}
		
		// Let the bullets do damage.
		if (bodyA.isBullet()) {
			objB.damage((Bullet)objA);
			bodyA.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
		}
		if (bodyB.isBullet()) {
			objA.damage((Bullet)objB);
			bodyB.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
		}
	}	
	
	/**
	 * Removes the bodies that have been marked for removal.
	 */
	public synchronized void removeBodies() {
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		for (Body b : bodies) {
			if (world.isLocked()) {
				break;
			}
			if (b.getUserData() == SimpleUserData.MARKED_FOR_REMOVAL && !world.isLocked()) {
				world.destroyBody(b);
			}
		}
	}	
	
	public synchronized void executeActions() {
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		for (Body b : bodies) {
			if (world.isLocked()) {
				break;
			}
			while (b.getUserData() instanceof PendingAction) {
				((PendingAction)b.getUserData()).start();
			}
		}
	}
	
	/**
	 * Fills the world with groundObject bodies
	 * 
	 * @param world The world to fill
	 */
	public void fillWorld() {
		for (int i = 0; i < Gdx.graphics.getWidth(); i = i + Constants.GROUND_SIZE) {
			for (int j = 0; j < Gdx.graphics.getHeight(); j = j + Constants.GROUND_SIZE) {
				new Ground(new Vector2(i + Utils.getCameraOffset().x, j + Utils.getCameraOffset().y));
			}
		}
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}
}


