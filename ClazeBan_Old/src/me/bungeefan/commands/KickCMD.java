package me.bungeefan.commands;

import java.util.UUID;

import me.bungeefan.ClazeBan;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCMD extends Command {

	public ProxiedPlayer player;
	public String uuid;
	public String grund = "";

	public KickCMD() {
		super("kick");
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (args.length >= 2) {
			player = BungeeCord.getInstance().getPlayer(args[0]);
			if (player != null) {
				if (!player.hasPermission("cb.admin") || !player.hasPermission("cb.kick.exempt")) {
					uuid = player.getUniqueId().toString();
					grund = "";
					for (int i = 2; i < args.length; i++) {
						grund = grund + (args[i]);
						if (i >= args.length - 1) {
						} else {
							grund = grund + " ";
						}
					}
					player.disconnect(ClazeBan.get().kickMessage(cs, grund));
					ClazeBan.get().notify("Kick", uuid);

				} else {
					cs.sendMessage(ClazeBan.get().sendausgabe("Kick.Exempt"));
				}
			} else {
				cs.sendMessage(ClazeBan.get().sendausgabe("Kick.NotOnline"));
			}
		} else {
			cs.sendMessage(ClazeBan.get().sendausgabe(("Kick.Usage")));
		}
	}
}
