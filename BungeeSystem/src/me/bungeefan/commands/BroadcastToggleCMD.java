package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BroadcastToggleCMD extends Command {

	private BungeeSystem instance;

	public BroadcastToggleCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("bs.bctoggle") || cs.hasPermission("bs.admin")) {
			if (args.length == 0) {
				if (instance.broadcastON) {
					instance.setbroadcastON(false);
					cs.sendMessage(instance.prefix + "§cDer Broadcast wurde deaktiviert!");
				} else {
					instance.setbroadcastON(true);
					cs.sendMessage(instance.prefix + "§aDer Broadcast wurde aktiviert!");
				}
			} else {
				cs.sendMessage(instance.sendausgabe("BroadcastToggle.Usage"));
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
		}
	}
}