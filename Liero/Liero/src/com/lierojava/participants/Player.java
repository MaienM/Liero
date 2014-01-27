package com.lierojava.participants;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.lierojava.Constants;
import com.lierojava.GlobalState;
import com.lierojava.Utils;
import com.lierojava.gameobjects.GameObject;
import com.lierojava.gameobjects.Ground;
import com.lierojava.gameobjects.StaticBarrier;
import com.lierojava.net.RenderProxy;
import com.lierojava.userdata.PendingAction;
import com.lierojava.userdata.SimpleUserData;
import com.lierojava.weapons.Grenade;
import com.lierojava.weapons.Jetpack;
import com.lierojava.weapons.Pistol;
import com.lierojava.weapons.Shotgun;
import com.lierojava.weapons.Weapon;

public class Player extends GameObject {
	/**
	 * The Box2D body for the player. 
	 * 
	 * This stores data such as position and velocity.
	 */
	private Body body;
	
	/**
	 * The current statetime of the walking animation.
	 */
	private float animationStateTime = 0;
	
	/**
	 * The weapons of the player.
	 */
	private ArrayList<Weapon> weapons;
	
	/**
	 * The current weapon of the player.
	 */
	public Weapon currentWeapon;
	
	/**
	 * The aiming angle.
	 */
	private float aimAngle;
	
	/**
	 * The player's jetpack.
	 */
	private Jetpack jetpack = new Jetpack(this);
	
	/**
	 * The playerstate enum. 
	 */
	public enum PlayerState {
		faceLeft,
		faceRight,
		moveLeft,
		moveRight,
	}
	
	/**
	 * The current state.
	 */
	private PlayerState state = PlayerState.faceRight;
	
	/**
	 * Whether to show the weapon in the hud.
	 */
	public boolean showWeapon = false;

	/**
	 * The show weapon countdown thread.
	 */
	public Thread showWeaponThread;
	
	public Player() {
		this(new Vector2(0, 0));
		spawn();
		
		weapons = new ArrayList<Weapon>();
		weapons.add(new Pistol(this));
		weapons.add(new Shotgun(this));
		weapons.add(new Grenade(this));

		currentWeapon = weapons.get(0);
	}
	
	/**
	 * Create a new player.
	 * 
	 * @param world The world the player is in.
	 * @param postion The start position of the player.
	 */
	public Player(Vector2 postion) {		
		// Create the body.
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(postion);
		bodyDef.fixedRotation = true;
		body = GlobalState.currentGame.world.createBody(bodyDef);
		body.setUserData(this);
				
		// Create the player shape.
		PolygonShape box = new PolygonShape();
		box.setAsBox(10.0f, 10.0f);
		
		// Create the fixture.
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0.2f;
		body.createFixture(fixtureDef);
		
		// Cleanup.
		box.dispose();
	}

	/**
	 * Render the player.
	 * @return 
	 */
	@SuppressWarnings("incomplete-switch")
	public RenderProxy render() {
		RenderProxy rp = new RenderProxy();
		
		// Determine current texture.
		animationStateTime += Gdx.graphics.getDeltaTime();
		switch (state) {
			case faceLeft:
			case faceRight:
				rp.textureRegion = "player0";
				break;
				
			case moveLeft:
			case moveRight:
				rp.textureRegion = "player" + (int)(animationStateTime / Constants.PLAYER_ANIMATION_SPEED) % Constants.PLAYER_ANIMATION_SIZE;
				break;
		}
		switch (state) {
			case faceLeft:
			case moveLeft:
				rp.flipX = true;
				break;
		}

		// Draw texture.
		rp.position = new Vector2(body.getPosition());
		TextureRegion texture = Constants.TEXTURES.findRegion(rp.textureRegion);
		rp.size = new Vector2(texture.getRegionWidth() * Constants.PLAYER_TEXTURE_SCALE, texture.getRegionHeight() * Constants.PLAYER_TEXTURE_SCALE);
		rp.position.x -= texture.getRegionWidth();
		rp.position.y -= texture.getRegionHeight();
		return rp;
	}

	/**
	 * Move the player to the right.
	 */
	public void moveRight() {
		float ySpeed = body.getLinearVelocity().y;
		body.setLinearVelocity(new Vector2(Constants.PLAYER_MOVEMENT_SPEED, ySpeed));
		state = PlayerState.moveRight;
	}

	/**
	 * Move the player towards the left.
	 */
	public void moveLeft() {
		float ySpeed = body.getLinearVelocity().y;
		body.setLinearVelocity(new Vector2(-Constants.PLAYER_MOVEMENT_SPEED, ySpeed));
		state = PlayerState.moveLeft;
	}

