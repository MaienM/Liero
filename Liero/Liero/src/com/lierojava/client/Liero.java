package com.lierojava.client;



public class Liero extends Game implements ApplicationListener {
	
	
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
				
		if (host == null) {
			try {
				startServer();
			} catch (IOException e) {
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
		
		Server server = new Server(12800 * 1024, 1600 * 1024);
		Utils.setupKryo(server.getKryo());
		server.start();
		server.bind(Constants.PORT, Constants.PORT);
		
		server.addListener(new Listener() {
             @Override
			public void connected(final Connection connection) {
            	 Utils.print("Connection");
            	 GlobalState.objectSpace.addConnection(connection);
             }

             @Override
			public void disconnected(Connection connection) {
            	 Utils.print("Client lost");
             }

             @Override
			public void received(Connection connection, Object object) {
            	 //Utils.print("Received data: " + object);
            	 GlobalState.lastSender = connection;
             }
         });
		
		IHostHandshake ihh = new HostHandshake();
		GlobalState.objectSpace.register(0, ihh);
		
		ihh.requestParticipant(true, 0);
		game.iph = new ParticipantHost(game.players.get(0));
	}
	
	private void startClient() {
		game.isHost = false;
		
		Client client = new Client(6400 * 1024, 1600 * 1024);
		Utils.setupKryo(client.getKryo());
		client.start();
		try {
			client.connect(5000, host, Constants.PORT, Constants.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.setTimeout(0);

		IHostHandshake ihh = ObjectSpace.getRemoteObject(client, 0, IHostHandshake.class);
		int index = ihh.requestParticipant(true, 1);
		if (index > 0) {
			game.iph = ObjectSpace.getRemoteObject(client, index, IParticipantHost.class);
		}
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
