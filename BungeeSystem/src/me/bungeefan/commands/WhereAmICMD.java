package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WhereAmICMD extends Command {

	private BungeeSystem instance;

	public WhereAmICMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		if (cs instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) cs;
			if (args.length == 0) {
				p.sendMessage(instance.prefix + "§3Du befindest dich momentan auf dem Server §8\u00BB §e"
						+ p.getServer().getInfo().getName());
			} else {
				p.sendMessage(instance.prefix + "§cDieser Befehl existiert nicht.");
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinSpieler"));
		}
	}
}