	/**
	 * Stop the player's horizontal movement.
	 */
	public void stopSidewaysMovement() {
		float ySpeed = body.getLinearVelocity().y;
		body.setLinearVelocity(new Vector2(0f, ySpeed));
		switch (state) {
			case moveLeft:
				state = PlayerState.faceLeft;
				break;
					
			case moveRight:
				state = PlayerState.faceRight;
				break;
				
			default:
				break;
		}
	}

	/**
	 * Make the player jump.
	 */
	public void jump() {
		boolean isTouchingGround = false;
		for (Contact c : Utils.getContacts(body)) {
			//Checks if we are standing on ground
			//When stading on ground we assume it is below the player position
			// TODO: Implement something actually useable
			if (c.getFixtureA().getBody().getUserData() instanceof Ground || c.getFixtureA().getBody().getUserData() instanceof StaticBarrier) {
				if (c.getFixtureA().getBody().getPosition().y < this.body.getPosition().y  &&
						c.getFixtureA().getBody().getPosition().x < this.body.getPosition().x - 2 && 
						c.getFixtureA().getBody().getPosition().x < this.body.getPosition().x + 22) {
					isTouchingGround = true;
					break;	
				}
			} else if (c.getFixtureB().getBody().getUserData() instanceof Ground || c.getFixtureB().getBody().getUserData() instanceof StaticBarrier) {
				if (c.getFixtureB().getBody().getPosition().y < this.body.getPosition().y &&
						c.getFixtureB().getBody().getPosition().x - 2 < this.body.getPosition().x && 
						c.getFixtureB().getBody().getPosition().x < this.body.getPosition().x + 22) {
					isTouchingGround = true;
					break;	
				}
			}
			
		}
		
		if (isTouchingGround) {
			float xSpeed = body.getLinearVelocity().x;
			body.setLinearVelocity(new Vector2(xSpeed, Constants.PLAYER_JUMP_SPEED));
		}
	}
	
	/**
	 * Get the number of the current weapon of the player.
	 */
	public int getWeaponIndex() {
		return weapons.indexOf(currentWeapon);
	}
	
	/**
	 * Set the weapon of the player.
	 * 
	 * @param weapon The weapon.
	 */
	public void setWeaponIndex(int index) {
		currentWeapon = weapons.get(index);
		showWeapon = true;
		if (showWeaponThread != null) {
			showWeaponThread.interrupt();
		}
		showWeaponThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					return;
				}
				showWeapon = false;
			}
		});
		showWeaponThread.start();
	}
	
	/**
	 * Get the body of this player.
	 * 
	 * @return The body of this player.
	 */
	public Body getBody() {
		return body;
	}
		
	/**
	 * TODO: Implement.
	 */
	@Override
	protected void die() {
		spawn();
	}
	
	/**
	 * (Re)spawn the player.
	 */
	private void spawn() {
		new PendingAction(body) {			
			@Override
			public void run() {
				health = 100;
				
				Random r = new Random();
				body.setTransform(new Vector2(r.nextFloat() * Gdx.graphics.getWidth() * 0.8f - Gdx.graphics.getWidth() * 0.4f, 
											  r.nextFloat() * Gdx.graphics.getHeight() * 0.8f - Gdx.graphics.getHeight() * 0.4f), 
								  0); 
				
				
				//Remove any ground objects the player would collide with on spawn
				//Perform microstep to calculate collisions of the newly created player
				GlobalState.currentGame.world.step(1f/120f, 8, 3);
				Array<Contact> contacts = Utils.getContacts(body);
				for (Contact c : contacts) {
					if (c.getFixtureA().getBody().getUserData() instanceof Ground) {
						Body fa = c.getFixtureA().getBody();
						fa.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
					}
					else if (c.getFixtureB().getBody().getUserData() instanceof Ground) {
						Body fb = c.getFixtureB().getBody();
						fb.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
					}
				}				
			}
		};
	}

	/**
	 * Use the jetpack.
	 */
	public void jetpack() {
		jetpack.fire((float) Math.PI);
	}
	
	/**
	 * Move aim towards the left.
	 */
	public void aimLeft() {
		aimAngle -= Constants.PLAYER_AIM_SPEED;
		aimAngle %= 360;
	}
	
	/**
	 * Move aim towards the right.
	 */
	public void aimRight() {
		aimAngle += Constants.PLAYER_AIM_SPEED;
		aimAngle %= 360;
	}
	
	/**
	 * Get the aiming angle.
	 * 
	 * @return The aiming angle.
	 */
	public float getAim() {
		return aimAngle;
	}

	/**
	 * Fire weapon.
	 */
	public void fire() {
		currentWeapon.fire(aimAngle);
	}

	/**
	 * Get the health of the player.
	 * 
	 * @return The health of the player.
	 */
	public int getHealth() {
		return health; 
	}
}
