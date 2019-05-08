package me.bungeefan.commands;

import java.io.IOException;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WartungCMD extends Command {

	private BungeeSystem instance;

	public WartungCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("bs.wartung") || cs.hasPermission("bs.admin")) {
			if (args.length == 0) {
				if (instance.config.getBoolean("Wartung.aktiviert")) {
					cs.sendMessage(instance.config.getString("Allgemein.Prefix").replaceAll("&", "§")
							+ "§cDer Wartungsmodus ist aktiviert!");
				} else {
					cs.sendMessage(instance.config.getString("Allgemein.Prefix").replaceAll("&", "§")
							+ "§cDer Wartungsmodus ist deaktiviert!");
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("aus")) {
					try {
						instance.toggleWartung(false);
					} catch (IOException e) {
						cs.sendMessage(instance.sendausgabe("Allgemein.ConfigFehler"));
					}
				} else if (args[0].equalsIgnoreCase("an")) {
					try {
						instance.toggleWartung(true);
					} catch (IOException e) {
						cs.sendMessage(instance.sendausgabe("Allgemein.ConfigFehler"));
					}
				}
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
		}
	}
}