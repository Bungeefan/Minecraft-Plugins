package me.bungeefan.commands;

import java.io.IOException;

import me.bungeefan.BungeeWartung;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class WartungCMD extends Command {

	public WartungCMD(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		boolean wartung = BungeeWartung.get().config.getBoolean("BungeeWartung.Status");
		if (cs.hasPermission("bungeewartung.use")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("aus")) {
					try {
						BungeeWartung.get().toggleWartung(false);
					} catch (IOException e) {
						cs.sendMessage("§4Config Fehler");
					}
				} else if (args[0].equalsIgnoreCase("an")) {
					try {
						BungeeWartung.get().toggleWartung(true);
					} catch (IOException e) {
						cs.sendMessage("§4Config Fehler");
					}
				}
				if (args[0].equalsIgnoreCase("reload")) {
					String config = BungeeWartung.get().config.getString("BungeeWartung.Prefix").replaceAll("&", "§");
					cs.sendMessage(config + "§cDie Config wird reloaded.");
					try {
						BungeeWartung.get().loadFiles();
						cs.sendMessage(config + "§aDie Config wurde erfolgreich reloaded.");
					} catch (IOException e) {
						cs.sendMessage(config + "§cDie Config wurde nicht erfolgreich reloaded.");
					}
					BungeeWartung.get().reload = true;
				}
				if (args[0].equalsIgnoreCase("help")) {
					cs.sendMessage((BungeeWartung.get().config.getString("BungeeWartung.Prefix")
							+ "§eBefehle:§r\n/wartung (an/aus/reload/help)").replaceAll("&", "§"));
				}
			} else if (args.length == 0) {
				wartung = (BungeeWartung.get().config.getBoolean("BungeeWartung.Status"));
				if (wartung) {
					cs.sendMessage(BungeeWartung.get().config.getString("BungeeWartung.Prefix").replaceAll("&", "§")
							+ "§cDer Wartungsmodus ist aktiviert!");
				} else {
					cs.sendMessage(BungeeWartung.get().config.getString("BungeeWartung.Prefix").replaceAll("&", "§")
							+ "§cDer Wartungsmodus ist deaktiviert!");
				}
			}
		} else {
			cs.sendMessage(sendausgabe("BungeeWartung.Kein Recht"));
		}
	}

	public String sendausgabe(String message) {
		return (BungeeWartung.get().config.getString("BungeeWartung.Prefix")
				+ (BungeeWartung.get().config.getString(message))).replaceAll("&", "§");
	}
}
