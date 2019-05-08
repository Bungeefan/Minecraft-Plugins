package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PremiumCMD extends Command {

	private BungeeSystem instance;

	public PremiumCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		if (args.length == 0) {
			for (String zeile : instance.premiumlayout) {
				cs.sendMessage(zeile.replaceAll("&", "§"));
			}
		} else {
			cs.sendMessage(instance.prefix + "§cDieser Befehl existiert nicht.");
		}
	}
}