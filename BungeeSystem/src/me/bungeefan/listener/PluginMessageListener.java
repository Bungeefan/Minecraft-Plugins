package me.bungeefan.listener;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import me.bungeefan.BungeeSystem;
import me.bungeefan.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageListener implements Listener {

	private BungeeSystem instance;

	public PluginMessageListener(BungeeSystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onPluginMessage(PluginMessageEvent e) {
		MySQL sql = instance.sql;
		if (sql != null && sql.isConnected()) {
			if (e.getTag().equalsIgnoreCase("BungeeCord")) {
				ByteArrayDataInput input = ByteStreams.newDataInput(e.getData());
				String command = input.readUTF();

				if (!command.equalsIgnoreCase("connect")) {
					e.setCancelled(true);
				}
			}

			if (e.getTag().equalsIgnoreCase("AntiKillaura")) {
				ByteArrayDataInput input = ByteStreams.newDataInput(e.getData());
				String command = input.readUTF();

				if (command.equalsIgnoreCase("reportKillaura")) {
					String name = input.readUTF();
					ProxiedPlayer p = ProxyServer.getInstance().getPlayer(name);
					if (!p.hasPermission("bs.instance.administrator")) {
						for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
							if (players.hasPermission("bs.instance.moderator")
									| players.hasPermission("bs.instance.srmoderator")
									| players.hasPermission("bs.instance.administrator")) {
								TextComponent server = new TextComponent(p.getServer().getInfo().getName());
								server.setColor(ChatColor.YELLOW);
								server.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
										new ComponentBuilder("§8\u00BB §aZu ihm springen").create()));
								server.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
										"/server " + p.getServer().getInfo().getName()));
								players.sendMessage(
										"§8§l §c §8 §8§m--------------------§r§8 §cReport §8§m--------------------");
								players.sendMessage("§8§l §c §8 §3Spieler §8\u00BB §e" + p.getDisplayName());
								players.sendMessage("§8§l §c §8 §3Grund §8\u00BB §eHacking");
								players.sendMessage(
										"§8§l §c §8 §3Reportet von §8\u00BB §eAutomatisch §8| §eAntiKillaura");
								players.sendMessage("§8§l §c §8 §3Server §8\u00BB §e");
								players.sendMessage("§8§l §c §8 §8§m------------------------------------------------");
							}
						}
					}
				}
				if (command.equalsIgnoreCase("setBannedKillaura")) {
					ProxyServer.getInstance().getScheduler().runAsync(instance, new Runnable() {

						@Override
						public void run() {
							String name = input.readUTF();
							ProxiedPlayer p = ProxyServer.getInstance().getPlayer(name);
							if (!p.hasPermission("bs.instance.administrator")) {
								String[] ip = p.getAddress().toString().split(":");
								String subip = ip[0];
								long time = 0;
								try {
									long time2 = Long.valueOf(30);
									time = System.currentTimeMillis() + time2 * 1000 * 60 * 60 * 24;
								} catch (NumberFormatException ex) {
									ex.printStackTrace();
								}
								try {
									if (sql.isPlayerExisting(p)) {
										if (sql.getUntil(p) != -1 && System.currentTimeMillis() - sql.getUntil(p) > 0) {
											sql.setUnBanned(p);
										} else {
											System.out.println(instance.prefix + "§cDer Spieler §e" + p.getName()
													+ " §cist schon gebannt.");
											return;
										}
									}
									p.disconnect(
											"§cDu wurdest für §e30 Tage §cvom Netzwerk gebannt.\n§3Grund §8\u00BB §eHacking"
													// + "\n§3Gebannt von §8\u00BB §eAutomatisch §8| §eAntiKillaura"
													+ "\n\n§aDu kannst auf unserer §eHomepage §aeinen §eEntbannungsantrag §astellen.");
									sql.registerPlayer(p);
									sql.setBanned(p, " Hacking", "Automatisch §8| §eAntiKillaura",
											System.currentTimeMillis(), time, subip);
									for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
										if (players.hasPermission("bs.instance.developer")
												| players.hasPermission("bs.instance.moderator")
												| players.hasPermission("bs.instance.srmoderator")
												| players.hasPermission("bs.instance.administrator")) {
											if (!instance.notifytoggle.contains(players)) {
												players.sendMessage(
														"§8§l §c §8 §8§m----------------------§r§8 §cBan §8§m---------------------");
												players.sendMessage("§8§l §c §8 §3Spieler §8\u00BB §e" + p.getName());
												players.sendMessage("§8§l §c §8 §3Grund §8\u00BB §eHacking");
												players.sendMessage(
														"§8§l §c §8 §3Gebannt von §8\u00BB §eAutomatisch §8| §eAntiKillaura");
												players.sendMessage("§8§l §c §8 §3Gebannte Zeit §8\u00BB §e30 Tage");
												players.sendMessage(
														"§8§l §c §8 §8§m------------------------------------------------");
											}
										}
									}

								} catch (SQLException e) {

								}
							}
						}
					});
				}

			}

			if (e.getTag().equalsIgnoreCase("Maintenance")) {
				ByteArrayDataInput input = ByteStreams.newDataInput(e.getData());
				String command = input.readUTF();

				if (command.equalsIgnoreCase("setMaintenance")) {
					String gameType = input.readUTF();
					/*
					 * if (!instance.maintenances.contains(gameType)) {
					 * instance.maintenances.add(gameType); } else {
					 * instance.maintenances.remove(gameType); }
					 */
				}
			}
		} else {
			BungeeCord.getInstance().getConsole()
					.sendMessage(instance.prefix + "§cMySQL Fehler, Diese Funktion ist deaktiviert!");
		}
	}
}