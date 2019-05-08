package me.bungeefan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.bungeefan.commands.BanCMD;
import me.bungeefan.commands.BanlistCMD;
import me.bungeefan.commands.CheckCMD;
import me.bungeefan.commands.KickCMD;
import me.bungeefan.commands.MuteCMD;
import me.bungeefan.commands.UnbanCMD;
import me.bungeefan.commands.UnmuteCMD;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class ClazeBan extends Plugin implements Listener {

	public Configuration config;
	public Configuration messages;
	public Configuration player;
	public Configuration mysql;
	public List<String> layout = new ArrayList<String>();
	public List<String> notifymsg = new ArrayList<String>();
	private static ClazeBan instance;

	public static ClazeBan get() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		loadDefaultFiles();
		try {
			loadFiles();
		} catch (IOException e) {
			System.out.println("§cConfig konnte nicht geladen werden!");
			getProxy().getPluginManager().getPlugin("ClazeBan").onDisable();
		}
		getProxy().getPluginManager().registerCommand(this, new BanCMD());
		getProxy().getPluginManager().registerCommand(this, new BanlistCMD());
		getProxy().getPluginManager().registerCommand(this, new CheckCMD());
		getProxy().getPluginManager().registerCommand(this, new KickCMD());
		getProxy().getPluginManager().registerCommand(this, new MuteCMD());
		getProxy().getPluginManager().registerCommand(this, new UnbanCMD());
		getProxy().getPluginManager().registerCommand(this, new UnmuteCMD());
		BungeeCord.getInstance().getPluginManager().registerListener(this, this);
	}

	@EventHandler
	public void onLogin(LoginEvent e) {
		String uuid = e.getConnection().getUniqueId().toString();
		if (uuid == null) {
			e.setCancelled(true);
			e.setCancelReason(new TextComponent("[ClazeBan] Deine UUID konnte nicht abgerufen werden!"));
		}
		String ip = e.getConnection().getAddress().getAddress().getHostAddress().replace(".", "/");
		if (ip == null) {
			e.setCancelled(true);
			e.setCancelReason(new TextComponent("[ClazeBan] Deine IP konnte nicht abgerufen werden!"));
		}
		if (this.player.getString("Spieler." + uuid + ".Spieler") != "") {
			System.out.println("C");
			if (isBanned(uuid)) {
				this.player.set("Spieler." + uuid + ".IP", ip);
				this.player.set("IPs." + ip + ".UUID", uuid);
				e.setCancelled(true);
				e.setCancelReason(banMessage(uuid));
			}
		} else if (this.player.getString("IPs." + ip + ".UUID") != "") {
			System.out.println("A");
			uuid = this.player.getString("IPs." + ip + ".UUID");
			if (isBanned(uuid)) {
				System.out.println("B");
				this.player.set("Spieler." + uuid + ".IP", ip);
				this.player.set("IPs." + ip + ".UUID", uuid);
				e.setCancelled(true);
				e.setCancelReason(banMessage(uuid));
			}
		}
	}

	public boolean isBanned(String uuid) {
		if (this.player.getLong("Spieler." + uuid + ".BanAnfang") == 0) {
			return false;
		} else {
			if (isEnd(uuid)) {
				this.player.set("Spieler." + uuid + ".BanDauer", 0);
				this.player.set("Spieler." + uuid + ".BanAnfang", 0);
				String ip = this.player.getString("Spieler." + uuid + ".ip").replaceAll("-", ".");
				this.player.set("IPs." + ip, null);
				return false;
			} else {
				return true;
			}
		}
	}

	public boolean isEnd(String uuid) {
		if (this.player.getLong("Spieler." + uuid + ".BanDauer") != -1) {
			long dauer = this.player.getLong("Spieler." + uuid + ".BanDauer");
			long anfang = this.player.getLong("Spieler." + uuid + ".BanAnfang");
			long current = System.currentTimeMillis();
			long ende = anfang + dauer;
			if (ende <= current) {
				return true;
			}
		}
		return false;
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

	/*
	 * public String getName(String uuid) { String name = ""; try { name =
	 * getName_fromURL(config.getString("Name-Fetcher.MOJANG-API.URL"), uuid,
	 * (config.getString("Name-Fetcher.MOJANG-API.Key"))); } catch (IOException
	 * e) { System.out.println("Error -> " + e.getMessage());
	 * System.out.println("!! Fehler beim abrufen des Namen von " + uuid);
	 * System.out.println("!! MOJANG-API konnnte unter" +
	 * config.getString("Name-Fetcher.MOJANG-API.URL") +
	 * "nicht erreicht werden!"); } if (name == null) {
	 * System.out.println("BackUP-API wird verwendet!"); try { name =
	 * getName_fromURL(config.getString("Name-Fetcher.BackUp-API.URL"), uuid,
	 * (config.getString("Name-Fetcher.BackUp-API.Key"))); } catch (IOException
	 * e) { System.out.println("!! Fehler beim abrufen des Namen von " + uuid);
	 * System.out.println("!! BackUp-API konnnte unter" +
	 * config.getString("Name-Fetcher.BackUp-API.URL") +
	 * "nicht erreicht werden!"); } } if (name == null) {
	 * System.out.println("!! !! Name von " + uuid +
	 * " konnte nicht abgerufen werden!");
	 * System.out.println("!! Prüfe bitte die UUID!"); } return uuid; }
	 * 
	 * private String getName_fromURL(String url, String uuid, String key)
	 * throws IOException { try { HttpURLConnection request =
	 * (HttpURLConnection) new URL(url.replaceAll("[UUID]",
	 * uuid)).openConnection(); request.connect(); JsonParser jp = new
	 * JsonParser(); JsonObject json = (JsonObject) jp.parse(new
	 * InputStreamReader(request.getInputStream())); return
	 * json.get(key).toString().replaceAll("\"", ""); } catch (Exception exc) {
	 * } return null; }
	 */

	public String sendausgabe(String message) {
		return (this.messages.getString("Prefix") + (this.messages.getString(message))).replaceAll("&", "§");
	}

	public String banMessage(String uuid) {
		layout = messages.getStringList("Ban.Layout");
		String operator = this.player.getString("Spieler." + uuid + ".BanOperator");
		String grund = this.player.getString("Spieler." + uuid + ".BanGrund");
		long anfang = this.player.getLong("Spieler." + uuid + ".BanAnfang");
		long dauer = this.player.getLong("Spieler." + uuid + ".BanDauer");
		String remainingTime;
		long current = System.currentTimeMillis();
		long end = anfang + dauer;
		long difference = end - current;
		difference /= 1000;
		if (dauer == -1) {
			remainingTime = "§4§lPermanent";
		} else {
			int minuten = 0;
			int stunden = 0;
			int tage = 0;
			int monate = 0;
			while (difference >= 60L) {
				difference -= 60L;
				minuten++;
			}
			while (minuten >= 60) {
				minuten -= 60;
				stunden++;
			}
			while (stunden >= 24) {
				stunden -= 24;
				tage++;
			}
			while (tage >= 30) {
				tage -= 30;
				monate++;
			}
			remainingTime = "§e" + monate + " Monat(e) " + tage + " Tag(e), " + stunden + " Stunde(n), " + minuten
					+ " Minute(n) ";
		}
		String banmessage = "";
		for (int i = 0; i < layout.size(); i++) {
			banmessage = banmessage + ((((layout.get(i).replaceAll("&", "§")).replaceAll("%Dauer%", remainingTime))
					.replaceAll("%Grund%", grund)).replaceAll("%OPERATOR%", operator)) + "\n";
		}
		return banmessage;
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
		String spieler = this.player.getString("Spieler." + uuid + ".Spieler");
		String operator = this.player.getString("Spieler." + uuid + ".Operator");
		String grund = this.player.getString("Spieler." + uuid + ".Grund");
		notifymsg = this.messages.getStringList(type + ".Notification");
		String notifymessage = "";
		for (int i = 0; i < notifymsg.size(); i++) {
			notifymessage = notifymessage + ((((notifymsg.get(i).replaceAll("&", "§")).replaceAll("%Spieler%", spieler))
					.replaceAll("%OPERATOR%", operator)).replaceAll("%Grund%", grund)) + "\n";
		}
		Collection<? extends ProxiedPlayer> onlineplayer = BungeeCord.getInstance().getPlayers();
		if (onlineplayer.size() != 0) {
			for (int i = 0; i < onlineplayer.size(); i++) {
				ProxiedPlayer player = Iterables.get(onlineplayer, i);
				if (player.hasPermission("cb." + type + ".notify"))
					player.sendMessage(notifymessage);
			}
		}
	}

	public void loadDefaultFiles() {
		if (!getDataFolder().exists())
			getDataFolder().mkdir();

		File configfile = new File(getDataFolder(), "config.yml");
		File messagesfile = new File(getDataFolder(), "messages.yml");
		File playerfile = new File(getDataFolder(), "player.yml");
		File mysqlfile = new File(getDataFolder(), "MySQL.yml");

		if (!configfile.exists()) {
			try (InputStream in = getResourceAsStream("config.yml")) {
				Files.copy(in, configfile.toPath());
			} catch (IOException e) {
				System.out.println("Fehler");
			}
		}
		if (!messagesfile.exists()) {
			try (InputStream in = getResourceAsStream("messages.yml")) {
				Files.copy(in, messagesfile.toPath());
			} catch (IOException e) {
				System.out.println("Fehler");
			}
		}
		if (!playerfile.exists()) {
			try {
				playerfile.createNewFile();
			} catch (IOException e) {
				System.out.println("Fehler");
			}
		}
		if (!mysqlfile.exists()) {
			try (InputStream in = getResourceAsStream("MySQL.yml")) {
				Files.copy(in, mysqlfile.toPath());
			} catch (IOException e) {
				System.out.println("Fehler");
			}
		}
	}

	public void loadFiles() throws IOException {
		config = ConfigurationProvider.getProvider(YamlConfiguration.class)
				.load(new File(getDataFolder(), "config.yml"));
		messages = ConfigurationProvider.getProvider(YamlConfiguration.class)
				.load(new File(getDataFolder(), "messages.yml"));
		player = ConfigurationProvider.getProvider(YamlConfiguration.class)
				.load(new File(getDataFolder(), "player.yml"));
		mysql = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "MySQL.yml"));
	}

	public void saveFiles() throws IOException {
		ConfigurationProvider.getProvider(YamlConfiguration.class).save(config,
				new File(getDataFolder(), "config.yml"));
		ConfigurationProvider.getProvider(YamlConfiguration.class).save(messages,
				new File(getDataFolder(), "messages.yml"));
		ConfigurationProvider.getProvider(YamlConfiguration.class).save(player,
				new File(getDataFolder(), "player.yml"));
		ConfigurationProvider.getProvider(YamlConfiguration.class).save(mysql, new File(getDataFolder(), "MySQL.yml"));
	}
}
