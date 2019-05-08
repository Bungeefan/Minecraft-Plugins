package me.bungeefan.commands;

import java.util.UUID;

import me.bungeefan.ClazeBan;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnmuteCMD extends Command {

	public ProxiedPlayer player;
	public String uuid;

	public UnmuteCMD() {
		super("unmute");
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
			ClazeBan.get().player.set("Spieler." + uuid + ".MuteAnfang", 0);
			ClazeBan.get().player.set("Spieler." + uuid + ".MuteDauer", 0);
			cs.sendMessage(new ComponentBuilder(args[0] + " wurde entmutet!").color(ChatColor.GREEN).create());
		} else {
			cs.sendMessage(ClazeBan.get().sendausgabe(("UnMute.Usage")));
		}
	}
}
