package me.bungeefan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Iterables;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.bungeefan.commands.BanCMD;
import me.bungeefan.commands.BanlistCMD;
import me.bungeefan.commands.BroadcastToggleCMD;
import me.bungeefan.commands.CheckCMD;
import me.bungeefan.commands.ClearChatCMD;
import me.bungeefan.commands.GlobalCMD;
import me.bungeefan.commands.JoinMeCMD;
import me.bungeefan.commands.KickCMD;
import me.bungeefan.commands.LobbyCMD;
import me.bungeefan.commands.MSGCMD;
import me.bungeefan.commands.MSGToggleCMD;
import me.bungeefan.commands.WartungCMD;
import me.bungeefan.commands.MuteCMD;
import me.bungeefan.commands.NotifyToggleCMD;
import me.bungeefan.commands.OnlineCMD;
import me.bungeefan.commands.PingCMD;
import me.bungeefan.commands.PremiumCMD;
import me.bungeefan.commands.PremiumPlusCMD;
import me.bungeefan.commands.ReplyCMD;
import me.bungeefan.commands.ReportCMD;
import me.bungeefan.commands.ReportsCMD;
import me.bungeefan.commands.UnbanCMD;
import me.bungeefan.commands.UnmuteCMD;
import me.bungeefan.commands.WhereAmICMD;
import me.bungeefan.commands.WhereIsCMD;
import me.bungeefan.commands.YouTuberCMD;
import me.bungeefan.listener.ChatListener;
import me.bungeefan.listener.LoginListener;
import me.bungeefan.listener.PlayerDisconnectListener;
import me.bungeefan.listener.PluginMessageListener;
import me.bungeefan.listener.PostLoginListener;
import me.bungeefan.listener.ProxyPingListener;
import me.bungeefan.listener.ServerConnectListener;
import me.bungeefan.listener.ServerConnectedListener;
import me.bungeefan.listener.ServerKickListener;
import me.bungeefan.listener.TabCompleteListener;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeSystem extends Plugin {

	private File configfile;
	private File messagesfile;
	private File mysqlfile;
	private File permissionsfile;
	public Configuration config;
	public Configuration messages;
	public Configuration mysql;
	public Configuration permissions;
	public int broadcast = 0;
	public boolean wartung = false;
	public boolean restart = false;
	public boolean broadcastON;
	public List<String> premiumlayout = new ArrayList<String>();
	public List<String> premiumpluslayout = new ArrayList<String>();
	public List<String> youtuberlayout = new ArrayList<String>();
	public HashMap<ProxiedPlayer, ProxiedPlayer> messager = new HashMap<ProxiedPlayer, ProxiedPlayer>();
	// public HashMap<UUID, ChatLogger> logger = new HashMap<UUID, ChatLogger>();
	public List<ProxiedPlayer> msgtoggle = new ArrayList<ProxiedPlayer>();
	public List<ProxiedPlayer> notifytoggle = new ArrayList<ProxiedPlayer>();
	public List<String> notifymsg = new ArrayList<String>();
	public List<String> layout = new ArrayList<String>();
	public List<String> broadcastmsg = new ArrayList<String>();
	public List<String> wordblacklist = new ArrayList<String>();
	public List<String> blockedCMDs;
	public String prefix;
	public MySQL sql;
	public List<String> ranks;

	public ArrayList<ProxiedPlayer> messagecd = new ArrayList<ProxiedPlayer>();

	public ArrayList<ProxiedPlayer> reportcd = new ArrayList<ProxiedPlayer>();
	public HashMap<ProxiedPlayer, Report> reports = new HashMap<ProxiedPlayer, Report>();
	public List<ProxiedPlayer> reporttoggle = new ArrayList<ProxiedPlayer>();
	public HashMap<ProxiedPlayer, String> joinMe = new HashMap<ProxiedPlayer, String>();

	@Override
	public void onEnable() {
		try {
			loadDefaultFiles();
			registerCommands();
			registerListener();
			blockedCMDs = messages.getStringList("BlockedCMDs.CMDs");
			premiumlayout = messages.getStringList("Premium.Layout");
			premiumpluslayout = messages.getStringList("PremiumPlus.Layout");
			youtuberlayout = messages.getStringList("Youtuber.Layout");
			broadcastmsg = messages.getStringList("Broadcast.Nachrichten");
			wordblacklist = messages.getStringList("BlockedWords.Words");
			wartung = config.getBoolean("Wartung.aktiviert");
			broadcastON = messages.getBoolean("Broadcast.aktiviert");
			prefix = messages.getString("Allgemein.Prefix").replaceAll("&", "§");

			ranks = permissions.getStringList("Ranks");

			// ProxyServer.getInstance().registerChannel("AntiKillaura");
			// ProxyServer.getInstance().registerChannel("Maintenance");
			// ProxyServer.getInstance().registerChannel("GameTempBan");

			if (config.getBoolean("ServerRestart.aktiviert")) {
				restart();
			}
			if (messages.getBoolean("Broadcast.aktiviert")) {
				broadcast();
			}

			// ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
			//
			// @Override
			// public void run() {
			// UUIDFetcher.clearCache();
			// }
			// }, 10, 10, TimeUnit.MINUTES);

			// ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
			// @Override
			// public void run() {
			// int fake = (int) (ProxyServer.getInstance().getOnlineCount()
			// * (double) (config.getDouble("Fake") / 100.0));
			// ByteArrayDataOutput out = ByteStreams.newDataOutput();
			// if (config.getBoolean("RealMaxPlayers")) {
			// out.writeUTF((ProxyServer.getInstance().getOnlineCount() + fake) + "/"
			// + config.getInt("MaxPlayers"));
			// } else {
			// out.writeUTF((ProxyServer.getInstance().getOnlineCount() + fake) + "/"
			// + ((ProxyServer.getInstance().getOnlineCount() + fake) + 1));
			// }
			// for (String servers : ProxyServer.getInstance().getServers().keySet()) {
			// ServerInfo server = ProxyServer.getInstance().getServerInfo(servers);
			// if (server.getName().equalsIgnoreCase("lobby")) {
			// server.sendData("Players", out.toByteArray());
			// }
			// }
			// }
			// }, 1, 1, TimeUnit.SECONDS);
			BungeeSystem instance = this;
			ProxyServer.getInstance().getScheduler().runAsync(this, new Runnable() {

				@Override
				public void run() {
					try {
						sql = new MySQL(instance);
					} catch (Exception e) {
						BungeeCord.getInstance().getConsole().sendMessage(prefix
								+ "§cMySQL Fehler, Einige Funktionen sind deaktiviert!\nMessage: " + e.getMessage());
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			BungeeCord.getInstance().getConsole().sendMessage("SYSTEM: §4PLUGIN KONNTE NICHT GESTARTET WERDEN!");
			BungeeCord.getInstance().getConsole().sendMessage("SYSTEM: Grund: " + e.getMessage());
			BungeeCord.getInstance().getConsole()
					.sendMessage("SYSTEM: §4SERVER WIRD  IN §e5 §4SEKUNDEN HERUNTERGEFAHREN!");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			ProxyServer.getInstance().stop();
		}
	}

	@Override
	public void onDisable() {
		if (sql != null) {
			try {
				sql.disconnect();
			} catch (SQLException e) {
			}
		}
		try {
			saveFiles();
		} catch (IOException | NullPointerException e) {
		}
	}

	public void loadDefaultFiles() throws IOException {
		if (!getDataFolder().exists())
			getDataFolder().mkdir();

		configfile = new File(getDataFolder(), "config.yml");
		messagesfile = new File(getDataFolder(), "messages.yml");
		mysqlfile = new File(getDataFolder(), "MySQL.yml");
		permissionsfile = new File(getDataFolder(), "permissions.yml");

		if (!configfile.exists()) {
			try (InputStream in = getResourceAsStream("config.yml")) {
				Files.copy(in, configfile.toPath());
			} catch (IOException | NullPointerException e) {
				BungeeCord.getInstance().getConsole().sendMessage("SYSTEM: §4Config (config.yml) Fehler");
				throw new IOException();
			}
		}
		if (!messagesfile.exists()) {
			try (InputStream in = getResourceAsStream("messages.yml")) {
				Files.copy(in, messagesfile.toPath());
			} catch (IOException | NullPointerException e) {
				BungeeCord.getInstance().getConsole().sendMessage("SYSTEM: §4Config (messages.yml) Fehler");
				throw new IOException();
			}
		}
		if (!mysqlfile.exists()) {
			try (InputStream in = getResourceAsStream("MySQL.yml")) {
				Files.copy(in, mysqlfile.toPath());
			} catch (IOException | NullPointerException e) {
				BungeeCord.getInstance().getConsole().sendMessage("SYSTEM: §4Config (MySQL.yml) Fehler");
				throw new IOException();
			}
		}

		if (!permissionsfile.exists()) {
			try (InputStream in = getResourceAsStream("permissions.yml")) {
				Files.copy(in, permissionsfile.toPath());
			} catch (IOException | NullPointerException e) {
				BungeeCord.getInstance().getConsole().sendMessage("SYSTEM: §4Config (MySQL.yml) Fehler");
				throw new IOException();
			}
		}

		try {
			loadFiles();
		} catch (IOException | NullPointerException e) {
			BungeeCord.getInstance().getConsole().sendMessage("SYSTEM: §4Allgemeiner Config Fehler");
			throw new IOException();
		}
	}

	public void loadFiles() throws IOException, NullPointerException {
		if (configfile.exists()) {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configfile);
		}
		if (configfile.exists()) {
			messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(messagesfile);
		}
		if (mysqlfile.exists()) {
			mysql = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mysqlfile);
		}
		if (permissionsfile.exists()) {
			permissions = ConfigurationProvider.getProvider(YamlConfiguration.class).load(permissionsfile);
		}
	}

	public void saveFiles() throws IOException, NullPointerException {
		if (configfile.exists()) {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
		}
		if (messagesfile.exists()) {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(messages, messagesfile);
		}
		if (mysqlfile.exists()) {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(mysql, mysqlfile);
		}
		if (permissionsfile.exists()) {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(permissions, permissionsfile);
		}
	}

	public boolean checkRang(CommandSender cs, ProxiedPlayer pt) {
		int index = 0;
		int index1 = 0;
		for (int i = (ranks.size() - 1); i >= 0; i--) {
			String rang = (String) ranks.get(i);
			if (cs.hasPermission("bs." + rang)) {
				index = i;
			}
			if (pt.hasPermission("bs." + rang)) {
				index1 = i;
			}
		}
		if (index < index1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkRang(CommandSender cs, String uuid) {
		String ptRank;
		try {
			ptRank = sql.getRang(uuid);
			int index = 0;
			int index1 = 0;
			for (int i = (ranks.size() - 1); i >= 0; i--) {
				String rang = (String) ranks.get(i);
				if (cs.hasPermission("bs." + rang)) {
					index = i;
				}
				if (ptRank.equalsIgnoreCase("bs." + rang)) {
					index1 = i;
				}
			}
			if (index < index1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean hasBanRestriction(CommandSender cs) {
		String rang = getRang(cs.getPermissions());
		System.out.println(rang);
		String ban = permissions.getString("Restrictions." + rang + ".Ban");
		if (ban.isEmpty() || ban.equals("0")) {
			return false;
		}
		return true;
	}

	public boolean hasMuteRestriction(CommandSender cs) {
		String rang = getRang(cs.getPermissions());
		String mute = permissions.getString("Restrictions." + rang + ".Mute");
		if (mute.isEmpty() || mute.equals("0")) {
			return false;
		}
		return true;
	}

	public long getBanRestriction(CommandSender cs) {
		String rang = getRang(cs.getPermissions());
		String zeit = permissions.getString("Restrictions." + rang + ".Ban");
		System.out.println(zeit);
		return getDauer(zeit);
	}

	public long getMuteRestriction(CommandSender cs) {
		String rang = getRang(cs.getPermissions());
		String zeit = permissions.getString("Restrictions." + rang + ".Mute");
		return getDauer(zeit);
	}

	public String getBanRestriction2(CommandSender cs) {
		String rang = getRang(cs.getPermissions());
		return permissions.getString("Restrictions." + rang + ".Ban");
	}

	public String getMuteRestriction2(CommandSender cs) {
		String rang = getRang(cs.getPermissions());
		return permissions.getString("Restrictions." + rang + ".Mute");
	}

	public String getRang(Collection<String> permissions) {
		String rank = "";
		for (int i = (ranks.size() - 1); i >= 0; i--) {
			String rang = (String) ranks.get(i);
			if (permissions.contains("bs." + rang)) {
				rank = "bs." + rang;
			}
		}
		rank = rank.replaceAll("bs.", "");
		return rank;
	}

	public String getUUID(String name) {
		String uuid = null;
		try {
			uuid = getUUID_fromURL(config.getString("UUID-Fetcher.MOJANG-API.URL"), name,
					(config.getString("UUID-Fetcher.MOJANG-API.Key")));
		} catch (IOException e) {
			System.out.println("Error -> " + e.getMessage());
			System.out.println("!! Fehler beim abrufen der UUID von " + name);
			System.out.println("!! MOJANG-API konnnte unter " + config.getString("UUID-Fetcher.MOJANG-API.URL")
					+ " nicht erreicht werden!");
		}

		if (uuid == null) {
			System.out.println("BackUP-API wird verwendet!");
			try {
				uuid = getUUID_fromURL(config.getString("UUID-Fetcher.BackUp-API.URL"), name,
						(config.getString("UUID-Fetcher.BackUp-API.Key")));
			} catch (IOException e) {
				System.out.println("!! Fehler beim abrufen der UUID von " + name);
				System.out.println("!! BackUp-API konnnte unter " + config.getString("UUID-Fetcher.BackUp-API.URL")
						+ " nicht erreicht werden!");
			}
		}
		if (uuid == null) {
			System.out.println("!! !! UUID von " + name + " konnte nicht abgerufen werden!");
			System.out.println("!! Pr\u00fcfe bitte den Namen!");
		}
		return uuid;
	}

	private String getUUID_fromURL(String url, String name, String key) throws IOException {
		try {
			HttpURLConnection request = (HttpURLConnection) new URL(url.replaceAll("%Spieler%", name)).openConnection();
			request.connect();
			JsonParser jp = new JsonParser();
			JsonObject json = (JsonObject) jp.parse(new InputStreamReader(request.getInputStream()));
			return json.get(key).toString().replaceAll("/", "").replaceAll("\"", "");
		} catch (Exception e) {

		}
		return null;
	}

	public String sendausgabe(String message) {
		return (prefix + this.messages.getString(message).replaceAll("&", "§"));
	}

	public long getDauer(String zeit) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		int amount;
		long dauer;
		if (zeit.endsWith("s")) {
			amount = Integer.parseInt(zeit.substring(0, zeit.length() - 1));
			cal.set(Calendar.SECOND, amount);
			dauer = cal.getTimeInMillis();
		} else if (zeit.endsWith("min")) {
			amount = Integer.parseInt(zeit.substring(0, zeit.length() - 3));
			cal.set(Calendar.MINUTE, amount);
			dauer = cal.getTimeInMillis();
		} else if (zeit.endsWith("h")) {
			amount = Integer.parseInt(zeit.substring(0, zeit.length() - 1));
			cal.set(Calendar.HOUR_OF_DAY, amount);
			dauer = cal.getTimeInMillis();
		} else if (zeit.endsWith("d")) {
			amount = Integer.parseInt(zeit.substring(0, zeit.length() - 1));
			cal.set(Calendar.DAY_OF_MONTH, amount);
			dauer = cal.getTimeInMillis();
		} else if (zeit.endsWith("m")) {
			amount = Integer.parseInt(zeit.substring(0, zeit.length() - 1));
			cal.set(Calendar.MONTH, amount);
			dauer = cal.getTimeInMillis();
		} else {
			amount = Integer.parseInt(zeit.replaceAll("\\D", ""));
			cal.set(Calendar.SECOND, amount);
			dauer = cal.getTimeInMillis();
		}
		return dauer;
	}

	public String getRemainingTime(long end) {
		String remainingTime;
		if (end == -1) {
			remainingTime = "§4§lPermanent";
		} else {
			end += 1000;
			long difference = end - System.currentTimeMillis();
			/*
			 * long years = (long) Math.floor(seconds / 960534450); seconds -= (years *
			 * 960534450); long months = (long) Math.floor(seconds / 2629800); seconds -=
			 * (months * 2629800); long days = (long) Math.floor(seconds / 86400); seconds
			 * -= (days * 86400); long hours = (long) Math.floor(seconds / 3600); seconds -=
			 * (hours * 3600); long minutes = (long) Math.floor(seconds / 60); seconds -=
			 * (minutes * 60);
			 */
			long sekunden = (difference / 1000);
			int minuten = 0;
			int stunden = 0;
			int tage = 0;
			int monate = 0;
			while (sekunden >= 60) {
				minuten++;
				sekunden -= 60;
			}
			while (minuten >= 60) {
				stunden++;
				minuten -= 60;
			}
			while (stunden >= 24) {
				tage++;
				stunden -= 24;
			}
			while (tage >= 30) {
				monate++;
				tage -= 30;
			}
			remainingTime = monate + " Monat(e), " + tage + " Tag(e), " + stunden + " Stunde(n), " + minuten
					+ " Minute(n) " + sekunden + " Sekunde(n)";
		}
		return remainingTime;
	}

	public String banMessage(String uuid) {
		try {
			layout = messages.getStringList("Ban.Layout");
			String operator = sql.getBy(uuid);
			String grund = sql.getReason(uuid);
			long end = sql.getUntil(uuid);
			String remainingTime = getRemainingTime(end);
			String banmessage = "";
			for (int i = 0; i < layout.size(); i++) {
				banmessage = banmessage + ((((layout.get(i).replaceAll("&", "§")).replaceAll("%Dauer%", remainingTime))
						.replaceAll("%Grund%", grund)).replaceAll("%OPERATOR%", operator)) + "\n";
			}
			return banmessage;
		} catch (Exception e) {
			System.out.println(messages.getString("Allgemein.Fehler"));
			e.printStackTrace();
			return "Banmessage: \n§cServer Fehler";
		}
	}

	public String muteMessage(String uuid) {
		try {
			layout = this.messages.getStringList("Mute.Layout");
			String operator = sql.getBy2(uuid);
			String grund = sql.getReason2(uuid);
			long end = sql.getUntil2(uuid);
			String remainingTime;
			if (end == -1) {
				remainingTime = "§4§lPermanent";
			} else {
				end += 1000;
				remainingTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(end);
			}
			String mutemessage = "";
			for (int i = 0; i < layout.size(); i++) {
				mutemessage = mutemessage + prefix
						+ ((((layout.get(i).replaceAll("&", "§")).replaceAll("%Dauer%", remainingTime))
								.replaceAll("%Grund%", grund)).replaceAll("%OPERATOR%", operator))
						+ "\n";
			}
			return mutemessage;
		} catch (SQLException e) {
			System.out.println(messages.getString("Allgemein.Fehler"));
			return "§cServer Fehler";
		}
	}

	public String kickMessage(CommandSender cs, String grund) {
		layout = this.messages.getStringList("Kick.Layout");
		String kickmessage = "";
		for (int i = 0; i < layout.size(); i++) {
			kickmessage = kickmessage + (((layout.get(i).replaceAll("&", "§")).replaceAll("%Grund%", grund))
					.replaceAll("%OPERATOR%", cs.getName())) + "\n";
		}
		return kickmessage;
	}

	public void notify(String type, String uuid) {
		String spieler = "null";
		String operator = "null";
		String grund = "null";
		try {
			switch (type) {
			case "Ban":
				spieler = sql.getPlayerName(uuid);
				operator = sql.getBy(uuid);
				grund = sql.getReason(uuid);
				break;
			case "Mute":
				spieler = sql.getPlayerName(uuid);
				operator = sql.getBy2(uuid);
				grund = sql.getReason2(uuid);
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(messages.getString("Allgemein.Fehler"));
		}
		notifymsg = messages.getStringList(type + ".Notification");
		String notifymessage = "";
		for (int i = 0; i < notifymsg.size(); i++) {
			notifymessage = notifymessage + ((((notifymsg.get(i).replaceAll("&", "§")).replaceAll("%Spieler%", spieler))
					.replaceAll("%OPERATOR%", operator)).replaceAll("%Grund%", grund)) + "\n";
		}
		Collection<? extends ProxiedPlayer> onlineplayer = BungeeCord.getInstance().getPlayers();
		if (onlineplayer.size() != 0) {
			for (int i = 0; i < onlineplayer.size(); i++) {
				ProxiedPlayer player = Iterables.get(onlineplayer, i);
				if (player.hasPermission("bs.notify") || player.hasPermission("bs.admin"))
					player.sendMessage(notifymessage);
			}
		}
	}

	public void notifyKick(String name, String by, String grund) {
		String type = "Kick";
		notifymsg = messages.getStringList(type + ".Notification");
		String notifymessage = "";
		for (int i = 0; i < notifymsg.size(); i++) {
			notifymessage = notifymessage + ((((notifymsg.get(i).replaceAll("&", "§")).replaceAll("%Spieler%", name))
					.replaceAll("%OPERATOR%", by)).replaceAll("%Grund%", grund)) + "\n";
		}
		Collection<? extends ProxiedPlayer> onlineplayer = BungeeCord.getInstance().getPlayers();
		if (onlineplayer.size() != 0) {
			for (int i = 0; i < onlineplayer.size(); i++) {
				ProxiedPlayer player = Iterables.get(onlineplayer, i);
				if (player.hasPermission("bs.notify") || player.hasPermission("bs.admin"))
					player.sendMessage(notifymessage);
			}
		}
	}

	public void kickAllIP(String ip) throws SQLException {
		String uuid = sql.getUUIDOfIP(ip);
		for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
			if (p.getAddress().getAddress().getHostAddress().equals(ip)) {
				p.disconnect(banMessage(uuid));
			}
		}
	}

	public void setbroadcastON(boolean on) {
		if (on) {
			broadcastON = true;
			messages.set("Broadcast.aktiviert", broadcastON);
		} else {
			broadcastON = false;
			messages.set("Broadcast.aktiviert", broadcastON);
		}
		try {
			saveFiles();
		} catch (NullPointerException | IOException e) {
			e.printStackTrace();
		}
	}

	public void broadcast() {
		int zeit = messages.getInt("Broadcast.Wiederholungen");
		ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
			@Override
			public void run() {
				if (broadcastON) {
					String nachricht = ("\n" + broadcastmsg.get(broadcast) + "\n").replaceAll("&", "§");
					if (!nachricht.startsWith("/") || (!nachricht.equalsIgnoreCase(""))) {
						// if (nachricht.contains("/ping")) {
						// String[] teile = nachricht.split("/ping");
						// TextComponent msg = new TextComponent(teile[0]);
						// TextComponent ce = new TextComponent("/ping");
						// ce.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ping"));
						// ce.setColor(ChatColor.YELLOW);
						// msg.addExtra(ce);
						// msg.addExtra(teile[1]);
						// ProxyServer.getInstance().broadcast(msg);
						// } else if (nachricht.contains("/premium+")) {
						// String[] teile = nachricht.split("/premium+");
						// TextComponent msg = new TextComponent(teile[0]);
						// TextComponent ce = new TextComponent("/premium+");
						// ce.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/premium+"));
						// ce.setColor(ChatColor.YELLOW);
						// TextComponent behind = new TextComponent(teile[1]);
						// msg.addExtra(ce);
						// msg.addExtra(behind);
						// ProxyServer.getInstance().broadcast(msg);
						// } else if (nachricht.contains("/premium")) {
						// String[] teile = nachricht.split("/premium");
						// TextComponent msg = new TextComponent(teile[0]);
						// TextComponent ce = new TextComponent("/premium");
						// ce.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/premium"));
						// ce.setColor(ChatColor.YELLOW);
						// TextComponent behind = new TextComponent(teile[1]);
						// msg.addExtra(ce);
						// msg.addExtra(behind);
						// ProxyServer.getInstance().broadcast(msg);
						// } else if (nachricht.contains("/youtube")) {
						// String[] teile = nachricht.split("/youtube");
						// TextComponent msg = new TextComponent(teile[0]);
						// TextComponent ce = new TextComponent("/youtube");
						// ce.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/youtube"));
						// ce.setColor(ChatColor.YELLOW);
						// TextComponent behind = new TextComponent(teile[1]);
						// msg.addExtra(ce);
						// msg.addExtra(behind);
						// ProxyServer.getInstance().broadcast(msg);
						// } else {
						ProxyServer.getInstance().broadcast(nachricht);
						// }
					}
					broadcast++;
					if (broadcast >= broadcastmsg.size()) {
						broadcast = 0;
					}
				}
			}
		}, zeit, zeit, TimeUnit.MINUTES);
	}

	public void restart() {
		String prefix = messages.getString("Allgemein.Prefix").replaceAll("&", "§");
		String minMSG = messages.getString("ServerRestart.MinMSG").replaceAll("&", "§");
		String sekMSG = messages.getString("ServerRestart.SekMSG").replaceAll("&", "§");
		String restartMSG = messages.getString("ServerRestart.RestartMSG").replaceAll("&", "§");
		final int hour = messages.getInt("ServerRestart.Uhrzeit") - 1;
		ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
			@Override
			public void run() {
				Calendar cal = Calendar.getInstance();
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 45
						&& cal.get(Calendar.SECOND) == 0) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + minMSG.replaceAll("%Zeit", String.valueOf(15))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 50
						&& cal.get(Calendar.SECOND) == 0) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + minMSG.replaceAll("%Zeit", String.valueOf(10))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 55
						&& cal.get(Calendar.SECOND) == 0) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + minMSG.replaceAll("%Zeit", String.valueOf(5))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 56
						&& cal.get(Calendar.SECOND) == 0) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + minMSG.replaceAll("%Zeit", String.valueOf(4))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 57
						&& cal.get(Calendar.SECOND) == 0) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + minMSG.replaceAll("%Zeit", String.valueOf(3))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 58
						&& cal.get(Calendar.SECOND) == 0) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + minMSG.replaceAll("%Zeit", String.valueOf(2))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 59
						&& cal.get(Calendar.SECOND) == 0) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + minMSG.replaceAll("%Zeit", String.valueOf(1))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 59
						&& cal.get(Calendar.SECOND) == 15) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + sekMSG.replaceAll("%Zeit", String.valueOf(45))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 59
						&& cal.get(Calendar.SECOND) == 30) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + sekMSG.replaceAll("%Zeit", String.valueOf(30))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 59
						&& cal.get(Calendar.SECOND) == 40) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + sekMSG.replaceAll("%Zeit", String.valueOf(20))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 59
						&& cal.get(Calendar.SECOND) == 45) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + sekMSG.replaceAll("%Zeit", String.valueOf(15))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 59
						&& cal.get(Calendar.SECOND) == 50) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + sekMSG.replaceAll("%Zeit", String.valueOf(10))));
					restart = true;
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 59
						&& cal.get(Calendar.SECOND) == 55) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + sekMSG.replaceAll("%Zeit", String.valueOf(5))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 59
						&& cal.get(Calendar.SECOND) == 56) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + sekMSG.replaceAll("%Zeit", String.valueOf(4))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 59
						&& cal.get(Calendar.SECOND) == 57) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + sekMSG.replaceAll("%Zeit", String.valueOf(3))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 59
						&& cal.get(Calendar.SECOND) == 58) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + sekMSG.replaceAll("%Zeit", String.valueOf(2))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == 59
						&& cal.get(Calendar.SECOND) == 59) {
					ProxyServer.getInstance()
							.broadcast(new TextComponent(prefix + sekMSG.replaceAll("%Zeit", String.valueOf(1))));
				}
				if (cal.get(Calendar.HOUR_OF_DAY) == hour + 1 && cal.get(Calendar.MINUTE) == 0
						&& cal.get(Calendar.SECOND) == 0) {
					for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
						players.disconnect(new TextComponent(prefix + restartMSG));
					}
					ProxyServer.getInstance().stop();
				}
			}
		}, 1, 1, TimeUnit.SECONDS);
	}

	public void toggleWartung(boolean ein) throws IOException {
		if (ein) {
			ProxyServer.getInstance().broadcast(sendausgabe("Wartung.Ein"));
			for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
				if (!player.hasPermission("bs.wartung.exempt")) {
					player.disconnect((config.getString("Wartung.Kick.Line1").replaceAll("&", "§")) + "\n"
							+ (config.getString("Wartung.Kick.Line2").replaceAll("&", "§")) + "\n"
							+ (config.getString("Wartung.Kick.Line3").replaceAll("&", "§")) + "\n"
							+ (config.getString("Wartung.Kick.Line4").replaceAll("&", "§")));
				}
			}
			for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
				p.resetTabHeader();
				p.setTabHeader(
						new TextComponent(
								(config.getString("Wartung.MOTD.Line1") + "\n" + config.getString("Wartung.MOTD.Line2"))
										.replaceAll("&", "§")),
						new TextComponent(messages.getString("Login.TabFooter").replaceAll("&", "§")));
			}
			wartung = true;
		} else {
			for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
				p.resetTabHeader();
				p.setTabHeader(new TextComponent(messages.getString("Login.TabHeader").replaceAll("&", "§")),
						new TextComponent(messages.getString("Login.TabFooter").replaceAll("&", "§")));
			}
			wartung = false;
			ProxyServer.getInstance().broadcast(sendausgabe("Wartung.Aus"));
		}
		config.set("Wartung.aktiviert", wartung);
		saveFiles();
	}

	public void registerCommands() {
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanCMD(this, "ban"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanlistCMD(this, "banlist"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new BroadcastToggleCMD(this, "bctoggle"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this,
				new BroadcastToggleCMD(this, "broadcasttoggle"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new CheckCMD(this, "check"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new ClearChatCMD(this, "clearchat"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new ClearChatCMD(this, "cc"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new GlobalCMD(this, "global"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new JoinMeCMD(this, "joinme"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new KickCMD(this, "kick"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new LobbyCMD(this, "lobby"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new LobbyCMD(this, "l"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new LobbyCMD(this, "hub"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new LobbyCMD(this, "leave"));
		if (this.messages.getBoolean("MSG.aktiviert")) {
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new MSGCMD(this, "msg"));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new MSGCMD(this, "tell"));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new MSGCMD(this, "whisper"));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new MSGCMD(this, "w"));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new MSGToggleCMD(this, "msgtoggle"));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReplyCMD(this, "reply"));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReplyCMD(this, "r"));
		}
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new MuteCMD(this, "mute"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new NotifyToggleCMD(this, "notifytoggle"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new NotifyToggleCMD(this, "ntoggle"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new OnlineCMD(this, "online"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new PingCMD(this, "ping"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new PremiumCMD(this, "premium"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new PremiumCMD(this, "vip"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new PremiumPlusCMD(this, "premium+"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new PremiumPlusCMD(this, "premiumplus"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new PremiumPlusCMD(this, "vip+"));

		ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReportCMD(this, "report"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReportsCMD(this, "reports"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnbanCMD(this, "unban"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnmuteCMD(this, "unmute"));
		// ProxyServer.getInstance().getPluginManager().registerCommand(this, new
		// WartungCMD(this, "wartung"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new WhereAmICMD(this, "whereami"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new WhereIsCMD(this, "whereis"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new YouTuberCMD(this, "youtuber"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new YouTuberCMD(this, "youtube"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new YouTuberCMD(this, "yt"));
	}

	public void registerListener() {
		ProxyServer.getInstance().getPluginManager().registerListener(this, new ChatListener(this));
		ProxyServer.getInstance().getPluginManager().registerListener(this, new LoginListener(this));
		ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerDisconnectListener(this));
		// ProxyServer.getInstance().getPluginManager().registerListener(this, new
		// PluginMessageListener());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new PostLoginListener(this));
		ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxyPingListener(this));
		ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerConnectedListener(this));
		ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerConnectListener(this));
		ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerKickListener(this));
		ProxyServer.getInstance().getPluginManager().registerListener(this, new TabCompleteListener(this));
	}

	public boolean messageContainsBlackListedWords(String message) {
		for (String word : message.split(" ")) {
			for (String blacklist : wordblacklist) {
				word.toLowerCase();
				blacklist.toLowerCase();
				if (word.contains(blacklist)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean messageContainsServerIP(String message) {
		for (String word : message.split(" ")) {
			word.toLowerCase();
			if (word.contains(".com") || word.contains(".de") || word.contains(".net") || isValidURL(word)) {
				return true;
			}
		}
		return false;
	}

	public boolean isValidURL(String urlString) {
		try {
			new URL(urlString);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}