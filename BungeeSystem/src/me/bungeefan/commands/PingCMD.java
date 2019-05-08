package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PingCMD extends Command {

	private BungeeSystem instance;

	public PingCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		if (args.length == 0) {
			if (cs instanceof ProxiedPlayer) {
				ProxiedPlayer p = (ProxiedPlayer) cs;
				p.sendMessage(instance.prefix + "§7Dein Ping§8\u00BB §e" + p.getPing() + "ms");
			} else {
				cs.sendMessage(instance.sendausgabe("Allgemein.KeinSpieler"));
			}
		} else if (args.length == 1) {
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);
			if (player != null) {
				cs.sendMessage(instance.prefix + player.getDisplayName() + "§7's §7Ping §8\u00BB §e"
						+ player.getPing() + "ms");
			} else {
				cs.sendMessage(instance.prefix + "§cDer Spieler §e" + args[0] + " §cist nicht online.");
			}
		} else {
			cs.sendMessage(instance.prefix + "§cDieser Befehl existiert nicht.");
		}
	}
}