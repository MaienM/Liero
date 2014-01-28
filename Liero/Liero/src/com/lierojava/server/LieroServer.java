package com.lierojava.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
    
    private boolean isHost = false;
    
    /**
     * Server constructor, tries to connect to the database
     */
    public LieroServer() {
    	
		try {
			StartServer();
		} catch (IOException e1) {
			try {
				StartClient();
				isHost = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			e1.printStackTrace();
		}
		
		if (!isHost) {
			return;
		}
		
		try {
			connectionSource = new JdbcConnectionSource(connectionString);
			accDao = DaoManager.createDao(connectionSource, Account.class);
			TableUtils.createTableIfNotExists(connectionSource, Account.class);
		} catch (SQLException e) {
			//  TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * Starts the server
     * @param args
     */
	public static void main(String[] args) {
		LieroServer s = new LieroServer();
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
            public void connected(final Connection connection) {
           	 Utils.print("Connection");
            }

            public void disconnected(Connection connection) {
           	 Utils.print("Client lost");
            }

            public void received(Connection connection, Object object) {
           	 Utils.print("Received data: " + object);
            }
        });
		ObjectSpace.registerClasses(kryoServer.getKryo());
	}
	
	/**
	 * Connects to the server
	 * 
	 * @throws IOException
	 */
	public void StartClient() throws IOException {
		Client kryoClient = new Client();
		kryoClient.start();
		
		kryoClient.connect(10000, Constants.SERVER_HOST, Constants.SERVER_PORT);
		kryoClient.setTimeout(0);
		ObjectSpace.registerClasses(kryoClient.getKryo());
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
	public void register(String username, String password) {
		Account newAccount = new Account(username, password);
		try {
			// The name is not available, return
			if (isUsernameTaken(username)) {
				return;
			}
			// Create and save and account
			accDao.create(newAccount);
		
		} catch (SQLException e) {
			e.printStackTrace();
			// Something went wrong, so we return false to be sure nothing too odd happens
		}
	}

	/**
	 * Checks the credentials of a use using his username and password
	 * 
	 * @param username The username to check 
	 * @param password The password that should belong to the account
	 * @return True if the info is valid, false otherwise
	 */
	public boolean login(String username, String password) {
		try {
			// Fetch the correct account
			List<Account> accounts = accDao.queryForEq("name", username);
			
			// If we don't have exactly one account for the current user, something wierd is going on, 
			// so we return false
			if (accounts.size() != 1) {
				return false;
			}
			
			// Actually check the credentials
			Account loginAccount = accounts.get(0);
			return loginAccount.isCorrectPassword(password);
		} catch (SQLException e) {
			e.printStackTrace();
			// Something went wrong, so we return false to be sure nothing too odd happens
			return false;
		}
	}
	
	/**
	 * Persists the stats of a player
	 * 
	 * @param id Database id of the player
	 * 
	 * @param kills The number of kills the player had this round
	 * 
	 * @param deaths The number of deaths the player had this round
	 */
	public void savePlayerStats(int id, int kills, int deaths) {
		try {
			Account acc = accDao.queryForId(id);
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

}
