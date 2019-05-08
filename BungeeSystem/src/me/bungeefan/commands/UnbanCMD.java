package me.bungeefan.commands;

import java.sql.SQLException;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnbanCMD extends Command {

	private BungeeSystem instance;
	public ProxiedPlayer player;
	public String uuid;

	public UnbanCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("bs.unban") || cs.hasPermission("bs.admin")) {
			if (instance.sql != null && instance.sql.isConnected()) {
				if (args.length == 1) {
					if (BungeeCord.getInstance().getPlayer(args[0]) != null) {
						player = BungeeCord.getInstance().getPlayer(args[0]);
						uuid = player.getUniqueId().toString();
					} else {
						try {
							uuid = instance.sql.getUUID(args[0]);
						} catch (SQLException e) {
							uuid = null;
						}
						/*
						 * uuid = instance.getUUID(args[0]); uuid =
						 * uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
						 * "$1-$2-$3-$4-$5"); UUID u = UUID.fromString(uuid); player =
						 * BungeeCord.getInstance().getPlayerByOfflineUUID(u);
						 */
					}
					if (player != null) {
						try {
							instance.sql.setUnBanned(player);
							cs.sendMessage(instance.sendausgabe(("UnBan.Erfolg")).replaceAll("%Spieler%", args[0]));
						} catch (SQLException e) {
							cs.sendMessage(instance.sendausgabe(("UnBan.NotErfolg")).replaceAll("%Spieler%", args[0]));
						}
					} else if (uuid != null) {
						try {
							instance.sql.setUnBanned(uuid);
							cs.sendMessage(instance.sendausgabe(("UnBan.Erfolg")).replaceAll("%Spieler%", args[0]));
						} catch (SQLException e) {
							cs.sendMessage(instance.sendausgabe(("UnBan.NotErfolg")).replaceAll("%Spieler%", args[0]));
						}
					} else {
						cs.sendMessage(instance.sendausgabe(("Allgemein.Fehler")));
					}
				} else {
					cs.sendMessage(instance.sendausgabe(("UnBan.Usage")));
				}
			} else {
				cs.sendMessage(instance.prefix + "§cMySQL Fehler, Diese Funktion ist deaktiviert!");
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
		}
	}
}
