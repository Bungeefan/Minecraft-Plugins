package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class OnlineCMD extends Command {

	private BungeeSystem instance;

	public OnlineCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		int fake = (int) (ProxyServer.getInstance().getOnlineCount()
				* (double) (instance.config.getDouble("Fake") / 100.0));
		if (args.length == 0) {
			if ((ProxyServer.getInstance().getOnlineCount() + fake) == 1) {
				cs.sendMessage(instance.prefix + "§7Es ist momentan §e"
						+ (ProxyServer.getInstance().getOnlineCount() + fake) + " §7Spieler online.");
			} else {
				cs.sendMessage(instance.prefix + "§7Es sind momentan §e"
						+ (ProxyServer.getInstance().getOnlineCount() + fake) + " §7Spieler online.");
			}
		} else {
			cs.sendMessage(instance.prefix + "§cDieser Befehl existiert nicht.");
		}
	}
}