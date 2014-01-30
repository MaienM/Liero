package com.lierojava.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map.Entry;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lierojava.Constants;
import com.lierojava.Utils;
import com.lierojava.net.handles.ParticipantServer;
import com.lierojava.net.handshake.ServerHandshake;
import com.lierojava.net.interfaces.IParticipantServer;
import com.lierojava.net.interfaces.IServerHandshake;
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
     * Starts the server
     * @param args
     * @throws IOException 
     */
	public static void main(String[] args) throws IOException {
		new LieroServer();
	}
	
    /**
     * Server constructor, tries to connect to the database
     * @throws IOException 
     */
    public LieroServer() throws IOException {
    	startServer();

		// Initialize database and make sure table exists
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
	 * 
	 * @throws IOException
	 */
	public void startServer() throws IOException {
		Server kryoServer = new Server();
		kryoServer.start();
		kryoServer.bind(Constants.SERVER_PORT);
		kryoServer.addListener(new Listener() {
			@Override
			public void connected(final Connection connection) {
				GlobalServerState.serverObjectSpace.addConnection(connection);
				if (connection.getRemoteAddressTCP().getAddress() == null) {
					connection.close();
				}
			}

			@Override
			public void disconnected(Connection connection) {
				// Cleans up a connection and removes a game if the connection had any
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
				// ParticipantIdentifier is a class solely made to attach a connection to a player
				if (object instanceof ParticipantIdentifier) {
					GlobalServerState.server.addAccountToList(connection,
							((ParticipantIdentifier) object).dbId);
				}
			}
		});
		GlobalServerState.server = this;
		Utils.setupKryo(kryoServer.getKryo());

		IServerHandshake ish = new ServerHandshake();
		GlobalServerState.serverObjectSpace.register(0, ish);
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
				if (entry.getValue().getName().equals(username)) {
					return -1;
				}
			}
			
			// Actually check the credentials
			Account loginAccount = accounts.get(0);
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
	 * End the current game.
	 */
	public void endGame() {
		
	}
}
