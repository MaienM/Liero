package com.lierojava.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lierojava.Constants;
import com.lierojava.Utils;
import com.lierojava.net.handles.ParticipantServer;
import com.lierojava.net.handshake.ServerHandshake;
import com.lierojava.net.interfaces.IHostServer;
import com.lierojava.net.interfaces.IParticipantChat;
import com.lierojava.net.interfaces.IParticipantServer;
import com.lierojava.net.interfaces.IServerHandshake;
import com.lierojava.server.data.HostStruct;
import com.lierojava.server.data.ParticipantIdentifier;
import com.lierojava.server.database.Account;

public class LieroServer {

	/**
	 * The connection string for the database
	 */
    String connectionString = "jdbc:h2:file:data";
    
    /**
     * The database connection source
     */
    ConnectionSource connectionSource;
    
    /**
     * The account Data Access Object
     */
    Dao<Account, Integer> accDao;
    
    /**
     * Boolean to determine whether we are a server or a client
     */
    private boolean isHost = true;
    
    /**
     * Server constructor, tries to connect to the database
     */
    public LieroServer() {
    	//Try to start a server
		try {
			StartServer();
		} catch (IOException e1) {
			try {
				//That failed, try to start as client then
				StartClient();
				isHost = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			e1.printStackTrace();
		}
		
		//Initialization below this point is Serverside only
		if (!isHost) {
			return;
		}
		
		//Initialize database and make sure table exists
		try {
			connectionSource = new JdbcConnectionSource(connectionString);
			accDao = DaoManager.createDao(connectionSource, Account.class);
			TableUtils.createTableIfNotExists(connectionSource, Account.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Starts the server
     * @param args
     */
	public static void main(String[] args) {
		new LieroServer();
	}
	
	/**
	 * Starts the server
	 * 
	 * @throws IOException
	 */
	public void StartServer() throws IOException {
		Server kryoServer = new Server();
		kryoServer.start();
		kryoServer.bind(Constants.SERVER_PORT);
		kryoServer.addListener(new Listener() {
            @Override
			public void connected(final Connection connection) {
           	 GlobalServerState.serverObjectSpace.addConnection(connection);
           	 Utils.print(connection.getID());
           	 if (connection.getRemoteAddressTCP().getAddress() == null) {
           		 connection.close();
           	 }
            }

            @Override
			public void disconnected(Connection connection) {
            	//Cleans up a connection and removes a game if the connection had any
            	if (GlobalServerState.connectionAccounts.containsKey(connection)) {
            		Account acc = GlobalServerState.connectionAccounts.get(connection);
            		if (GlobalServerState.accountGame.containsKey(acc.getId())) {
            			GlobalServerState.accountGame.remove(acc.getId());
            		}
            	}
            	GlobalServerState.connectionAccounts.remove(connection);
            }

            @Override
			public void received(Connection connection, Object object) {
            	//ParticipantIdentifier is a class solely made to attach a connection to a player
            	if (object instanceof ParticipantIdentifier) {
            		GlobalServerState.server.addAccountToList(connection, ((ParticipantIdentifier) object).dbId);
            	}
            }
        });
		GlobalServerState.server = this;
		Utils.setupKryo(kryoServer.getKryo());
		
		IServerHandshake ish = new ServerHandshake();
		GlobalServerState.serverObjectSpace.register(0, ish);
	}
	
	/**
	 * Connects to the server
	 * 
	 * @throws IOException
	 */
	public void StartClient() throws IOException {
		Client kryoClient = new Client();
		kryoClient.start();
		kryoClient.connect(5000, Constants.SERVER_HOST, Constants.SERVER_PORT);
		kryoClient.setTimeout(0);
		Utils.setupKryo(kryoClient.getKryo());
		
		//Get server handshake
		IServerHandshake ish = ObjectSpace.getRemoteObject(kryoClient, 0, IServerHandshake.class);
		
		//Try to login, dbId stays -1 if login fails
		int dbId = -1;
		dbId = ish.login("anotherTest", "test");
		
		//We managed to login, set some values
		if (dbId != -1) {
			//Get our interface to the server
			GlobalServerState.ips = ObjectSpace.getRemoteObject(kryoClient, dbId, IParticipantServer.class);
			//Workaround to be able to link a connection to an account
			ParticipantIdentifier ident = new ParticipantIdentifier();
			ident.dbId = dbId;
			kryoClient.sendTCP(ident);
			
			// Get the global chat handle
			int index = GlobalServerState.ips.getChatInstance();
			GlobalServerState.ipc = ObjectSpace.getRemoteObject(kryoClient, index, IParticipantChat.class);
			
			//-------Run test functionality---------
			//TODO: Remove
			//-------Run test functionality---------
			this.testClientFunctionality(kryoClient, dbId);
		}
		//We failed to login
		else  {
			Utils.print("Login failed");
			kryoClient.close();
		}
		
	}
	
	/**
	 * Checks if a username is available
	 * 
	 * @param username The username to check
	 * @return false if it is available, true if it is taken
	 */
	public boolean isUsernameTaken(String username) {
		try {
			// Fetch the correct account
			List<Account> accounts = accDao.queryForEq("name", username);
			
			// if the list is empty, the username is free so return true, if not, return false
			return !accounts.isEmpty();
		} catch (SQLException e) {
			e.printStackTrace();
			// Something went wrong, so we return false to be sure nothing too odd happens
			return false;
		}
	}
	
	/**
	 * Handles the registration of an account
	 * 
	 * @param username The name of the new account
	 * @param password The password of the new account
	 */
	public boolean register(String username, String password) {
		Account newAccount = new Account(username, password);
		try {
			// The name is not available, return
			if (isUsernameTaken(username)) {
				return false;
			}
			// Create and save and account
			accDao.create(newAccount);
			return true;
		
		} catch (SQLException e) {
			e.printStackTrace();
			// Something went wrong, so we return false to be sure nothing too odd happens
			return false;
		}
	}

	/**
	 * Checks the credentials of a use using his username and password
	 * 
	 * @param username The username to check 
	 * @param password The password that should belong to the account
	 * @param connectionId 
	 * @return The database id of the player if login was successful, -1 otherwise
	 */
	public int login(String username, String password) {
		try {
			// Fetch the correct account
			List<Account> accounts = accDao.queryForEq("name", username);
			// If we don't have exactly one account for the current user, something wierd is going on, 
			// so we return false
			if (accounts.size() != 1) {
				return -1;
			}
			
			// Check if this account is already logged in
			for (Entry<Connection, Account> entry :  GlobalServerState.connectionAccounts.entrySet()) {
				Utils.print(entry.getValue().getName());
				if (entry.getValue().getName().equals(username)) {
					return -1;
				}
			}
			
			// Actually check the credentials
			Account loginAccount = accounts.get(0);
			Utils.print(loginAccount.getKills()  + "  :  " + loginAccount.getDeaths());
			if (loginAccount.isCorrectPassword(password)) {
				IParticipantServer ips = new ParticipantServer(loginAccount.getId(), loginAccount.getName());
				GlobalServerState.serverObjectSpace.register(loginAccount.getId(), ips);
				return loginAccount.getId();
			} 
			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			// Something went wrong, so we return false to be sure nothing too odd happens
			return -1;
		}
	}
	
	/**
	 * Persists the stats of a player
	 * 
	 * @param id Database id of the player
	 * @param kills The number of kills the player had this round
	 * @param deaths The number of deaths the player had this round
	 */
	public void savePlayerStats(int id, int kills, int deaths) {
		try {
			Account acc = null;
			
			for (Entry<Connection, Account> entry :  GlobalServerState.connectionAccounts.entrySet()) {
				if (entry.getValue().getId() == id) {
					acc = entry.getValue();
				}
			}
			if (acc == null) {
				return;
			}
			acc.increaseDeaths(deaths);
			acc.increaseKills(kills);
			accDao.update(acc);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Maps a databaseAccount to a connection
	 * @param dbId
	 */
	private void addAccountToList(Connection conn, int dbId) {
		try {
			Account acc = accDao.queryForId(dbId);
			GlobalServerState.connectionAccounts.put(conn, acc);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks whether the player with dbId belongs to the passed host
	 * @param dbId The dabase id of the account
	 * @param host The host the account is connecting from
	 * @return True if it is a valid player with correct host, false otherwise
	 */
	public boolean isLoggedInPlayer(int dbId, String host) {
		for (Entry<Connection, Account> entry :  GlobalServerState.connectionAccounts.entrySet()) {
			if (entry.getValue().getId() == dbId && entry.getKey().getRemoteAddressTCP().getAddress().getHostAddress().equals(host)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Tests all required client and host functionality
	 * @param kryoClient The client to test with
	 * @param dbId the dbId to test with
	 * 
	 * TODO: remove
	 */
	private void testClientFunctionality(Client kryoClient, int dbId) {
		//Create a game host, send it to the server and receive an IHostServer interface
		HostStruct hs = new HostStruct("127.0.0.1", 2900, "ssddaa");
		int ihsId = GlobalServerState.ips.addGame(hs);
		
		if (ihsId == -1) {
			Utils.print("Failed to create game");
			return;
		}
		IHostServer ihs = ObjectSpace.getRemoteObject(kryoClient, ihsId, IHostServer.class);
		
		//Check if someplayer with dbId is logged in and belongs to some host
		Utils.print(ihs.isLoggedinPlayer(dbId, "127.0.0.1"));
		
		//Fetch games, print them and then increase the kills and deaths of this player by 10
		ArrayList<HostStruct> games = GlobalServerState.ips.getGames();
		for (HostStruct game : games) {
			Utils.print(game.host + ":" + game.port + " > " + game.name);
		}
		ihs.savePlayerStats(dbId, 10, 10);
		
		// Get the Chat object
		GlobalServerState.ipc.sendMessage(GlobalServerState.ips.getName() + ":" + "Message1");
		GlobalServerState.ipc.sendMessage(GlobalServerState.ips.getName() + ":" + "Message2");
		GlobalServerState.ipc.sendMessage(GlobalServerState.ips.getName() + ":" + "Message3");
		GlobalServerState.ipc.sendMessage(GlobalServerState.ips.getName() + ":" + "Message4");
		GlobalServerState.ipc.sendMessage(GlobalServerState.ips.getName() + ":" + "Message5");
		ArrayList<String> messages = GlobalServerState.ipc.getNewMessages();
		Utils.print("Gotmessages");
		for (String message : messages) {
			Utils.print(message);
		}
		GlobalServerState.ipc.sendMessage(GlobalServerState.ips.getName() + ":" + "Message6");
		GlobalServerState.ipc.sendMessage(GlobalServerState.ips.getName() + ":" + "Message7");
		messages = GlobalServerState.ipc.getNewMessages();
		for (String message : messages) {
			Utils.print(message);
		}

	}

}
