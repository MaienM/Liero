package com.lierojava.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.lierojava.net.interfaces.IParticipantChat;
import com.lierojava.net.interfaces.IParticipantServer;
import com.lierojava.server.data.HostStruct;
import com.lierojava.server.database.Account;

public class GlobalServerState {
	/**
	 * The list of games currently available
	 * The integer is the dbId of the player
	 */
	public static HashMap<Integer, HostStruct> accountGame = new HashMap<Integer, HostStruct>();
	
	/**
	 * The LieroServer instance
	 */
	public static LieroServer server;
	
	/**
	 * The global objectSpace for the server
	 */
	public static ObjectSpace serverObjectSpace = new ObjectSpace();
	
	/**
	 * The handle to the server
	 */
	public static IParticipantServer ips;
	
	/**
	 * The chat handle to the global server
	 */
	public static IParticipantChat ipc;
	
	/**
	 * Links a connection to an account, used to keep track of logged in accounts
	 */
	public static HashMap<Connection, Account> connectionAccounts = new HashMap<Connection, Account>();
	
	/**
	 * A complete list of all sent chat messages
	 */
	public static ArrayList<String> chatMessages = new ArrayList<String>();
	
	

}
