package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCMD extends Command {

	private BungeeSystem instance;
	public ProxiedPlayer player;
	public String uuid;

	public KickCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("bs.kick") || cs.hasPermission("bs.admin")) {
			if (args.length >= 2) {
				player = BungeeCord.getInstance().getPlayer(args[0]);
				if (player != null) {
					if ((instance.checkRang(cs, player) && !player.hasPermission("bs.kick.exempt"))) {
						uuid = player.getUniqueId().toString();
						String grund = args[1];
						for (int i = 2; i < args.length; i++) {
							grund = grund + " " + args[i];
						}
						player.disconnect(instance.kickMessage(cs, grund));
						instance.notifyKick(player.getName(), cs.getName(), grund);

					} else {
						cs.sendMessage(instance.sendausgabe("Kick.Exempt"));
					}
				} else {
					cs.sendMessage(instance.sendausgabe("Kick.NotOnline"));
				}
			} else {
				cs.sendMessage(instance.sendausgabe("Kick.Usage"));
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
		}
	}
}
