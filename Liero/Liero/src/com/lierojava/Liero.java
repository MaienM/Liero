package com.lierojava;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterable;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.LongMap.Values;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.lierojava.bullets.Bullet;
import com.lierojava.bullets.PistolBullet;
import com.lierojava.enums.GameState;
import com.lierojava.gameobjects.DynamicCircle;
import com.lierojava.gameobjects.StaticBarrier;
import com.lierojava.gui.HUD;
import com.lierojava.net.handles.HostParticipant;
import com.lierojava.net.handles.HostServer;
import com.lierojava.net.handles.ParticipantHost;
import com.lierojava.net.handles.ParticipantServer;
import com.lierojava.net.handles.ServerHost;
import com.lierojava.net.handles.ServerParticipant;
import com.lierojava.net.handshake.HostHandshake;
import com.lierojava.net.handshake.ServerHandshake;
import com.lierojava.net.interfaces.IHostHandshake;
import com.lierojava.net.interfaces.IHostParticipant;
import com.lierojava.net.interfaces.IHostServer;
import com.lierojava.net.interfaces.IParticipantHost;
import com.lierojava.net.interfaces.IParticipantServer;
import com.lierojava.net.interfaces.IServerHandshake;
import com.lierojava.net.interfaces.IServerHost;
import com.lierojava.net.interfaces.IServerParticipant;
import com.lierojava.participants.Participant;
import com.lierojava.participants.Player;
import com.lierojava.participants.Spectator;
import com.lierojava.userdata.SimpleUserData;
import com.lierojava.weapons.Jetpack;
import com.lierojava.weapons.Pistol;
import com.lierojava.weapons.Weapon;

public class Liero extends Game implements ApplicationListener {
	private final static Class<?>[] classes = new Class<?>[] {
		IParticipantHost.class,
		IParticipantServer.class,
		IHostParticipant.class,
		IHostServer.class,
		IServerHost.class,
		IServerParticipant.class,
		IHostHandshake.class,
		IServerHandshake.class,
		
		ParticipantHost.class,
		ParticipantServer.class,
		HostParticipant.class,
		HostServer.class,
		ServerHost.class,
		ServerParticipant.class,
		HostHandshake.class,
		ServerHandshake.class,

		Participant.class,
		Player.class,
		Player.PlayerState.class,
		Spectator.class,
		Chatroom.class,
		Stats.class,
		GameState.class,
		DynamicCircle.class,
		StaticBarrier.class,
		SimpleUserData.class,
		Weapon.class,
		Pistol.class,
		Jetpack.class,
		Bullet.class,
		PistolBullet.class,
		MainGame.class,
		MainGameContactListener.class,
		HUD.class,
		
		ArrayList.class,
		Class.class,
		StringBuffer.class,
  		Locale.class,
  		String.class,
  		byte[].class,
  		byte.class,
  		char[].class,
  		char.class,
  		short[].class,
  		short.class,
  		long[].class,
  		long.class,
  		int[].class,
  		int.class,
  		float.class,
  		float[].class,
  		boolean[].class,
  		boolean.class,
		
		World.class,
		WorldManifold.class,
		Body.class,
		Array.class,
		ArrayIterable.class,
		ArrayIterator.class,
		Fixture.class,
		Filter.class,
		Vector2.class,
		Vector2[].class,
		MassData.class,
		Transform.class,
		LongMap.class,
		Values.class,
		PolygonShape.class,
		Contact.class,
		ContactListener.class,
		Screen.class,
		
		java.lang.RuntimeException.class,
		java.lang.StackTraceElement[].class,
		java.lang.StackTraceElement.class,
		
		com.badlogic.gdx.Audio.class,
		com.badlogic.gdx.audio.Sound.class,
	};
	
	/**
	 * The maingame.
	 */
	private MainGame game;
	
	/**
	 * The host, if any.
	 */
	private String host;
	
	public Liero() {
	}
	public Liero(String host) {
		this.host = host;
	}
	
	@Override
	public void create() {
		game = GlobalState.currentGame = new MainGame();
		GlobalState.gameState = GameState.GAME_PLAYING;
		setScreen(GlobalState.currentGame);
		
		GlobalState.objectSpace = new ObjectSpace();
		
		Player.setup();
				
		if (host == null) {
			try {
				startServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				host = "127.0.0.1";
			}
		} 
		
		
		if (host != null) {
			startClient();
		}
	}
	
	private void startServer() throws IOException {
		game.isHost = true;
		
		Server server = new Server(1280 * 1024, 160 * 1024);
		setupKryo(server.getKryo());
		server.start();
		server.bind(Constants.PORT, Constants.PORT);
		
		server.addListener(new Listener() {
             public void connected(final Connection connection) {
            	 Utils.print("Connection");
            	 GlobalState.objectSpace.addConnection(connection);
             }

             public void disconnected(Connection connection) {
            	 Utils.print("Client lost");
             }

             public void received(Connection connection, Object object) {
            	 Utils.print("Received data: " + object);
            	 GlobalState.lastSender = connection;
             }
         });
		
		IHostHandshake ihh = new HostHandshake();
		GlobalState.objectSpace.register(0, ihh);
		
		ihh.requestParticipant(true);
		game.iph = new ParticipantHost(game.players.get(0));
	}
	
	private void startClient() {
		game.isHost = false;
		
		Client client = new Client(640 * 1024, 160 * 1024);
		setupKryo(client.getKryo());
		client.start();
		try {
			client.connect(5000, host, Constants.PORT, Constants.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.setTimeout(0);

		IHostHandshake ihh = ObjectSpace.getRemoteObject(client, 0, IHostHandshake.class);
		int index = ihh.requestParticipant(true);
		if (index > 0) {
			game.iph = ObjectSpace.getRemoteObject(client, index, IParticipantHost.class);
			//RemoteObject ro = (RemoteObject)game.iph;
			//ro.setNonBlocking(true);
			
			IHostParticipant ihp = new HostParticipant();
			GlobalState.objectSpace.register(++GlobalState.objectSpaceIndex, ihp);
			game.iph.register(GlobalState.objectSpaceIndex);
			
			//ro.setNonBlocking(false);
		}
	}
	
	private void setupKryo(Kryo kryo) {
		kryo.setReferences(true);
		kryo.setRegistrationRequired(false);
		ObjectSpace.registerClasses(kryo);
		for (Class<?> cls : classes) {
			kryo.register(cls);
		}
		//kryo.setReferenceResolver(new com.esotericsoftware.kryo.util.MapReferenceResolver());
		//kryo.setDefaultSerializer(com.esotericsoftware.kryo.serializers..class);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
