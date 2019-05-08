package me.bungeefan.commands;

import java.sql.SQLException;
import java.util.Map.Entry;

import me.bungeefan.BungeeSystem;
import me.bungeefan.Report;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportsCMD extends Command {

	private BungeeSystem instance;

	public ReportsCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {

		if (cs instanceof ProxiedPlayer) {

			ProxiedPlayer p = (ProxiedPlayer) cs;

			String prefix = instance.prefix;

			if (p.hasPermission("bs.reports")) {
				if (args.length == 0) {
					if (instance.reports.size() == 0) {
						p.sendMessage(prefix + "§cEs liegen momentan keine unbeantworteten §eReports §cvor.");
						return;
					} else {
						p.sendMessage("§8§l §c §8 §8§m--------------------§r§8 §cReport §8§m--------------------");
						int count = 0;
						for (Entry<ProxiedPlayer, Report> entry : instance.reports.entrySet()) {
							count++;
							TextComponent tc = new TextComponent("§8§l §c §8 " + "§e" + count + ". §8\u00BB "
									+ entry.getKey().getDisplayName() + " §8 ");

							TextComponent list = new TextComponent("§aAnzeigen");
							list.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("§8\u00BB §cReport §3anzeigen").create()));
							list.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
									"/reports info " + entry.getKey().getName()));

							tc.addExtra(list);

							p.sendMessage(tc);
						}
						p.sendMessage("§8§l §c §8 §8§m------------------------------------------------");
					}
				} else if (args.length == 2) {
					ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
					if (args[0].equalsIgnoreCase("info")) {

						if (target == null) {
							p.sendMessage(prefix + "§cEs liegt kein §eReport §cbezüglich des Spielers §e" + args[1]
									+ " §cvor.");
						} else if (!instance.reports.containsKey(target)) {
							p.sendMessage(prefix + "§cEs liegt kein §eReport §cbezüglich des Spielers §e"
									+ target.getName() + " §cvor.");
						} else {
							Report report = instance.reports.get(target);
							report.sendDetailsToPlayer(p);
						}
					} else if (args[0].equalsIgnoreCase("accept")) {
						if (target == null) {
							p.sendMessage(prefix + "§cEs liegt kein §eReport §cbezüglich des Spielers §e" + args[1]
									+ " §cvor.");
						} else if (!instance.reports.containsKey(target)) {
							p.sendMessage(prefix + "§cEs liegt kein §eReport §cbezüglich des Spielers §e"
									+ target.getName() + " §cvor.");
						} else {
							Report report = instance.reports.get(target);
							report.accept(p);
						}
					} else if (args[0].equalsIgnoreCase("deny")) {
						if (target == null) {
							p.sendMessage(prefix + "§cEs liegt kein §eReport §cbezüglich des Spielers §e" + args[1]
									+ " §cvor.");
						} else if (!instance.reports.containsKey(target)) {
							p.sendMessage(prefix + "§cEs liegt kein §eReport §cbezüglich des Spielers §e"
									+ target.getName() + " §cvor.");
						} else {

							Report report = instance.reports.get(target);
							report.deny(p);
						}
					} else if (args[0].equalsIgnoreCase("ban")) {
						if (target == null) {
							p.sendMessage(prefix + "§cEs liegt kein §eReport §cbezüglich des Spielers §e" + args[1]
									+ " §cvor.");
						} else if (!instance.reports.containsKey(target)) {
							p.sendMessage(prefix + "§cEs liegt kein §eReport §cbezüglich des Spielers §e"
									+ target.getName() + " §cvor.");
						} else {
							Report report = instance.reports.get(target);
							try {
								report.ban(p);
							} catch (SQLException e) {
								BungeeCord.getInstance().getConsole().sendMessage(instance.prefix + "§cMySQL Fehler, Diese Funktion ist deaktiviert!");
							}
						}
					}
				} else {
					p.sendMessage(instance.sendausgabe("Reports.Usage"));
				}
			} else {
				p.sendMessage(instance.sendausgabe("Allgemein.KeinRecht"));
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinSpieler"));
		}
	}
}