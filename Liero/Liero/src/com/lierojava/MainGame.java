package com.lierojava;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.lierojava.gameobjects.StaticBarrier;
import com.lierojava.gui.HUD;
import com.lierojava.net.interfaces.IHostParticipant;
import com.lierojava.net.interfaces.IParticipantHost;
import com.lierojava.participants.Player;
import com.lierojava.userdata.SimpleUserData;

public class MainGame implements Screen {
	/**
	 * The Box2D world.
	 */
	public World world;
	
	/**
	 * The stats.
	 */
	public Stats stats;
	
	/**
	 * Debug crap.
	 */
	private Box2DDebugRenderer debugRenderer;
	
	/**
	 * The viewport camera.
	 */
	private OrthographicCamera camera;
	
	/**
	 * The HUD.
	 */
	private HUD hud = new HUD(this);
	
	/**
	 * The player belonging to this instance.
	 */
	public Player currentPlayer;
	
	/**
	 * The players.
	 */
	public ArrayList<Player> players = new ArrayList<>();
	
	/**
	 * Whether this instance is hosting the game.
	 */
	public boolean isHost;

	/**
	 * Renders sprites.
	 */
	private SpriteBatch batch;

	/**
	 * The background texture.
	 */
	private Texture backgroundTexture = new Texture(Gdx.files.internal("textures/background.png"));
	
	/**
	 * The IParticipantHost object, for communication to the host.
	 */
	public IParticipantHost iph;
	
	/**
	 * The IHostParticipant objects, for communications from the host.
	 */
	public ArrayList<IHostParticipant> ihps = new ArrayList<>();
	
	/**
	 * Create a new game.
	 */
	public MainGame() {				
		// Mark self as host.
		// TODO: Only set this when actually host.
		isHost = true;
		
		// Create a new sprite batch.
		batch = new SpriteBatch();
	}

	/**
	 * Gets executed once every frame.
	 * 
	 * Handles game updates, input and rendering.
	 */	
	@Override
	public void render(float delta) {
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
		batch.draw(backgroundTexture, -Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Render the players.
		for (Player p : players) {
			p.render(batch);
		}

		// Render the HUD.
		hud.render(batch);
		
		// Finish rendering.
		batch.end();	

		// Draw the debug view.
		debugRenderer.render(world, camera.combined);
		
		// Perform a game step.
		if (isHost) {
			update();
		} 
		
		// Handle input.
		handleInput();
	}

	/**
	 * Peform a game step.
	 */
	private synchronized void update() {
		// Perform a physics step.
		world.step(1f/30f, 8, 3);
		
		// Remove unwanted objects.
		removeBodies();
		
		// Push the update to all clients.
		/*for (IHostParticipant ihp : ihps) {
			ihp.updateWorld(world);
		}*/
	}
	
	/**
	 * Process user input.
	 */
	private void handleInput() {
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
		if (Gdx.input.isKeyPressed(Keys.PAGE_UP)) {
			iph.setWeaponIndex((iph.getWeaponIndex() + 1) % Constants.MAX_WEAPONS);
		}
		if (Gdx.input.isKeyPressed(Keys.PAGE_DOWN)) {
			iph.setWeaponIndex((iph.getWeaponIndex() - 1) % Constants.MAX_WEAPONS);
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
	}

	@Override
	public void show() {
		// Create the world.
		world = new World(new Vector2(0, -10), true);
		
		// Create the debug renderer.
		debugRenderer = new Box2DDebugRenderer();
		
		// Create the camera.
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// If not host, stop now.
		if (!isHost) {
			return;
		}
		
		// Create test bodies.
		createTestBody();

		// Set the contact listener for the world, for collisions.
		world.setContactListener(new MainGameContactListener());
	}
	
	/**
	 * TODO: Remove
	 */
	private void createTestBody()
	{
		/*
		Random r = new Random();
		int i = 0;
		for (int x = -Gdx.graphics.getWidth() / 2 + 20; x < Gdx.graphics.getWidth() / 2 - 20; x += 20) {
			for (int y = -Gdx.graphics.getHeight() / 2 - 100; y < Gdx.graphics.getHeight() / 2 - 20; y += 20) {
				DynamicCircle dynCircle = new DynamicCircle();
				dynCircle.setPosition(new Vector2(x, y));
				dynCircle.addToWorld(level.getWorld());
				i++;
			}
		}
		Utils.print(i);*/
		
		new StaticBarrier();
		
		//currentPlayer = new Player(new Vector2(-30,(camera.viewportHeight / -2) + 10f));		
		//players.add(currentPlayer);
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
		
		// If the collision is with a player, deal damage.
		for (Player p : players) {
			if (bodyA.equals(p.getBody())) {
				p.doDamage(50);
			}
			if (bodyB.equals(p.getBody())) {
				p.doDamage(50);
			}
		}
		
		// Remove the bullets.
		if (bodyA.isBullet()) {
			bodyA.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
		}
		if (bodyB.isBullet()) {
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
			if (b.getUserData() == SimpleUserData.MARKED_FOR_REMOVAL) {
				world.destroyBody(b);
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

class MainGameContactListener implements ContactListener {	
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
}