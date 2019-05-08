package me.bungeefan.commands;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanlistCMD extends Command {

	private BungeeSystem instance;
	public ProxiedPlayer player;
	public String uuid;

	public BanlistCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("bs.banlist") || cs.hasPermission("bs.admin")) {
			if (instance.sql != null && instance.sql.isConnected()) {
				if (args.length == 0) {

				} else if (args.length <= 1) {

				} else {
					cs.sendMessage(instance.sendausgabe("Banlist.Usage"));
				}
			} else {
				cs.sendMessage(instance.prefix + "§cMySQL Fehler, Diese Funktion ist deaktiviert!");
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
		}
	}
}
