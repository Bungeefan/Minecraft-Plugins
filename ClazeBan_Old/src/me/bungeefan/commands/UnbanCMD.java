package me.bungeefan.commands;

import java.util.UUID;

import me.bungeefan.ClazeBan;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnbanCMD extends Command {

	public ProxiedPlayer player;
	public String uuid;

	public UnbanCMD() {
		super("unban");
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (args.length == 1) {
			if (BungeeCord.getInstance().getPlayer(args[0]) != null) {
				player = BungeeCord.getInstance().getPlayer(args[0]);
				uuid = player.getUniqueId().toString();
			} else {
				uuid = ClazeBan.get().getUUID(args[0]);
			}
			ClazeBan.get().player.set("Spieler." + uuid + ".BanDauer", 0);
			ClazeBan.get().player.set("Spieler." + uuid + ".BanAnfang", 0);
			String ip = ClazeBan.get().player.getString(uuid + ".IP").replaceAll("/", ".");
			ClazeBan.get().player.set("IPs." + ip, null);
			cs.sendMessage(new ComponentBuilder(args[0] + " wurde entbannt!").color(ChatColor.GREEN).create());
		} else {
			cs.sendMessage(ClazeBan.get().sendausgabe(("UnBan.Usage")));
		}
	}
}
