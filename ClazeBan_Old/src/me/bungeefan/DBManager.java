package me.bungeefan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;



public class DBManager {

	private static DBManager instance = null;
	public HashMap<UUID, Boolean> banned = new HashMap<UUID, Boolean>();
	private String ip;
	private String dbName;
	private String usrName;
	private String password;
	private int port = 3306;
	private boolean autoRefresh;
	private int refreshMin;
	private Connection connection;
	private boolean failed = false;

	public static DBManager get() {
		return instance == null ? (instance = new DBManager()) : instance;
	}

	public boolean isBanned(UUID uuid) {

		return false;
	}

	public boolean isBanned(String ip) {

		return false;
	}

}
