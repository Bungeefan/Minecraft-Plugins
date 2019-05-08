package me.bungeefan.commands;

import java.util.concurrent.TimeUnit;

import me.bungeefan.BungeeSystem;
import me.bungeefan.Report;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportCMD extends Command {

	private BungeeSystem instance;

	public ReportCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		if (cs instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) cs;
			if (!instance.reportcd.contains(p)) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("login")) {
						instance.reporttoggle.remove(p);
						p.sendMessage(instance.prefix + "§aDu empfängst nun §eReportnachrichten.");
					} else if (args[0].equalsIgnoreCase("logout")) {
						instance.reporttoggle.add(p);
						p.sendMessage(instance.prefix + "§aDu empfängst nun keine §eReportnachrichten.");
					}
				} else if (args.length > 1) {
					String reason = args[1];
					for (int i = 2; i < args.length; i++) {
						reason = reason + " " + args[i];
					}
					ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);
					if (player != null) {
						if (player != p) {
							if ((!player.hasPermission("bs.notify"))) {
								if (instance.reports.containsKey(player)) {
									p.sendMessage(instance.sendausgabe("Report.AlreadyReportet").replaceAll("%Spieler%",
											player.getName()));
								} else {
									p.sendMessage(instance.sendausgabe("Report.Erfolg"));
									Report report = new Report(instance, player, reason, p);
									report.announce();
									instance.reportcd.add(p);
									ProxyServer.getInstance().getScheduler().schedule(instance, new Runnable() {

										@Override
										public void run() {
											instance.reportcd.remove(p);
										}
									}, 5, TimeUnit.SECONDS);
								}
							} else {
								p.sendMessage(instance.prefix + "§cDu kannst keine §eTeam-Mitglieder §creporten.");
							}
						} else {
							p.sendMessage(instance.prefix + "§cDu darfst dich nicht selbst reporten.");
						}
					} else {
						p.sendMessage(instance.sendausgabe("Allgemein.NichtOnline").replaceAll("%Spieler", args[0]));
					}
				} else {
					p.sendMessage(instance.sendausgabe("Report.Usage"));
				}
			} else {
				p.sendMessage(instance.prefix + "§cDu kannst diesen Befehl nur alle §e5 §cSekunden ausführen.");
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinSpieler"));
		}
	}
}