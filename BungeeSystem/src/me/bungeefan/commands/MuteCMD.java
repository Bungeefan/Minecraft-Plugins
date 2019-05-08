package me.bungeefan.commands;

import java.sql.SQLException;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class MuteCMD extends Command {

	private BungeeSystem instance;
	public ProxiedPlayer player;
	public String uuid;
	public long dauer;
	public long restriction = -1;

	public MuteCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("bs.mute") || cs.hasPermission("bs.admin")) {
			if (instance.sql != null && instance.sql.isConnected()) {
				if (args.length >= 3) {
					String ip = null;
					if (BungeeCord.getInstance().getPlayer(args[0]) != null) {
						player = BungeeCord.getInstance().getPlayer(args[0]);
						ip = player.getAddress().getAddress().getHostAddress();
						uuid = player.getUniqueId().toString();
					} else {
						try {
							uuid = instance.sql.getUUID2(args[0]);
							// uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
							// "$1-$2-$3-$4-$5");
							// UUID u = UUID.fromString(uuid);
							// player = BungeeCord.getInstance().getPlayerByOfflineUUID(u);
						} catch (Exception e) {
							uuid = null;
						}
					}
					if (args[1].equals("-1")) {
						dauer = -1;
					} else {
						String zeit = args[1].trim().toLowerCase();
						dauer = instance.getDauer(zeit);
					}
					String grund = args[2];
					for (int i = 3; i < args.length; i++) {
						grund = grund + " " + args[i];
					}
					if (instance.hasMuteRestriction(cs) && !(cs instanceof ConsoleCommandSender)) {
						restriction = instance.getMuteRestriction(cs);
					}
					if (dauer <= restriction || restriction == -1) {
						if (player != null) {
							if ((instance.checkRang(cs, player))) {
								try {
									if (cs instanceof ProxiedPlayer) {
										ProxiedPlayer by = (ProxiedPlayer) cs;
										instance.sql.setBanned2(player, grund, by, System.currentTimeMillis(), dauer,
												ip);
									} else {
										instance.sql.setBanned2(player, grund, cs.getName(), System.currentTimeMillis(),
												dauer, ip);
									}
									cs.sendMessage(instance.sendausgabe("Mute.Erfolg").replaceAll("%Spieler%",
											player.getName()));
									instance.notify("Mute", uuid);
								} catch (SQLException e) {
									e.printStackTrace();
								}
							} else {
								cs.sendMessage(instance.sendausgabe("Mute.Exempt"));
							}
						} else if (uuid != null) {
							if ((instance.checkRang(cs, uuid))) {
								try {
									if (cs instanceof ProxiedPlayer) {
										ProxiedPlayer by = (ProxiedPlayer) cs;
										instance.sql.setBanned2(uuid, grund, by, System.currentTimeMillis(), dauer, ip);
									} else {
										instance.sql.setBanned2(uuid, grund, cs.getName(), System.currentTimeMillis(),
												dauer, ip);
									}
									cs.sendMessage(instance.sendausgabe("Mute.Erfolg").replaceAll("%Spieler%",
											instance.sql.getPlayerName(uuid)));
									instance.notify("Mute", uuid);
								} catch (SQLException e) {
									e.printStackTrace();
								}
							} else {
								cs.sendMessage(instance.sendausgabe("Mute.Exempt"));
							}
						} else {
							cs.sendMessage(instance.sendausgabe("Allgemein.Fehler"));
						}
					} else {
						String mutelimit = instance.getMuteRestriction2(cs);
						cs.sendMessage(instance.sendausgabe("Mute.Restriction").replaceAll("%Limit%", mutelimit));
					}
				} else {
					cs.sendMessage(instance.sendausgabe("Mute.Usage"));
				}
			} else {
				cs.sendMessage(instance.prefix + "§cMySQL Fehler, Diese Funktion ist deaktiviert!");
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
		}
	}
}
