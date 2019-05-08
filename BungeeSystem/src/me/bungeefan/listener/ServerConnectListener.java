package me.bungeefan.listener;

import java.sql.SQLException;

import me.bungeefan.BungeeSystem;
import me.bungeefan.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectListener implements Listener {

	private BungeeSystem instance;

	public ServerConnectListener(BungeeSystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onServerConnect(ServerConnectEvent e) {
		String lobby = instance.messages.getString("Allgemein.LobbyServer");
		ProxiedPlayer p = (ProxiedPlayer) e.getPlayer();
		MySQL sql = instance.sql;
		if (sql != null && sql.isConnected()) {
			try {
				sql.setRang(p.getUniqueId().toString(), instance.getRang(p.getPermissions()));
				sql.setRang2(p.getUniqueId().toString(), instance.getRang(p.getPermissions()));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			if (instance.wartung && !p.hasPermission("bs.wartung.exempt")) {
				e.setCancelled(true);
				p.disconnect(((instance.config.getString("Wartung.Kick.Line1")) + "\n"
						+ (instance.config.getString("Wartung.Kick.Line2")) + "\n"
						+ (instance.config.getString("Wartung.Kick.Line3")) + "\n"
						+ (instance.config.getString("Wartung.Kick.Line4"))).replaceAll("&", "§"));
			}
			if (instance.restart) {
				e.setCancelled(true);
				p.disconnect(instance.messages.getString("Allgemein.RestartMSG"));
			}
			if (e.getTarget().getName().contains(lobby)) {
				if (instance.wartung) {
					p.resetTabHeader();
					p.setTabHeader(
							new TextComponent((instance.config.getString("Wartung.MOTD.Line1") + "\n"
									+ instance.config.getString("Wartung.MOTD.Line2")).replaceAll("&", "§")),
							new TextComponent(instance.messages.getString("Login.TabFooter").replaceAll("&", "§")));
				}
				if (instance.messages.getBoolean("Login.TabEnabled") && !instance.wartung) {
					p.resetTabHeader();
					p.setTabHeader(
							new TextComponent(instance.messages.getString("Login.TabHeader").replaceAll("&", "§")),
							new TextComponent(instance.messages.getString("Login.TabFooter").replaceAll("&", "§")));
				}
			}
			// p.sendMessage("LightningPvP.de");
			// p.sendMessage("SevenPvP.net");
			// p.sendMessage("Riventus.net");

			if (p.hasPermission("bs.notify")) {
				if (!instance.reports.isEmpty()) {
					TextComponent all = new TextComponent("§8§l §c §8 ");
					TextComponent list = new TextComponent("§a§nAlle anzeigen");
					list.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder("§8\u00BB §3Alle §cReports §3anzeigen").create()));
					list.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports"));
					all.addExtra(list);

					p.sendMessage("");
					p.sendMessage("§8§l §c §8 §8§m--------------------§r§8 §cReport§8§m--------------------");
					String amount = "";
					if (instance.reports.size() < 4) {
						amount = "§a" + instance.reports.size();
					} else if (instance.reports.size() < 7) {
						amount = "§e" + instance.reports.size();
					} else if (instance.reports.size() < 10) {
						amount = "§c" + instance.reports.size();
					} else if (instance.reports.size() < 13) {
						amount = "§4" + instance.reports.size();
					} else if (instance.reports.size() >= 13) {
						amount = "§4§l" + instance.reports.size();
					}
					p.sendMessage("§8§l §c §8 " + "§3Unbearbeitete §cReports §8\u00BB " + amount);
					p.sendMessage(all);
					p.sendMessage("§8§l §c §8§m------------------------------------------------");
				}
			}

			// if (!BungeeSystem.logger.containsKey(p.getUniqueId())) {
			// BungeeSystem.logger.put(p.getUniqueId(), new ChatLogger(p.getUniqueId()));
			// }
		} else {
			BungeeCord.getInstance().getConsole()
					.sendMessage(instance.prefix + "§cMySQL Fehler, Diese Funktion ist deaktiviert!");
		}
	}
}