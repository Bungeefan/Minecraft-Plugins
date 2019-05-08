package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class NotifyToggleCMD extends Command {

	private BungeeSystem instance;

	public NotifyToggleCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		if (cs instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) cs;
			if (p.hasPermission("bs.notify") || cs.hasPermission("bs.admin")) {
				if (args.length == 0) {
					if (!instance.notifytoggle.contains(p)) {
						instance.notifytoggle.add(p);
						p.sendMessage(instance.prefix
								+ "§cDu empfängst nun keine §eKick-§c, §eMute-§c und §eBan-§cNachrichten §cmehr.");
					} else {
						instance.notifytoggle.remove(p);
						p.sendMessage(instance.prefix
								+ "§aDu kannst nun wieder §eKick-§a, §eMute-§a und §eBan-§aNachrichten §aempfangen.");
					}
				} else {
					cs.sendMessage(instance.sendausgabe("NotifyToggle.Usage"));
				}
			} else {
				cs.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinSpieler"));
		}
	}
}