package me.bungeefan;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

import java.util.UUID;

public class ChatLogger {

	private UUID uuid;
	private HashMap<Entry<Long, String>, String> log = new HashMap<Entry<Long, String>, String>();
	private MySQL sql;
	
	public ChatLogger(UUID uuid) {
		this.uuid = uuid;
	}
	public void log(String server, String message) {
		long timestamp = System.currentTimeMillis();
		Entry<Long, String> entry = new Entry<Long, String>() {
			@Override
			public String setValue(String value) {
				return null;
			}
			@Override
			public String getValue() {
				return server;
			}
			@Override
			public Long getKey() {
				return timestamp;
			}
		};
		log.put(entry, message);
	}
	public String generate() {
		int count = 0;
		String id = new BigInteger(130, new SecureRandom()).toString(32);
		if (!sql.isChatLogExisting(id)) {
			try {
				for (Entry<Long, String> logs : log.keySet()) {
					PreparedStatement st;
					if (count == 0) {
						st = sql.getConnection3().prepareStatement("CREATE TABLE IF NOT EXISTS " + id + " (UUID Varchar(200), Server TEXT, Zeit TEXT, Nachricht TEXT)");
						st.execute();
						st.close();
					}
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(logs.getKey());
					String time = sdf.format(cal.getTime());
					st = sql.getConnection3().prepareStatement("INSERT INTO " + id + " (UUID, Server, Zeit, Nachricht) VALUES (?, ?, ?, ?)");
					st.setString(1, uuid.toString());
					st.setString(2, logs.getValue());	
					st.setString(3, time);
					st.setString(4, log.get(logs));
					st.execute();
					st.close();
					count++;
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			if (count == 0) {
				id = "NO MESSAGES";
			}
			return id;
		} else {
			return generate();
		}
	}
}