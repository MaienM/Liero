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
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lierojava.gameobjects.GameObject;
import com.lierojava.gameobjects.Ground;
import com.lierojava.gameobjects.StaticBarrier;
import com.lierojava.gui.HUD;
import com.lierojava.net.RenderProxy;
import com.lierojava.net.interfaces.IHostParticipant;
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
	 * The IHostParticipant objects, for communications from the host.
	 */
	public ArrayList<IHostParticipant> ihps = new ArrayList<IHostParticipant>();
	
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
		
		// Render game objects.
		for (RenderProxy rp : renderProxies) {
			rp.render(batch);
		}
		
		// Finish rendering.
		batch.end();	

		// Draw the debug view.
		debugRenderer.render(world, camera.combined);
		
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
		
		// Handle input.
		handleInput();
	}

	/**
	 * Peform a game step.
	 */
	private synchronized void update() {
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
		
		// Push the update to all clients.
		/*for (IHostParticipant ihp : ihps) {
			ihp.updateWorld(world);
		}*/
	}
	
	/**
	 * Process user input.
	 */
	private void handleInput() {
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
			objB.damage(Math.max(0, objA.health));
			bodyA.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
		}
		if (bodyB.isBullet()) {
			objA.damage(Math.max(0, objB.health));
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
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyType.StaticBody;
				bodyDef.fixedRotation = true;
				bodyDef.position.x = i + Utils.getCameraOffset().x;
				
				bodyDef.position.y = j + Utils.getCameraOffset().y;
				
				Body body = GlobalState.currentGame.world.createBody(bodyDef);
				Ground currGround = new Ground(body);
				body.setUserData(currGround);
						
				// Create the shape.
				PolygonShape box = new PolygonShape();
				box.setAsBox(Constants.GROUND_SIZE, Constants.GROUND_SIZE);
				
				// Create the fixture.
				body.createFixture(box, 0f);
				
				// Cleanup.
				box.dispose();
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
