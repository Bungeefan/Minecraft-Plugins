package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WhereIsCMD extends Command {

	private BungeeSystem instance;

	public WhereIsCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("bs.whereis") || cs.hasPermission("bs.admin")) {
			if (args.length == 0) {
				cs.sendMessage(
						instance.prefix + "§cDu musst einen Spieler eingeben, um zu sehen wo er sich gerade befindet.");
			}
			if (args.length == 1) {
				ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
				if (target == null) {
					cs.sendMessage(instance.prefix + "§cDer Spieler §e" + args[0] + " §cist nicht online.");
				} else {
					TextComponent server = new TextComponent(target.getServer().getInfo().getName());
					server.setColor(ChatColor.YELLOW);
					server.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder("§8\u00BB §aZu ihm springen").create()));
					server.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
							"/server " + target.getServer().getInfo().getName()));
					cs.sendMessage(
							new BaseComponent[] {
									new TextComponent(instance.prefix + target.getDisplayName()
											+ " §3befindet §3sich §3momentan §3auf §3dem §3Server §8\u00BB "),
									server });
				}
			} else {
				cs.sendMessage(instance.prefix + "§cDieser Befehl existiert nicht.");
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
		}
	}
}