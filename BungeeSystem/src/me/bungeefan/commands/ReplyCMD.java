package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReplyCMD extends Command {

	private BungeeSystem instance;

	public ReplyCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		if (cs instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) cs;
			if (args.length == 0) {
				p.sendMessage(instance.prefix + "§cDu musst eine Nachricht eingeben, um sie zu senden.");
			} else if (args.length > 0) {
				if (instance.messager.containsKey(p) && instance.messager.get(p) != null) {
					ProxiedPlayer target = ProxyServer.getInstance().getPlayer(instance.messager.get(p).toString());
					String msg = args[0];
					for (int i = 1; i < args.length; i++) {
						msg = msg + " " + args[i];
					}
					msg = msg.replaceAll("&", "§");
					if (target == null) {
						p.sendMessage(instance.prefix + "§cDer Spieler §e" + instance.messager.get(p)
								+ " §cist nicht mehr online.");
					} else if (instance.msgtoggle.contains(target)) {
						p.sendMessage(instance.prefix
								+ "§cDu kannst diesem Spieler momentan keine privaten Nachrichten senden.");
					} else {
						instance.messager.put(target, p);
						p.sendMessage(
								instance.messages.getString("MSG.Prefix").replaceAll("%Spieler%", p.getDisplayName())
										.replaceAll("%Target%", target.getDisplayName()) + msg);
						target.sendMessage(
								instance.messages.getString("MSG.Prefix").replaceAll("%Spieler%", p.getDisplayName())
										.replaceAll("%Target%", target.getDisplayName()) + msg);
					}
				} else {
					p.sendMessage(instance.prefix + "§cDu hast keine zu beantwortenden Nachrichten.");
				}
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinSpieler"));
		}
	}
}