package me.bungeefan.commands;

import java.sql.SQLException;
import java.util.UUID;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnmuteCMD extends Command {

	private BungeeSystem instance;
	public ProxiedPlayer player;
	public String uuid;

	public UnmuteCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("bs.unmute") || cs.hasPermission("bs.admin")) {
			if (instance.sql != null && instance.sql.isConnected()) {
				if (args.length == 1) {
					if (BungeeCord.getInstance().getPlayer(args[0]) != null) {
						player = BungeeCord.getInstance().getPlayer(args[0]);
						uuid = player.getUniqueId().toString();
					} else {
						try {
							uuid = instance.sql.getUUID2(args[0]);
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
							instance.sql.setUnBanned2(player);
							cs.sendMessage(instance.sendausgabe(("UnMute.Erfolg")).replaceAll("%Spieler%", args[0]));
						} catch (SQLException e) {
							cs.sendMessage(instance.sendausgabe(("UnMute.NotErfolg")).replaceAll("%Spieler%", args[0]));
						}
					} else if (uuid != null) {
						try {
							instance.sql.setUnBanned2(uuid);
							cs.sendMessage(instance.sendausgabe(("UnMute.Erfolg")).replaceAll("%Spieler%", args[0]));
						} catch (SQLException e) {
							cs.sendMessage(instance.sendausgabe(("UnMute.NotErfolg")).replaceAll("%Spieler%", args[0]));
						}
					} else {
						cs.sendMessage(instance.sendausgabe(("Allgemein.Fehler")));
					}
				} else {
					cs.sendMessage(instance.sendausgabe(("UnMute.Usage")));
				}
			} else {
				cs.sendMessage(instance.prefix + "§cMySQL Fehler, Diese Funktion ist deaktiviert!");
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
		}
	}
}