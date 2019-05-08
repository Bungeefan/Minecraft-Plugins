package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MSGToggleCMD extends Command {
	
	private BungeeSystem instance;

	public MSGToggleCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		if (cs instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) cs;
			if (args.length == 0) {
				if (instance.msgtoggle.contains(p)) {
					instance.msgtoggle.remove(p);
					p.sendMessage(instance.prefix + "§aDu kannst nun wieder private Nachrichten empfangen.");
				} else {
					instance.msgtoggle.add(p);
					p.sendMessage(instance.prefix + "§cDu empfängst nun keine privaten Nachrichten mehr.");
				}
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinSpieler"));
		}
	}
}