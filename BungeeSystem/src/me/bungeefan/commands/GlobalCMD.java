package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GlobalCMD extends Command {

	private BungeeSystem instance;

	public GlobalCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("bs.global") || cs.hasPermission("bs.admin")) {
			if (args.length > 0) {
				String msg = args[0];
				for (int i = 1; i < args.length; i++) {
					msg = msg + " " + args[i];
				}
				msg = msg.replaceAll("&", "§");
				ProxyServer.getInstance().broadcast("\n" + instance.prefix + msg + "\n");
			} else {
				cs.sendMessage(instance.sendausgabe("Global.Usage"));
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
		}
	}
}