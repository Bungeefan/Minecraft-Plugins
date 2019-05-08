package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ClearChatCMD extends Command {
	
	private BungeeSystem instance;

	public ClearChatCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("bs.cc") || cs.hasPermission("bs.admin")) {
			if (cs instanceof ProxiedPlayer) {
				ProxiedPlayer player = (ProxiedPlayer) cs;
				if (args.length == 0) {
					for (ProxiedPlayer players : player.getServer().getInfo().getPlayers()) {
						if (!players.hasPermission("bs.cc.exempt")) {
							for (int i = 0; i < 10; i++) {
								players.sendMessage("\n\n\n\n\n\n\n\n\n\n");
							}
						}
					}
					for (ProxiedPlayer players : player.getServer().getInfo().getPlayers()) {
						players.sendMessage(instance.sendausgabe("ClearChat.Erfolg").replaceAll("%Spieler%",
								player.getDisplayName()));
					}
				} else {
					player.sendMessage(instance.sendausgabe("CleanChat.Usage"));
				}
			} else {
				cs.sendMessage(instance.sendausgabe("Allgemein.KeinSpieler"));
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
		}
	}
}