package me.bungeefan.commands;

import java.sql.SQLException;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class BanCMD extends Command {

	private BungeeSystem instance;
	public ProxiedPlayer player;
	public String uuid;
	public long dauer;
	public long restriction = -1;

	public BanCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("bs.ban") || cs.hasPermission("bs.admin")) {
			if (instance.sql != null || !instance.sql.isConnected()) {
				if (args.length >= 3) {
					String ip = null;
					if (BungeeCord.getInstance().getPlayer(args[0]) != null) {
						player = BungeeCord.getInstance().getPlayer(args[0]);
						ip = player.getAddress().getAddress().getHostAddress();
						uuid = player.getUniqueId().toString();
					} else {
						try {
							uuid = instance.sql.getUUID(args[0]);
							// uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
							// "$1-$2-$3-$4-$5");
							// UUID u = UUID.fromString(uuid);
							// player = BungeeCord.getInstance().getPlayerByOfflineUUID(u);
						} catch (Exception e) {
							e.printStackTrace();
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
					if (instance.hasBanRestriction(cs) && !cs.getName().equalsIgnoreCase("CONSOLE")) {
						restriction = instance.getBanRestriction(cs);
					}
					if (dauer <= restriction || restriction == -1) {
						if (player != null) {
							if (instance.checkRang(cs, player)) {
								try {
									if (cs instanceof ProxiedPlayer) {
										ProxiedPlayer by = (ProxiedPlayer) cs;
										instance.sql.setBanned(player, grund, by, System.currentTimeMillis(), dauer,
												ip);
									} else {
										instance.sql.setBanned(player, grund, cs.getName(), System.currentTimeMillis(),
												dauer, ip);
									}
									player.disconnect(instance.banMessage(uuid));
									cs.sendMessage(instance.sendausgabe("Ban.Erfolg").replaceAll("%Spieler%",
											player.getName()));
									instance.kickAllIP(ip);
									instance.notify("Ban", uuid);
								} catch (SQLException e) {
									e.printStackTrace();
								}
							} else {
								cs.sendMessage(instance.sendausgabe("Ban.Exempt"));
							}
						} else if (uuid != null) {
							if ((instance.checkRang(cs, uuid))) {
								try {
									if (cs instanceof ProxiedPlayer) {
										ProxiedPlayer by = (ProxiedPlayer) cs;
										instance.sql.setBanned(uuid, grund, by, System.currentTimeMillis(), dauer, ip);
									} else {
										instance.sql.setBanned(uuid, grund, cs.getName(), System.currentTimeMillis(),
												dauer, ip);
									}
									cs.sendMessage(instance.sendausgabe("Ban.Erfolg").replaceAll("%Spieler%",
											instance.sql.getPlayerName(uuid)));
									instance.kickAllIP(ip);
									instance.notify("Ban", uuid);
								} catch (SQLException | NullPointerException e) {
									e.printStackTrace();
								}
							} else {
								cs.sendMessage(instance.sendausgabe("Ban.Exempt"));
							}
						} else {
							cs.sendMessage(instance.sendausgabe("Allgemein.Fehler"));
						}
					} else {
						String banlimit = instance.getBanRestriction2(cs);
						cs.sendMessage(instance.sendausgabe("Ban.Restriction").replaceAll("%Limit%", banlimit));
					}
				} else {
					cs.sendMessage(instance.sendausgabe("Ban.Usage"));
				}
			} else {
				cs.sendMessage(instance.prefix + "§4MySQL Fehler, Diese Funktion ist deaktiviert!");
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
		}
	}
}
