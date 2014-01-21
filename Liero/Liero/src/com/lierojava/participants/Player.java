package com.lierojava.participants;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.lierojava.gameobjects.Ground;
import com.lierojava.userdata.PendingAction;
import com.lierojava.userdata.SimpleUserData;
import com.lierojava.weapons.Grenade;
import com.lierojava.weapons.Jetpack;
import com.lierojava.weapons.Pistol;
import com.lierojava.weapons.Shotgun;
import com.lierojava.weapons.Weapon;

public class Player extends Participant {
	/**
	 * The Box2D body for the player. 
	 * 
	 * This stores data such as position and velocity.
	 */
	private Body body;
	
	/**
	 * The player textures.
	 */
	private static TextureRegion faceLeftTexture, faceRightTexture;
	
	/**
	 * The walking animations.
	 */
	private static Animation walkLeftAnimation, walkRightAnimation;
	
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
	 * The current health.
	 */
	private int health = 100;
	
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
		
		weapons = new ArrayList<>();
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
				
		// Create the player shape.
		PolygonShape box = new PolygonShape();
		// Set the polygon shape as a box which is twice the size of our view
		// port and 20 high
		// (setAsBox takes half-width and half-height as arguments)
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
	
	public static void setup() {
		// Texture loading.
		faceRightTexture = Constants.TEXTURES.findRegion("slice38");
		faceLeftTexture = new TextureRegion(faceRightTexture);
		faceLeftTexture.flip(true, false);
		
		// Create the animations.
		final TextureRegion[] walkRightFrames = new TextureRegion[5];
		for (int i = 0; i < 5; i++) {
			walkRightFrames[i] = Constants.TEXTURES.findRegion("slice" + (i + 38));
		}

		final TextureRegion[] walkLeftFrames = new TextureRegion[5];
		for (int i = 0; i < 5; i++) {
			walkLeftFrames[i] = new TextureRegion(walkRightFrames[i]);
			walkLeftFrames[i].flip(true, false);
		}
		
		walkRightAnimation = new Animation(Constants.PLAYER_ANIMATION_SPEED, new Array<TextureRegion>(walkRightFrames), Animation.LOOP);
		walkLeftAnimation = new Animation(Constants.PLAYER_ANIMATION_SPEED, new Array<TextureRegion>(walkLeftFrames), Animation.LOOP);
	}

	/**
	 * Render the player.
	 */
	public void render(SpriteBatch batch) {
		// Determine current texture.
		TextureRegion texture = null;
		animationStateTime += Gdx.graphics.getDeltaTime();
		switch (state) {
			case faceLeft:
				texture = faceLeftTexture;
				break;
				
			case faceRight:
				texture = faceRightTexture;
				break;
				
			case moveLeft:
				texture = walkLeftAnimation.getKeyFrame(animationStateTime);
				break;

			case moveRight:
				texture = walkRightAnimation.getKeyFrame(animationStateTime); 
				break;
		}

		// Draw texture.
		Vector2 position = body.getPosition();
		Vector2 size = new Vector2(texture.getRegionWidth() * Constants.TEXTURE_SCALE, texture.getRegionHeight() * Constants.TEXTURE_SCALE);
		batch.draw(texture, position.x - size.x / 2, position.y - size.y / 2, size.x, size.y);
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
			if (c.getFixtureA().getBody().getUserData() == SimpleUserData.FLOOR ||
				c.getFixtureB().getBody().getUserData() == SimpleUserData.FLOOR) {
				isTouchingGround = true;
				break;
			}
			//Checks if we are standing on ground
			//When stading on ground we assume it is below the player position
			// TODO: Implement something actually useable
			if (c.getFixtureA().getBody().getUserData() instanceof Ground) {
				if (c.getFixtureA().getBody().getPosition().y < this.body.getPosition().y  &&
						c.getFixtureA().getBody().getPosition().x < this.body.getPosition().x - 2 && 
						c.getFixtureA().getBody().getPosition().x < this.body.getPosition().x + 22) {
					isTouchingGround = true;
					break;	
				}
			} else if (c.getFixtureB().getBody().getUserData() instanceof Ground) {
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
	 * Deal damage to the player.
	 * 
	 * @param amount The amount of damage to deal.
	 */
	public void doDamage(int amount) {
		if (health <= 0) {
			return;
		}
		health -= amount;
		if (health <= 0) {
			die();
		}
	}
		
	/**
	 * TODO: Implement.
	 */
	private void die() {
		spawn();
	}
	
	private void spawn() {
		final Object oldUserData = body.getUserData();
		body.setUserData(new PendingAction() {
			@Override
			public void run(Body body) {
				health = 100;
				
				Random r = new Random();
				body.setTransform(new Vector2(r.nextFloat() * Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 2, 
											  r.nextFloat() * Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2), 
								  0); 
				
				//Remove any ground objects the player would collide with on spawn
				//Perform microstep to calculate collisions of the newly created player
				GlobalState.currentGame.world.step(1f/120f, 8, 3);
				Array<Contact> contacts = Utils.getContacts(body);
				for (Contact c : contacts) {
					if (c.getFixtureA().getBody().getUserData() instanceof Ground) {
						Body fa = c.getFixtureA().getBody();
						Ground.groundObjects.remove(fa.getUserData());
						fa.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
					}
					if (c.getFixtureB().getBody().getUserData() instanceof Ground) {
						Body fb = c.getFixtureB().getBody();
						Ground.groundObjects.remove(fb.getUserData());
						fb.setUserData(SimpleUserData.MARKED_FOR_REMOVAL);
					}
				}				
				
				body.setUserData(oldUserData);
			}
		});
	}

	/**
	 * Use the jetpack.
	 */
	public void jetpack() {
		jetpack.fire(180);
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
