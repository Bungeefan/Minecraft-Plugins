package me.bungeefan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MySQL {

	private BungeeSystem instance;

	private String host;
	private String port;
	private String username;
	private String password;
	// private String backuphost =
	// instance.mysql.getString("MySQL.BungeeSystem.BackUp-Host");
	// private String backupport =
	// instance.mysql.getString("MySQL.BungeeSystem.BackUp-Port");

	private String database;
	private String table;
	private String table2;
	private String hostPex;
	private String portPex;
	private String usernamePex;
	private String passwordPex;
	private String databasePex;

	public Connection connection;

	public MySQL(BungeeSystem instance) throws Exception {
		this.instance = instance;

		host = instance.mysql.getString("MySQL.BungeeSystem.Host");
		port = String.valueOf(instance.mysql.getInt("MySQL.BungeeSystem.Port"));
		username = instance.mysql.getString("MySQL.BungeeSystem.Username");
		password = instance.mysql.getString("MySQL.BungeeSystem.Password");

		database = instance.mysql.getString("MySQL.BungeeSystem.DB");
		table = instance.mysql.getString("MySQL.BungeeSystem.Ban-Table");
		table2 = instance.mysql.getString("MySQL.BungeeSystem.Mute-Table");

		hostPex = instance.mysql.getString("MySQL.PermissionsEx.Host");
		portPex = String.valueOf(instance.mysql.getInt("MySQL.PermissionsEx.Port"));
		usernamePex = instance.mysql.getString("MySQL.PermissionsEx.Username");
		passwordPex = instance.mysql.getString("MySQL.PermissionsEx.Password");
		databasePex = instance.mysql.getString("MySQL.PermissionsEx.DB");

		Class.forName("com.mysql.jdbc.Driver");
		if (!host.isEmpty() && !port.isEmpty() && !username.isEmpty() && !password.isEmpty() && !database.isEmpty()
				&& !table.isEmpty() && !table2.isEmpty()) {
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user="
					+ username + "&password=" + password + "&autoReconnect=true");
			// createDatabaseIfNotExists();
			createTableIfNotExists();
		} else {
			throw new Exception("MySQL File ist leer!");
		}
	}

	public void createDatabaseIfNotExists() throws SQLException {
		String pstatement = "CREATE DATABASE IF NOT EXISTS " + database;
		PreparedStatement ps = connection.prepareStatement(pstatement);
		ps.executeUpdate();
		ps.close();
	}

	public void createTableIfNotExists() throws SQLException {
		String[] sql = new String[2];
		sql[0] = "CREATE TABLE IF NOT EXISTS " + table
				+ " (Spielername VARCHAR(100), UUID VARCHAR(100), IP VARCHAR(100), Rang VARCHAR(100), Grund VARCHAR(100), Von VARCHAR(100), Anfang VARCHAR(100), Dauer VARCHAR(100), Gebannt BOOL);";
		sql[1] = "CREATE TABLE IF NOT EXISTS " + table2
				+ " (Spielername VARCHAR(100), UUID VARCHAR(100), IP VARCHAR(100), Rang VARCHAR(100), Grund VARCHAR(100), Von VARCHAR(100), Anfang VARCHAR(100), Dauer VARCHAR(100), Gemutet BOOL);";
		PreparedStatement ps = null;
		for (int i = 0; i < sql.length; i++) {
			String pstatement = sql[i];
			ps = connection.prepareStatement(pstatement);
			ps.executeUpdate();
		}
		ps.close();
	}

	public boolean isChatLogExisting(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	public Connection getConnection() {
		return connection;
	}

	public Connection getConnection3() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isConnected() {
		return (connection == null ? false : true);
	}

	public void reconnect() throws SQLException {
		if (isConnected()) {
			disconnect();
		}
		connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user="
				+ username + "&password=" + password + "&autoReconnect=true");
	}

	public void disconnect() throws SQLException {
		if (isConnected()) {
			connection.close();
		}
	}

	public boolean hasPermission(ProxiedPlayer p, String permission) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM permissions WHERE name = ?");
		ps.setString(1, p.getName());
		ResultSet result = ps.executeQuery();
		result.next();
		String perm = result.getString("permission");
		result.close();
		ps.close();
		if (perm.equals(permission)) {
			return true;
		} else {
			return false;
		}
	}

	public int getBannedPlayer() throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE Gebannt = true");
		ResultSet result = ps.executeQuery();
		result.next();
		int anzahl = result.getFetchSize();
		result.close();
		ps.close();
		return anzahl;
	}

	public int getMutedPlayer() throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE Gemutet = true");
		ResultSet result = ps.executeQuery();
		result.next();
		int anzahl = result.getFetchSize();
		result.close();
		ps.close();
		return anzahl;
	}

	public boolean isPlayerExisting(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, p.getUniqueId().toString());
		ResultSet result = ps.executeQuery();
		boolean user = result.next();
		result.close();
		ps.close();
		return user;
	}

	public boolean isPlayerExisting2(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, p.getUniqueId().toString());
		ResultSet result = ps.executeQuery();
		boolean user = result.next();
		result.close();
		ps.close();
		return user;
	}

	public boolean isPlayerExisting(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		boolean user = result.next();
		result.close();
		ps.close();
		return user;
	}

	public boolean isPlayerExisting2(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		boolean user = result.next();
		result.close();
		ps.close();
		return user;
	}

	public boolean isIPExisting(String ip) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE IP = ?");
		ps.setString(1, ip);
		ResultSet result = ps.executeQuery();
		boolean user = result.next();
		result.close();
		ps.close();
		return user;
	}

	public boolean isIPExisting2(String ip) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE IP = ?");
		ps.setString(1, ip);
		ResultSet result = ps.executeQuery();
		boolean user = result.next();
		result.close();
		ps.close();
		return user;
	}

	public void registerPlayer(ProxiedPlayer p) throws SQLException {
		if (!isPlayerExisting(p)) {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO " + table
					+ " (Spielername, UUID, IP, Rang, Grund, Von, Anfang, Dauer, Gebannt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, p.getName());
			ps.setString(2, p.getUniqueId().toString());
			ps.setString(3, p.getAddress().getAddress().getHostAddress());
			ps.setString(4, "");
			ps.setString(5, "");
			ps.setString(6, "");
			ps.setString(7, "");
			ps.setString(8, "");
			ps.setBoolean(9, false);
			ps.execute();
			ps.close();
		}
	}

	public void registerPlayer2(ProxiedPlayer p) throws SQLException {
		if (!isPlayerExisting2(p)) {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO " + table2
					+ " (Spielername, UUID, IP, Rang, Grund, Von, Anfang, Dauer, Gemutet) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, p.getName());
			ps.setString(2, p.getUniqueId().toString());
			ps.setString(3, p.getAddress().getAddress().getHostAddress());
			ps.setString(4, "");
			ps.setString(5, "");
			ps.setString(6, "");
			ps.setString(7, "");
			ps.setString(8, "");
			ps.setBoolean(9, false);
			ps.execute();
			ps.close();
		}
	}

	public void registerPlayer(String name, String uuid, String ip) throws SQLException {
		if (!isPlayerExisting(uuid)) {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO " + table
					+ " (Spielername, UUID, IP, Rang, Grund, Von, Anfang, Dauer, Gebannt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, name);
			ps.setString(2, uuid);
			ps.setString(3, ip);
			ps.setString(4, "");
			ps.setString(5, "");
			ps.setString(6, "");
			ps.setString(7, "");
			ps.setString(8, "");
			ps.setBoolean(9, false);
			ps.execute();
			ps.close();
		}
	}

	public void registerPlayer2(String name, String uuid, String ip) throws SQLException {
		if (!isPlayerExisting2(uuid)) {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO " + table2
					+ " (Spielername, UUID, IP, Rang, Grund, Von, Anfang, Dauer, Gemutet) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, name);
			ps.setString(2, uuid);
			ps.setString(3, ip);
			ps.setString(4, "");
			ps.setString(5, "");
			ps.setString(6, "");
			ps.setString(7, "");
			ps.setString(8, "");
			ps.setBoolean(9, false);
			ps.execute();
			ps.close();
		}
	}

	public String getPlayerName(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		String name = result.getString("Spielername");
		result.close();
		ps.close();
		return name;
	}

	public void setPlayerName(String uuid, String name) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE " + table + " SET Spielername = ? WHERE UUID = ?");
		ps.setString(1, name);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();
	}

	public void setPlayerName2(String uuid, String name) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE " + table2 + " SET Spielername = ? WHERE UUID = ?");
		ps.setString(1, name);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();
	}

	public void setIP(String uuid, String ip) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE " + table + " SET IP = ? WHERE UUID = ?");
		ps.setString(1, ip);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();
	}

	public void setIP2(String uuid, String ip) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE " + table2 + " SET IP = ? WHERE UUID = ?");
		ps.setString(1, ip);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();
	}

	public void setRang(String uuid, String rang) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE " + table + " SET Rang = ? WHERE UUID = ?");
		ps.setString(1, rang);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();
	}

	public void setRang2(String uuid, String rang) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE " + table2 + " SET Rang = ? WHERE UUID = ?");
		ps.setString(1, rang);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();
	}

	public String getRang(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		String rang = result.getString("Rang");
		result.close();
		ps.close();
		return rang;
	}

	public String getRang2(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		String rang = result.getString("Rang");
		result.close();
		ps.close();
		return rang;
	}

	public String getReason(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, p.getUniqueId().toString());
		ResultSet result = ps.executeQuery();
		result.next();
		String name = result.getString("Grund");
		result.close();
		ps.close();
		return name;
	}

	public String getReason2(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, p.getUniqueId().toString());
		ResultSet result = ps.executeQuery();
		result.next();
		String name = result.getString("Grund");
		result.close();
		ps.close();
		return name;
	}

	public String getReason(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		String name = result.getString("Grund");
		result.close();
		ps.close();
		return name;
	}

	public String getReason2(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		String name = result.getString("Grund");
		result.close();
		ps.close();
		return name;
	}

	public String getBy(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, p.getUniqueId().toString());
		ResultSet result = ps.executeQuery();
		result.next();
		String name = result.getString("Von");
		result.close();
		ps.close();
		return name;
	}

	public String getBy2(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, p.getUniqueId().toString());
		ResultSet result = ps.executeQuery();
		result.next();
		String name = result.getString("Von");
		result.close();
		ps.close();
		return name;
	}

	public String getBy(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		String name = result.getString("Von");
		result.close();
		ps.close();
		return name;
	}

	public String getBy2(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		String name = result.getString("Von");
		result.close();
		ps.close();
		return name;
	}

	public long getStart(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, p.getUniqueId().toString());
		ResultSet result = ps.executeQuery();
		result.next();
		long start = result.getLong("Anfang");
		result.close();
		ps.close();
		return start;
	}

	public long getStart2(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, p.getUniqueId().toString());
		ResultSet result = ps.executeQuery();
		result.next();
		long start = result.getLong("Anfang");
		result.close();
		ps.close();
		return start;
	}

	public long getStart(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		long start = result.getLong("Anfang");
		result.close();
		ps.close();
		return start;
	}

	public long getStart2(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		long start = result.getLong("Anfang");
		result.close();
		ps.close();
		return start;
	}

	public long getDuration(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, p.getUniqueId().toString());
		ResultSet result = ps.executeQuery();
		result.next();
		long duration = result.getLong("Dauer");
		result.close();
		ps.close();
		return duration;
	}

	public long getDuration2(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, p.getUniqueId().toString());
		ResultSet result = ps.executeQuery();
		result.next();
		long duration = result.getLong("Dauer");
		result.close();
		ps.close();
		return duration;
	}

	public long getDuration(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		long duration = result.getLong("Dauer");
		result.close();
		ps.close();
		return duration;
	}

	public long getDuration2(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		long duration = result.getLong("Dauer");
		result.close();
		ps.close();
		return duration;
	}

	public long getUntil(ProxiedPlayer p) throws SQLException {
		if (getDuration(p) == -1) {
			return -1;
		} else {
			long start = getStart(p);
			long dauer = getDuration(p);
			return start + dauer;
		}
	}

	public long getUntil2(ProxiedPlayer p) throws SQLException {
		if (getDuration2(p) == -1) {
			return -1;
		} else {
			long start = getStart2(p);
			long dauer = getDuration2(p);
			return start + dauer;
		}
	}

	public long getUntil(String uuid) throws SQLException {
		if (getDuration(uuid) == -1) {
			return -1;
		} else {
			long start = getStart(uuid);
			long dauer = getDuration(uuid);
			return start + dauer;
		}
	}

	public long getUntil2(String uuid) throws SQLException {
		if (getDuration2(uuid) == -1) {
			return -1;
		} else {
			long start = getStart2(uuid);
			long dauer = getDuration2(uuid);
			return start + dauer;
		}
	}

	public String getIP(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		String ip = result.getString("IP");
		result.close();
		ps.close();
		return ip;
	}

	public String getIP2(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		String ip = result.getString("IP");
		result.close();
		ps.close();
		return ip;
	}

	public String getUUID(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE Spielername = ?");
		ps.setString(1, p.getName());
		ResultSet result = ps.executeQuery();
		result.next();
		String uuid = result.getString("UUID");
		result.close();
		ps.close();
		return uuid;
	}

	public String getUUID2(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE Spielername = ?");
		ps.setString(1, p.getName());
		ResultSet result = ps.executeQuery();
		result.next();
		String uuid = result.getString("UUID");
		result.close();
		ps.close();
		return uuid;

	}

	public String getUUID(String name) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE Spielername = ?");
		ps.setString(1, name);
		ResultSet result = ps.executeQuery();
		result.next();
		String uuid = result.getString("UUID");
		result.close();
		ps.close();
		return uuid;
	}

	public String getUUID2(String name) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE Spielername = ?");
		ps.setString(1, name);
		ResultSet result = ps.executeQuery();
		result.next();
		String uuid = result.getString("UUID");
		result.close();
		ps.close();
		return uuid;
	}

	public String getUUIDOfIP(String ip) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE IP = ?");
		ps.setString(1, ip);
		ResultSet result = ps.executeQuery();
		result.next();
		String uuid = result.getString("UUID");
		result.close();
		ps.close();
		return uuid;
	}

	public String getUUIDOfIP2(String ip) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE IP = ?");
		ps.setString(1, ip);
		ResultSet result = ps.executeQuery();
		result.next();
		String uuid = result.getString("UUID");
		result.close();
		ps.close();
		return uuid;
	}

	public void setBanned(ProxiedPlayer p, String reason, ProxiedPlayer by, long anfang, long dauer, String ip)
			throws SQLException {
		registerPlayer(p);

		PreparedStatement ps;
		ps = connection.prepareStatement("UPDATE " + table + " SET Grund = ? WHERE UUID = ?");
		ps.setString(1, reason);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Von = ? WHERE UUID = ?");
		ps.setString(1, by.getName());
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Anfang = ? WHERE UUID = ?");
		ps.setLong(1, anfang);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Dauer = ? WHERE UUID = ?");
		ps.setLong(1, dauer);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET IP = ? WHERE UUID = ?");
		ps.setString(1, ip);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Gebannt = ? WHERE UUID = ?");
		ps.setBoolean(1, true);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();
	}

	public void setBanned2(ProxiedPlayer p, String reason, ProxiedPlayer by, long anfang, long dauer, String ip)
			throws SQLException {
		registerPlayer(p);

		PreparedStatement ps;
		ps = connection.prepareStatement("UPDATE " + table2 + " SET Grund = ? WHERE UUID = ?");
		ps.setString(1, reason);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Von = ? WHERE UUID = ?");
		ps.setString(1, by.getName());
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Anfang = ? WHERE UUID = ?");
		ps.setLong(1, anfang);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Dauer = ? WHERE UUID = ?");
		ps.setLong(1, dauer);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET IP = ? WHERE UUID = ?");
		ps.setString(1, ip);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Gemutet = ? WHERE UUID = ?");
		ps.setBoolean(1, true);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

	}

	public void setBanned(String uuid, String reason, ProxiedPlayer by, long anfang, long dauer, String ip)
			throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("UPDATE " + table + " SET Grund = ? WHERE UUID = ?");
		ps.setString(1, reason);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Von = ? WHERE UUID = ?");
		ps.setString(1, by.getName());
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Anfang = ? WHERE UUID = ?");
		ps.setLong(1, anfang);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Dauer = ? WHERE UUID = ?");
		ps.setLong(1, dauer);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET IP = ? WHERE UUID = ?");
		ps.setString(1, ip);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Gebannt = ? WHERE UUID = ?");
		ps.setBoolean(1, true);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

	}

	public void setBanned2(String uuid, String reason, ProxiedPlayer by, long anfang, long dauer, String ip)
			throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("UPDATE " + table2 + " SET Grund = ? WHERE UUID = ?");
		ps.setString(1, reason);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Von = ? WHERE UUID = ?");
		ps.setString(1, by.getName());
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Anfang = ? WHERE UUID = ?");
		ps.setLong(1, anfang);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Dauer = ? WHERE UUID = ?");
		ps.setLong(1, dauer);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET IP = ? WHERE UUID = ?");
		ps.setString(1, ip);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Gemutet = ? WHERE UUID = ?");
		ps.setBoolean(1, true);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();
	}

	public void setBanned(ProxiedPlayer p, String reason, String by, long anfang, long dauer, String ip)
			throws SQLException {
		registerPlayer(p);

		PreparedStatement ps;
		ps = connection.prepareStatement("UPDATE " + table + " SET Grund = ? WHERE UUID = ?");
		ps.setString(1, reason);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Von = ? WHERE UUID = ?");
		ps.setString(1, by);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Anfang = ? WHERE UUID = ?");
		ps.setLong(1, anfang);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Dauer = ? WHERE UUID = ?");
		ps.setLong(1, dauer);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET IP = ? WHERE UUID = ?");
		ps.setString(1, ip);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Gebannt = ? WHERE UUID = ?");
		ps.setBoolean(1, true);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();
	}

	public void setBanned2(ProxiedPlayer p, String reason, String by, long anfang, long dauer, String ip)
			throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("UPDATE " + table2 + " SET Grund = ? WHERE UUID = ?");
		ps.setString(1, reason);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Von = ? WHERE UUID = ?");
		ps.setString(1, by);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Anfang = ? WHERE UUID = ?");
		ps.setLong(1, anfang);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Dauer = ? WHERE UUID = ?");
		ps.setLong(1, dauer);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET IP = ? WHERE UUID = ?");
		ps.setString(1, ip);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Gemutet = ? WHERE UUID = ?");
		ps.setBoolean(1, true);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();
	}

	public void setBanned(String uuid, String reason, String by, long anfang, long dauer, String ip)
			throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("UPDATE " + table + " SET Grund = ? WHERE UUID = ?");
		ps.setString(1, reason);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Von = ? WHERE UUID = ?");
		ps.setString(1, by);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Anfang = ? WHERE UUID = ?");
		ps.setLong(1, anfang);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Dauer = ? WHERE UUID = ?");
		ps.setLong(1, dauer);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET IP = ? WHERE UUID = ?");
		ps.setString(1, ip);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table + " SET Gebannt = ? WHERE UUID = ?");
		ps.setBoolean(1, true);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();
	}

	public void setBanned2(String uuid, String reason, String by, long anfang, long dauer, String ip)
			throws SQLException {
		PreparedStatement ps;
		ps = connection.prepareStatement("UPDATE " + table2 + " SET Grund = ? WHERE UUID = ?");
		ps.setString(1, reason);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Von = ? WHERE UUID = ?");
		ps.setString(1, by);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Anfang = ? WHERE UUID = ?");
		ps.setLong(1, anfang);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Dauer = ? WHERE UUID = ?");
		ps.setLong(1, dauer);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET IP = ? WHERE UUID = ?");
		ps.setString(1, ip);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();

		ps = connection.prepareStatement("UPDATE " + table2 + " SET Gemutet = ? WHERE UUID = ?");
		ps.setBoolean(1, true);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();
	}

	public void setUnBanned(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE " + table + " SET Gebannt = ? WHERE UUID = ?");
		ps.setBoolean(1, false);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();
	}

	public void setUnBanned2(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE " + table2 + " SET Gemutet = ? WHERE UUID = ?");
		ps.setBoolean(1, false);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();
	}

	public void setUnBanned(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE " + table + " SET Gebannt = ? WHERE UUID = ?");
		ps.setBoolean(1, false);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();
	}

	public void setUnBanned2(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE " + table2 + " SET Gemutet = ? WHERE UUID = ?");
		ps.setBoolean(1, false);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();
	}

	public boolean isBanned(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, p.getUniqueId().toString());
		ResultSet result = ps.executeQuery();
		result.next();
		boolean banned = result.getBoolean("Gebannt");
		result.close();
		ps.close();
		;
		if ((getUntil(p) >= System.currentTimeMillis() || getUntil(p) == -1) && banned) {
			banned = true;
		} else {
			banned = false;
		}
		ps = connection.prepareStatement("UPDATE " + table + " SET Gebannt = ? WHERE UUID = ?");
		ps.setBoolean(1, banned);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();
		return banned;
	}

	public boolean isBanned2(ProxiedPlayer p) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, p.getUniqueId().toString());
		ResultSet result = ps.executeQuery();
		result.next();
		boolean mutet = result.getBoolean("Gemutet");
		result.close();
		ps.close();
		if ((getUntil2(p) >= System.currentTimeMillis() || getUntil2(p) == -1) && mutet) {
			mutet = true;
		} else {
			mutet = false;
		}
		ps = connection.prepareStatement("UPDATE " + table2 + " SET Gemutet = ? WHERE UUID = ?");
		ps.setBoolean(1, mutet);
		ps.setString(2, p.getUniqueId().toString());
		ps.executeUpdate();
		ps.close();
		return mutet;
	}

	public boolean isBanned(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		boolean banned = result.getBoolean("Gebannt");
		result.close();
		ps.close();
		if ((getUntil(uuid) >= System.currentTimeMillis() || getUntil(uuid) == -1) && banned) {
			banned = true;
		} else {
			banned = false;
		}
		ps = connection.prepareStatement("UPDATE " + table + " SET Gebannt = ? WHERE UUID = ?");
		ps.setBoolean(1, banned);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();
		return banned;
	}

	public boolean isBanned2(String uuid) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table2 + " WHERE UUID = ?");
		ps.setString(1, uuid);
		ResultSet result = ps.executeQuery();
		result.next();
		boolean mutet = result.getBoolean("Gemutet");
		result.close();
		ps.close();
		if ((getUntil2(uuid) >= System.currentTimeMillis() || getUntil2(uuid) == -1) && mutet) {
			mutet = true;
		} else {
			mutet = false;
		}
		ps = connection.prepareStatement("UPDATE " + table2 + " SET Gemutet = ? WHERE UUID = ?");
		ps.setBoolean(1, mutet);
		ps.setString(2, uuid);
		ps.executeUpdate();
		ps.close();
		return mutet;
	}

	public String getPlayerGroup(String uuid) {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM permissions_inheritance WHERE child = ?");
			ps.setString(1, uuid);
			ResultSet result = ps.executeQuery();
			result.next();
			String rank = result.getString("parent");
			result.close();
			ps.close();
			return rank;
		} catch (SQLException ex) {
			// ex.printStackTrace();
		}
		return "Spieler";
	}

	public String getPlayerGroupUntil(String uuid, String group) {
		try {
			PreparedStatement ps = connection
					.prepareStatement("SELECT * FROM permissions WHERE name = ? AND permission = ?");
			ps.setString(1, uuid);
			ps.setString(2, "group-" + group + "-until");
			ResultSet result = ps.executeQuery();
			result.next();
			String until = result.getString("value");
			result.close();
			ps.close();
			return until;
		} catch (SQLException ex) {
			// ex.printStackTrace();
		}
		return "-1";
	}
}