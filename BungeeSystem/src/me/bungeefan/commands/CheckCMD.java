package me.bungeefan.commands;

import java.sql.SQLException;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CheckCMD extends Command {

	private BungeeSystem instance;
	public ProxiedPlayer player;
	public String uuid;

	public CheckCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("bs.check") || cs.hasPermission("bs.admin")) {
			if (instance.sql != null && instance.sql.isConnected()) {
				if (args.length == 1) {
					try {
						if (BungeeCord.getInstance().getPlayer(args[0]) != null) {
							player = BungeeCord.getInstance().getPlayer(args[0]);
							uuid = player.getUniqueId().toString();
						} else {
							uuid = instance.sql.getUUID(args[0]);
							// uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
							// "$1-$2-$3-$4-$5");
							// UUID u = UUID.fromString(uuid);
							// player = BungeeCord.getInstance().getPlayerByOfflineUUID(u);
						}
					} catch (SQLException e) {
						e.printStackTrace();
						uuid = null;
					}
					try {
						if (instance.sql.isPlayerExisting(uuid) || instance.sql.isPlayerExisting2(uuid)) {
							String ip = instance.sql.getIP(uuid);
							String spieler = instance.sql.getPlayerName(uuid);
							String banoperator = instance.sql.getBy(uuid);
							String muteoperator = instance.sql.getBy2(uuid);
							String bangrund = instance.sql.getReason(uuid);
							String mutegrund = instance.sql.getReason2(uuid);
							long banend = instance.sql.getUntil(uuid);
							long muteend = instance.sql.getUntil2(uuid);
							String checkheader = (instance.messages.getString("Check.Header").replaceAll("%Spieler%",
									spieler)).replaceAll("&", "§");
							String checkuuid = (instance.messages.getString("Check.UUID").replaceAll("%UUID%",
									uuid.toString())).replaceAll("&", "§");
							String checkip = (instance.messages.getString("Check.IP").replaceAll("%IP%", ip))
									.replaceAll("&", "§");
							player.sendMessage(checkip);
							String checkban = (instance.messages.getString("Check.Ban").replaceAll("%OPERATOR%",
									banoperator)).replaceAll("&", "§");
							String checkmute = (instance.messages.getString("Check.Mute").replaceAll("%OPERATOR%",
									muteoperator)).replaceAll("&", "§");

							String checkbanreason = "";
							if (!bangrund.isEmpty()) {
								checkban = checkban.replaceAll("%BanDauer%", instance.getRemainingTime(banend));
								checkbanreason = (instance.messages.getString("Check.BanReason").replaceAll("%Grund%",
										bangrund)).replaceAll("&", "§");
							} else {
								checkban = "§aNicht gebannt!";
							}

							String checkmutereason = "";
							if (!mutegrund.isEmpty()) {
								checkmute = checkmute.replaceAll("%MuteDauer%", instance.getRemainingTime(muteend));
								checkmutereason = (instance.messages.getString("Check.MuteReason").replaceAll("%Grund%",
										mutegrund)).replaceAll("&", "§");
							} else {
								checkmute = "§aNicht gemutet!";
							}

							String checkmessage = checkheader + "\n" + checkuuid + "\n" + checkip + "\n" + checkban
									+ "\n" + checkbanreason + "\n" + checkmute + "\n" + checkmutereason;
							cs.sendMessage(checkmessage);
						} else {
							cs.sendMessage(instance.sendausgabe("Check.NotFound").replaceAll("%Spieler%", args[0]));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					cs.sendMessage(instance.sendausgabe("Check.Usage"));
				}
			} else {
				cs.sendMessage(instance.prefix + "§cMySQL Fehler, Diese Funktion ist deaktiviert!");
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
		}
	}
}
