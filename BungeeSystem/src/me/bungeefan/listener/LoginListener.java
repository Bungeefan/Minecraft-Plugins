package me.bungeefan.listener;

import me.bungeefan.BungeeSystem;
import me.bungeefan.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {

	private BungeeSystem instance;

	public LoginListener(BungeeSystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onLogin(LoginEvent e) {
		e.registerIntent(instance);
		String uuid = e.getConnection().getUniqueId().toString();
		String name = e.getConnection().getName();
		String ip = e.getConnection().getAddress().getAddress().getHostAddress();
		MySQL sql = instance.sql;
		if (sql != null && instance.sql.isConnected()) {
			ProxyServer.getInstance().getScheduler().runAsync(instance, new Runnable() {

				@Override
				public void run() {
					try {
						String uuid2;
						if (sql.isPlayerExisting(uuid)) {
							sql.setPlayerName(name, uuid);
							if (sql.getIP(uuid) == null) {
								sql.setIP(uuid, ip);
							}
							if (sql.isBanned(uuid)) {
								e.setCancelled(true);
								e.setCancelReason(instance.banMessage(uuid));
							}
						} else {
							sql.registerPlayer(name, uuid, ip);
						}
						if (sql.isPlayerExisting2(uuid)) {
							sql.setPlayerName2(name, uuid);
							if (sql.getIP2(uuid) == null) {
								sql.setIP2(uuid, ip);
							}
						} else {
							sql.registerPlayer2(name, uuid, ip);
						}
						if (sql.isIPExisting(ip) && !e.isCancelled()) {
							uuid2 = sql.getUUIDOfIP(ip);
							if (sql.isBanned(uuid2)) {
								e.setCancelled(true);
								e.setCancelReason(instance.banMessage(uuid2));
							}
						}

						String group = sql.getPlayerGroup(uuid);

						if (!e.isCancelled()) {
							if (!instance.wartung) {
								int fake = (int) (ProxyServer.getInstance().getOnlineCount()
										* (double) (instance.config.getDouble("Fake") / 100.0));

								if (ProxyServer.getInstance().getPlayers().size() + fake >= instance.config
										.getInt("MaxPlayers")) {
									if (!group.equalsIgnoreCase("Premium") && !group.equalsIgnoreCase("Premium+")
											&& !group.equalsIgnoreCase("YouTuber") && !group.equalsIgnoreCase("Builder")
											&& !group.equalsIgnoreCase("Developer")
											&& !group.equalsIgnoreCase("Moderator")
											&& !group.equalsIgnoreCase("SrModerator")
											&& !group.equalsIgnoreCase("Administrator")) {
										e.setCancelled(true);
										int maxPlayers = instance.config.getInt("MaxPlayers");
										if (maxPlayers != 1) {
											e.setCancelReason();
										} else {
											e.setCancelReason(instance.messages.getString("Login.LimitMSG")
													.replaceAll("%MaxPlayers%", String.valueOf(maxPlayers)));
										}
									}
								}
							}
						}
					} catch (Exception exp) {
						exp.printStackTrace();
					} finally {
						e.completeIntent(instance);
					}
				}
			});
		} else {
			BungeeCord.getInstance().getConsole()
					.sendMessage(instance.prefix + "§cMySQL Fehler, Diese Funktion ist deaktiviert!");
			e.completeIntent(instance);
		}
	}
}