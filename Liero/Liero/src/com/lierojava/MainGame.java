package com.lierojava;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.lierojava.bullets.Bullet;
import com.lierojava.gameobjects.GameObject;
import com.lierojava.gameobjects.Ground;
import com.lierojava.gameobjects.StaticBarrier;
import com.lierojava.gui.HUD;
import com.lierojava.net.RenderProxy;
import com.lierojava.net.interfaces.IParticipantHost;
import com.lierojava.participants.Player;
import com.lierojava.userdata.PendingAction;
import com.lierojava.userdata.SimpleUserData;

public class MainGame implements Screen {
	/**
	 * The Box2D world.
	 */
	public World world;
	
	/**
	 * The stats.
	 */
	public HashMap<Integer, PlayerData> stats = new HashMap<Integer, PlayerData>();
	
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
	private HUD hud = new HUD();
	
	/**
	 * The players.
	 */
	public ArrayList<Player> players = new ArrayList<Player>();
	
	/**
	 * Whether this instance is hosting the game.
	 */
	public boolean isHost;

	/**
	 * Renders sprites.
	 */
	private SpriteBatch batch;
	
	/**
	 * The render proxies.
	 */
	public ArrayList<RenderProxy> renderProxies = new ArrayList<RenderProxy>();

	/**
	 * The background texture.
	 */
	private Texture backgroundTexture = new Texture(Gdx.files.internal("background.png"));
	
	/**
	 * The IParticipantHost object, for communication to the host.
	 */
	public IParticipantHost iph;

	/**
	 * Whether we want the score window to be visible.
	 */
	private boolean showScore;
	
	/**
	 * How much time is remaining before the round ends.
	 */
	public float timeRemaining = 10;
	
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
		if (timeRemaining > 0) {
			// Handle input.
			handleInput();
					
			// Perform a game step.
			if (isHost) {
				update();
			} 
			else {
				ArrayList<RenderProxy> newRenderProxies = iph.getRenderProxies();
				if (newRenderProxies != null) {
					renderProxies = newRenderProxies;
				}
			}
			
			// Render. 
			render();
		}
		else if (timeRemaining > -10) {
			showScore = true;
			render();
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
		if (Gdx.input.isKeyPressed(Keys.PAGE_UP)) {
			iph.setWeaponIndex((iph.getWeaponIndex() + 1) % Constants.MAX_WEAPONS);
		}
		if (Gdx.input.isKeyPressed(Keys.PAGE_DOWN)) {
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
	}

	/**
	 * Peform a game step.
	 */
	private void update() {
		// Update the time remaining.
		timeRemaining -= Gdx.graphics.getDeltaTime();
		
		// Perform a physics step.
		world.step(1f/30f, 8, 3);
		
		// Perform pending actions.
		executeActions();
		
		// Remove unwanted objects.
		removeBodies();
		
		// Update the list of render proxies.
		synchronized (renderProxies) {
			Array<Body> bodies = new Array<Body>();
			world.getBodies(bodies);
			renderProxies.clear();
			for (Body b : bodies) {
				if (b.getUserData() instanceof GameObject) {
					RenderProxy rp = ((GameObject)b.getUserData()).render();
					if (rp != null) {
						renderProxies.add(rp);
					} else if (!(b.getUserData() instanceof StaticBarrier)) {
						Utils.print("Null");
					}
				}
				else if (b.getUserData() instanceof SimpleUserData) {
					Utils.print("Marked for removal");
				}
				else if (b.getUserData() instanceof PendingAction) {
					Utils.print("Pending action");
				}
				else {
					Utils.print("HELP!");
				}
			}
			renderProxies.addAll(hud.render());
		}
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
		batch.draw(backgroundTexture, -Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
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
		//debugRenderer.render(world, camera.combined);
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
		BitmapFont font = new BitmapFont();
		
		// Get width and height.
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		
		// Draw background and title.
		batch.draw(bg, width * -0.4f, height * -0.4f, width * 0.8f, height * 0.8f);
		font.draw(batch, "Player", width * -0.4f + 20, height * 0.4f - 20);
		font.draw(batch, "Kills", width * 0.2f, height * 0.4f - 20);
		font.draw(batch, "Deaths", width * 0.3f, height * 0.4f - 20);
		
		// Draw the scores.
		int i = 0;
		for (Entry<Integer, PlayerData> entry : stats.entrySet()) {
			batch.draw(i % 2 == 0 ? even : odd, width * -0.4f + 16, height * 0.4f - 56 - 20 * i, width * 0.8f - 32, 20);
			font.draw(batch, entry.getValue().name, width * -0.4f + 20, height * 0.4f - 40 - 20 * i);
			font.draw(batch, entry.getValue().kills + "", width * 0.2f, height * 0.4f - 40 - 20 * i);
			font.draw(batch, entry.getValue().deaths + "", width * 0.3f, height * 0.4f - 40 - 20 * i);
			
			i++;
		}
	}					

	@Override
	public void show() {
		// Create the world.
		world = new World(new Vector2(0, -10), true);
		
		// Create the debug renderer.
		debugRenderer = new Box2DDebugRenderer();
		debugRenderer.setDrawAABBs(false);
		debugRenderer.setDrawBodies(true);
		debugRenderer.setDrawContacts(false);
		debugRenderer.setDrawInactiveBodies(false);
		debugRenderer.setDrawJoints(false);
		debugRenderer.setDrawVelocities(false);
		
		// Create the camera.
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// If not host, stop now.
		if (!isHost) {
			return;
		}
		
		// Create world.
		new StaticBarrier();
		fillWorld(world);
		// Set the contact listener for the world, for collisions.
		world.setContactListener(new MainGameContactListener());
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
			if (b.getUserData() == SimpleUserData.MARKED_FOR_REMOVAL) {
				world.destroyBody(b);
				Utils.print("Destroying body");
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
	public void fillWorld(World world){
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
