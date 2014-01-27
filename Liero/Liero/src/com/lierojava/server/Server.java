package com.lierojava.server;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lierojava.Utils;
import com.lierojava.server.database.Account;

public class Server {

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
     * Server constructor, tries to connect to the database
     */
    public Server() {
		try {
			connectionSource = new JdbcConnectionSource(connectionString);
			accDao = DaoManager.createDao(connectionSource, Account.class);
			TableUtils.createTableIfNotExists(connectionSource, Account.class);
		} catch (SQLException e) {
			//  TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public static void main(String[] args) {
		Server s = new Server();
		Utils.print(s.login("test", "test"));
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

}
