package me.bungeefan.listener;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import me.bungeefan.BungeeSystem;
import me.bungeefan.ChatLogger;
import me.bungeefan.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

	private BungeeSystem instance;

	public ChatListener(BungeeSystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onChat(ChatEvent e) {
		ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		String ip = p.getAddress().getAddress().getHostAddress();
		String uuid = p.getUniqueId().toString();
		String message = e.getMessage();
		MySQL sql = instance.sql;
		String prefix = instance.prefix;
		if (sql != null && sql.isConnected()) {
			if (!p.hasPermission("bs.chatlistener.bypass") && !p.hasPermission("bs.admin")) {
				if (!e.isCommand()) {
					// ProxyServer.getInstance().getScheduler().runAsync(instance, new Runnable() {
					//
					// @Override
					// public void run() {
					try {
						String uuid2;
						if (sql.isPlayerExisting2(p)) {
							if (sql.isBanned2(p)) {
								e.setCancelled(true);
								p.sendMessage(instance.muteMessage(uuid));
							}
						} else {
							sql.registerPlayer2(p);
						}
						if (sql.isIPExisting2(ip) && !e.isCancelled()) {
							uuid2 = sql.getUUIDOfIP2(ip);
							if (sql.isBanned2(uuid2)) {
								e.setCancelled(true);
								p.sendMessage(instance.muteMessage(uuid2));
							}
						}
					} catch (SQLException | NullPointerException exp) {
						exp.printStackTrace();
					}
					if (!e.isCancelled()) {
						if (!instance.messagecd.contains(p)) {
							instance.messagecd.add(p);
							ProxyServer.getInstance().getScheduler().schedule(instance, new Runnable() {

								@Override
								public void run() {
									instance.messagecd.remove(p);
								}
							}, instance.config.getLong("MessageDelay"), TimeUnit.SECONDS);
						} else {
							e.setCancelled(true);
							p.sendMessage(instance.prefix + "§cBitte schreibe etwas langsamer.");
						}
						if (instance.messages.getBoolean("BlockedWords.aktiviert")) {
							if (instance.messageContainsBlackListedWords(message)) {
								e.setCancelled(true);
								p.sendMessage(prefix + "§cBitte achte auf deine Wortwahl.");
							}
						}
						if (instance.messageContainsServerIP(message)) {
							e.setCancelled(true);
						}
						// ChatLogger logger =
						// instance.logger.get(p.getUniqueId().toString());
						// logger.log(p.getServer().getInfo().getName(), message);
					}
				} else {
					if (instance.messages.getBoolean("BlockedCMDs.aktiviert")) {
						for (String cmd : instance.blockedCMDs) {
							String[] splitmsg = message.split(" ");
							if ((splitmsg[0].replaceAll("/", "")).equalsIgnoreCase(cmd)) {
								e.setCancelled(true);
								if (instance.messages.getBoolean("BlockedCMDs.Ersatz.aktiviert")) {
									p.sendMessage(instance.sendausgabe("BlockedCMDs.Ersatz.MSG"));
								}
							}
						}
					}
					// });
					// }
				}
			}
		} else {
			BungeeCord.getInstance().getConsole()
					.sendMessage(instance.prefix + "§cMySQL Fehler, Diese Funktion ist deaktiviert!");
		}
	}
}