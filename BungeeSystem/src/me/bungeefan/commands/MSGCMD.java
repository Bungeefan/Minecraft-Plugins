package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MSGCMD extends Command {

	private BungeeSystem instance;

	public MSGCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		if (cs instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) cs;
			if (args.length > 1) {
				ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
				String msg = args[1];
				for (int i = 2; i < args.length; i++) {
					msg = msg + " " + args[i];
				}
				msg = msg.replaceAll("&", "§");
				if (target == null) {
					p.sendMessage(instance.prefix + "§cDer Spieler §e" + args[0] + " §cist nicht online.");
				} else if (target == p) {
					p.sendMessage(instance.prefix + "§cDu kannst dir nicht selber schreiben.");
				} else if (instance.msgtoggle.contains(target)) {
					p.sendMessage(
							instance.prefix + "§cDu kannst diesem Spieler momentan keine privaten Nachrichten senden.");
				} else {
					instance.messager.put(target, p);
					p.sendMessage(instance.messages.getString("MSG.Prefix").replaceAll("%Spieler%", p.getDisplayName())
							.replaceAll("%Target%", target.getDisplayName()) + msg);
					target.sendMessage(
							instance.messages.getString("MSG.Prefix").replaceAll("%Spieler%", p.getDisplayName())
									.replaceAll("%Target%", target.getDisplayName()) + msg);
				}
			} else {
				p.sendMessage(instance.sendausgabe("MSG.Usage"));
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinSpieler"));
		}
	}
}