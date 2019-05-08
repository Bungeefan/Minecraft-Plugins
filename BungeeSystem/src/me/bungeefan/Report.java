package me.bungeefan;

import java.sql.SQLException;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Report {

	private BungeeSystem instance;
	private ProxiedPlayer reported;
	private ProxiedPlayer reporting;
	private MySQL sql;

	private String reason;

	public Report(BungeeSystem instance, ProxiedPlayer reported, String type, ProxiedPlayer reporting) {
		this.instance = instance;
		this.reported = reported;
		this.reporting = reporting;
		this.reason = type;
		instance.reports.put(this.reported, this);
		sql = instance.sql;
	}

	public String getReason() {
		return this.reason;
	}

	public ProxiedPlayer getReportedPlayer() {
		return this.reported;
	}

	public ProxiedPlayer getReportingPlayer() {
		return this.reporting;
	}

	public void announce() {
		TextComponent all = new TextComponent("§8§l §c §8 ");
		TextComponent list = new TextComponent("§a§nReports");

		list.setHoverEvent(
				new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§8\u00BB §7Reports").create()));
		list.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/reports"));

		all.addExtra(list);

		for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
			if ((players.hasPermission("bs.notify") || players.hasPermission("bs.admin"))
					&& !instance.reporttoggle.contains(players)) {
				players.sendMessage("§8§l §c §8 §8§m--------------------§r§8 §cReport §8§m--------------------");
				players.sendMessage("§8§l| §c+ §8| " + "§7Ein neuer §cReport §7ist eingegangen.");
				String amount = "";
				if (instance.reports.size() < 4) {
					amount = "§a" + instance.reports.size();
				} else if (instance.reports.size() < 7) {
					amount = "§e" + instance.reports.size();
				} else if (instance.reports.size() < 10) {
					amount = "§c" + instance.reports.size();
				} else if (instance.reports.size() < 13) {
					amount = "§4" + instance.reports.size();
				} else if (instance.reports.size() >= 13) {
					amount = "§4§l" + instance.reports.size();
				}
				players.sendMessage("§8§l §c §8 " + "§7Unbearbeitete §cReports §8\u00BB " + amount);
				players.sendMessage(all);
				players.sendMessage("§8§l §c §8 §8§m------------------------------------------------");
			}
		}
	}

	public void sendDetailsToPlayer(ProxiedPlayer p) {
		TextComponent tc = new TextComponent("§8§l §c §8 " + "§7Server §8\u00BB ");
		TextComponent server = new TextComponent("§e" + this.reported.getServer().getInfo().getName());
		server.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("§8\u00BB §aZu ihm springen").create()));
		server.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
				"/server " + this.reported.getServer().getInfo().getName()));
		tc.addExtra(server);

		TextComponent action = new TextComponent("§8§l §c §8 ");

		TextComponent accept = new TextComponent("§aAnnehmen");
		accept.setHoverEvent(
				new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§8\u00BB §aAnnehmen").create()));
		accept.setClickEvent(
				new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports accept " + this.reported.getName()));

		TextComponent spacer = new TextComponent(" §8 ");

		TextComponent deny = new TextComponent("§cAblehnen");
		deny.setHoverEvent(
				new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§8\u00BB §cAblehnen").create()));
		deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports deny " + this.reported.getName()));

		TextComponent ban = new TextComponent("§eReportausnutzung");
		ban.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("§8\u00BB §eReportausnutzung").create()));
		ban.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports ban " + this.reported.getName()));

		action.addExtra(accept);
		action.addExtra(spacer);
		action.addExtra(deny);
		action.addExtra(spacer);
		action.addExtra(ban);

		p.sendMessage("§8§l §c §8 §8§m--------------------§r§8 §cReport §8§m--------------------");
		p.sendMessage("§8§l §c §8 " + "§7Spieler §8\u00BB " + this.reported.getDisplayName());
		p.sendMessage("§8§l §c §8 " + "§7Grund §8\u00BB §e" + this.reason);
		p.sendMessage("§8§l §c §8 " + "§7Reportet von §8\u00BB " + this.reporting.getDisplayName());
		p.sendMessage(tc);
		p.sendMessage(action);
		p.sendMessage("§8§l §c §8 §8§m------------------------------------------------");
	}

	public void accept(ProxiedPlayer dealing) {

		String prefix2 = instance.prefix;

		instance.reports.remove(this.reported);

		dealing.sendMessage("§8§l|§cReport§8| §r§7Du hast den §cReport §7bezüglich " + this.reported.getDisplayName()
				+ " §aangenommen§7.");

		if (dealing.getServer().getInfo() != this.reported.getServer().getInfo()) {
			dealing.connect(this.reported.getServer().getInfo());
		}

		for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
			if (players.hasPermission("bs.owner") | players.hasPermission("bs.developer")
					| players.hasPermission("bs.moderator") | players.hasPermission("bs.srmoderator")
					| players.hasPermission("bs.administrator")) {
				if (players != dealing) {

					players.sendMessage("§8§l §c §8 §7Der §cReport §7bezüglich " + this.reported.getDisplayName()
							+ " §7wurde von " + dealing.getDisplayName() + " §aangenommen§7.");
				}
			}
		}

		ProxiedPlayer reporter = BungeeCord.getInstance().getPlayer(this.reporting.getUniqueId());

		if (reporter != null) {
			reporter.sendMessage(prefix2 + "§7Dein §cReport §7bezüglich " + this.reported.getDisplayName()
					+ " §7wurde von " + dealing.getDisplayName() + " §aangenommen§7.");
		}
	}

	public void deny(ProxiedPlayer dealing) {

		String prefix2 = instance.prefix;

		instance.reports.remove(this.reported);

		dealing.sendMessage(
				"§8§l §c §8 §7Du hast den §cReport §7bezüglich " + this.reported.getDisplayName() + " §cabgelehnt§7.");

		for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
			if (players.hasPermission("bs.owner") | players.hasPermission("bs.developer")
					| players.hasPermission("bs.moderator") | players.hasPermission("bs.srmoderator")
					| players.hasPermission("bs.administrator")) {
				if (players != dealing) {

					players.sendMessage("§8§l §c §8 §7Der §cReport §7bezüglich " + this.reported.getDisplayName()
							+ " §7wurde von " + dealing.getDisplayName() + " §cabgelehnt§7.");
				}
			}
		}

		ProxiedPlayer reporter = BungeeCord.getInstance().getPlayer(this.reporting.getUniqueId());

		if (reporter != null) {
			reporter.sendMessage(prefix2 + "§7Dein §cReport §7bezüglich " + this.reported.getDisplayName()
					+ " §7wurde von " + dealing.getDisplayName() + " §cabgelehnt§7.");
		}
	}

	public void ban(ProxiedPlayer dealing) throws SQLException {

		String prefix2 = instance.prefix;

		instance.reports.remove(this.reported);

		dealing.sendMessage(
				"§8§l §c §8 §7Du hast den §cReport §7bezüglich " + this.reported.getDisplayName() + " §cabgelehnt§7.");

		for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
			if (players.hasPermission("bs.owner") | players.hasPermission("bs.developer")
					| players.hasPermission("bs.moderator") | players.hasPermission("bs.srmoderator")
					| players.hasPermission("bs.administrator")) {
				if (players != dealing) {
					players.sendMessage("§8§l §c §8 §7Der §cReport §7bezüglich " + this.reported.getDisplayName()
							+ " §7wurde von " + dealing.getDisplayName() + " §cabgelehnt§7.");
				}
			}
		}

		ProxiedPlayer reporter = BungeeCord.getInstance().getPlayer(this.reporting.getUniqueId());

		if (reporter != null) {
			reporter.sendMessage(prefix2 + "§7Dein §cReport §7bezüglich " + this.reported.getDisplayName()
					+ " §7wurde von " + dealing.getDisplayName() + " §cabgelehnt§7.");

			if (reporter == dealing) {
				dealing.sendMessage(prefix2 + "§cDu darfst dich nicht selbst muten.");
				return;
			}

			if ((!reporter.hasPermission("bs.owner") && !reporter.hasPermission("bs.supporter")
					&& !reporter.hasPermission("bs.builder") && !reporter.hasPermission("bs.developer")
					&& !reporter.hasPermission("bs.moderator") && !reporter.hasPermission("bs.srmoderator")
					&& !reporter.hasPermission("bs.administrator")) | dealing.hasPermission("bs.administrator")) {
				if (sql.isPlayerExisting2(reporter)) {
					if (sql.getUntil2(reporter) != -1 && System.currentTimeMillis() - sql.getUntil2(reporter) > 0) {
						sql.setUnBanned2(reporter);
					} else {
						dealing.sendMessage(
								prefix2 + "§cDer Spieler §e" + reporter.getName() + " §cist schon gemutet.");
						return;
					}
				}
				reporter.sendMessage(prefix2 + "§cDu wurdest für §e3 Tage §cvom Netzwerk gemutet.");
				reporter.sendMessage(prefix2 + "§7Grund §8\u00BB §eReportausnutzung");
				reporter.sendMessage(prefix2 + "§7Gemutet von §8\u00BB §e" + dealing.getName());
				reporter.sendMessage(
						prefix2 + "§aDu kannst auf unserer §eHomepage §aeinen §eEntbannungsantrag §astellen.");
				sql.registerPlayer2(reporter);
				String[] ip = reporter.getAddress().toString().split(":");
				String subip = ip[0];
				long time = 0;
				try {
					long time2 = Long.valueOf(3);
					time = System.currentTimeMillis() + time2 * 1000 * 60 * 60 * 24;
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
				sql.setBanned2(reporter, " Unngültiger Report", dealing, System.currentTimeMillis(), time, subip);
				dealing.sendMessage("§8§l §c §8 §8§m----------------------§r§8 §cMute §8§m---------------------");
				dealing.sendMessage("§8§l §c §8 §7Spieler §8\u00BB §e" + reporter.getName());
				dealing.sendMessage("§8§l §c §8 §7Grund §8\u00BB §eReportausnutzung");
				dealing.sendMessage("§8§l §c §8 §7Gemutet von §8\u00BB §e" + dealing.getName());
				dealing.sendMessage("§8§l §c §8 §7Gemutete Zeit §8\u00BB §e3 Tage");
				dealing.sendMessage("§8§l §c §8 §8§m------------------------------------------------");
				for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
					if (players != dealing) {
						if (players.hasPermission("bs.owner") | players.hasPermission("bs.supporter")
								| players.hasPermission("bs.developer") | players.hasPermission("bs.moderator")
								| players.hasPermission("bs.srmoderator") | players.hasPermission("bs.administrator")) {
							if (!instance.notifytoggle.contains(players)) {
								players.sendMessage(
										"§8§l §c §8 §8§m----------------------§r§8 §cMute §8§m---------------------");
								players.sendMessage("§8§l §c §8 §7Spieler §8\u00BB §e" + reporter.getName());
								players.sendMessage("§8§l §c §8 §7Grund §8\u00BB §eReportausnutzung");
								players.sendMessage("§8§l §c §8 §7Gemutet von §8\u00BB §e" + dealing.getName());
								players.sendMessage("§8§l §c §8 §7Gemutete Zeit §8\u00BB §e3 Tage");
								players.sendMessage("§8§l §c §8 §8§m------------------------------------------------");
							}
						}
					}
				}
			} else {
				dealing.sendMessage(prefix2 + "§cDu darfst keine §eTeam-Mitglieder §cmuten.");
			}
		} else {
			String uuid = sql.getUUID(reporting.getName());
			String name = sql.getPlayerName(uuid);
			if (name != null) {
				String group = sql.getPlayerGroup(uuid);
				if ((!group.equalsIgnoreCase("Owner") && !group.equalsIgnoreCase("Supporter")
						&& !group.equalsIgnoreCase("Builder") && !group.equalsIgnoreCase("Developer")
						&& !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("SrModerator")
						&& !group.equalsIgnoreCase("Administrator")) | dealing.hasPermission("bs.administrator")) {
					if (sql.isPlayerExisting2(uuid)) {
						if (sql.getUntil2(uuid) != -1 && System.currentTimeMillis() - sql.getUntil2(uuid) > 0) {
							sql.setUnBanned2(uuid);
						} else {
							dealing.sendMessage(prefix2 + "§cDer Spieler §e" + name + " §cist schon gemutet.");
							return;
						}
					}
					dealing.sendMessage(prefix2 + "§cDer Spieler §e" + name + " §cist momentan nicht online.");
					sql.registerPlayer2(name, uuid, reporting.getAddress().getAddress().getHostAddress());
					long time = 0;
					try {
						long time2 = Long.valueOf(3);
						time = System.currentTimeMillis() + time2 * 1000 * 60 * 60 * 24;
					} catch (NumberFormatException ex) {
						ex.printStackTrace();
					}
					sql.setBanned2(uuid, " Unngültiger Report", dealing, System.currentTimeMillis(), time, "?");
					dealing.sendMessage("§8§l §c §8 §8§m----------------------§r§8 §cMute §8§m---------------------");
					dealing.sendMessage("§8§l §c §8 §7Spieler §8\u00BB §e" + name);
					dealing.sendMessage("§8§l §c §8 §7Grund §8\u00BB §eReportausnutzung");
					dealing.sendMessage("§8§l §c §8 §7Gemutet von §8\u00BB §e" + dealing.getName());
					dealing.sendMessage("§8§l §c §8 §7Gemutete Zeit §8\u00BB §e3 Tage");
					dealing.sendMessage("§8§l §c §8 §8§m------------------------------------------------");
					for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
						if (players != dealing) {
							if (players.hasPermission("bs.owner") | players.hasPermission("bs.supporter")
									| players.hasPermission("bs.developer") | players.hasPermission("bs.moderator")
									| players.hasPermission("bs.srmoderator")
									| players.hasPermission("bs.administrator")) {
								if (!instance.notifytoggle.contains(players)) {
									players.sendMessage(
											"§8§l §c §8 §8§m----------------------§r§8 §cMute §8§m---------------------");
									players.sendMessage("§8§l §c §8 §7Spieler §8\u00BB §e" + name);
									players.sendMessage("§8§l §c §8 §7Grund §8\u00BB §eReportausnutzung");
									players.sendMessage("§8§l §c §8 §7Gemutet von §8\u00BB §e" + dealing.getName());
									players.sendMessage("§8§l §c §8 §7Gemutete Zeit §8\u00BB §e3 Tage");
									players.sendMessage(
											"§8§l §c §8 §8§m------------------------------------------------");
								}
							}
						}
					}
				} else {
					dealing.sendMessage(prefix2 + "§cDu darfst keine §eTeam-Mitglieder §cmuten.");
				}
			} else {
				dealing.sendMessage(prefix2 + "§cDer Spieler §e" + this.reporting.getName() + " §cexistiert nicht.");
			}
		}
	}
}