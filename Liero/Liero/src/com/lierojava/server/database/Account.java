package com.lierojava.server.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lierojava.BCrypt;

/**
 * The OrmLite class holding the data of an account
 * 
 * @author Subhi
 */
@DatabaseTable(tableName = "accounts")
public class Account {
	/**
	 * OrmLite no-arg constructor
	 */
	public Account() {
		BCrypt.BCRYPT_SALT_LEN = 16;
	}
	
	/**
	 * Creates a new account object, encrypts the password
	 * 
	 * @param username The new user's name
	 * 
	 * @param password The new user's password
	 */
	public Account(String username, String password) {
		this.name = username;
		// Has to be done using the set method to make sure we generate a new salt
		this.SetPassword(password);
		this.kills = 0;
		this.deaths = 0;
	}
	
	/**
	 * The unique database id
	 */
	@DatabaseField(id = true)
	private int id;
	
	/**
	 * The username of this account
	 */
	@DatabaseField(canBeNull = false, unique = true)
	private String name;
	
	/**
	 * The password after hashing and encryption for this account
	 */
	@DatabaseField(canBeNull = false)
	private String password;
	
	/**
	 * The salt used to encrypt the password, prefixed.
	 */
	@DatabaseField(canBeNull = false)
	private String salt;
	
	/**
	 * The number of kills this account has
	 */
	@DatabaseField(canBeNull = false)
	private int kills;
	
	/**
	 * The number of deaths this account has
	 */
	@DatabaseField(canBeNull = false, defaultValue = "0")
	private int deaths;
	
	/**
	 * Returns the name of the player
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the username for this account
	 * 
	 * @param name the new name
	 */
	public void setName(String name) {
		if (name == null || name.equals("")) {
			return;
		}
		this.name = name;
	}
	
	/**
	 * Sets the password
	 * @param clear the cleartext password
	 * 
	 * TODO: Encrypt
	 */
	public void SetPassword(String clear) {
		String salt = BCrypt.gensalt();
		String encPasswd = BCrypt.hashpw(clear, salt);
		this.salt = salt;
		this.password = encPasswd;
	}
	
	/**
	 * Returns the salt this player has
	 * 
	 * @return the salt
	 */
	public String getSalt() {
		return this.salt;
	}

	/**
	 * Returns the number of kills this player has
	 * 
	 * @return the number of kills
	 */
	public int getKills() {
		return this.kills;
	}
	
	/**
	 * Returns the number of deaths this player has
	 * 
	 * @return the number of deaths
	 */
	public int getDeaths() {
		return this.deaths;
	}
	
	/**
	 * Increases the number of kills this player has
	 * 
	 * @param kills the number of kills
	 */
	public void increaseKills(int kills) {
		this.kills += kills;
	}
	
	/**
	 * Increases the number of deaths this player has
	 * 
	 * @param deaths the number of deaths
	 */
	public void increaseDeaths(int deaths) {
		this.deaths += deaths;
	}
	
	/**
	 * Checks if the credentials match the current Account
	 * 
	 * @param clear the cleartext password
	 * @return True if we are logged in, false otherwise
	 */
	public boolean isCorrectPassword(String clear) {
		String hashed = BCrypt.hashpw(clear, this.salt);
		if (hashed.equals(this.password)) {
			return true;
		}
		return false;
	}
	
}
