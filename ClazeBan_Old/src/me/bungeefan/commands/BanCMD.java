package me.bungeefan.commands;

import java.io.IOException;
import java.util.UUID;

import me.bungeefan.ClazeBan;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanCMD extends Command {

	public ProxiedPlayer player;
	public String uuid;
	public long dauer;
	public long ende = 0;
	public String grund = "";

	public BanCMD() {
		super("ban");
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (args.length >= 3) {
			String ip = null;
			try {
				if (BungeeCord.getInstance().getPlayer(args[0]) != null) {
					player = BungeeCord.getInstance().getPlayer(args[0]);
					ip = player.getAddress().getAddress().getHostAddress().replace(".", "/");
					uuid = player.getUniqueId().toString().replaceAll("-", "");
					System.out.println(uuid + " A");
				} else {
					uuid = ClazeBan.get().getUUID(args[0]);
					System.out.println(uuid);
					uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
					UUID u = UUID.fromString(uuid);
					System.out.println(u);
					player = BungeeCord.getInstance().getPlayerByOfflineUUID(u);
					System.out.println(uuid + " B");
				}
			} catch (Exception e) {
				e.printStackTrace();
				uuid = null;
			}
			if (uuid != null) {
				if (!player.hasPermission("cb.admin") || !player.hasPermission("cb.ban.exempt")) {
					if (args[1].equals("-1")) {
						dauer = -1;
					} else {
						String zeit = args[1].trim().toLowerCase();
						if (zeit.endsWith("s")) {
							dauer = Long.parseLong(zeit.substring(0, zeit.length() - 1));
							dauer *= 1000;
						} else if (zeit.endsWith("min")) {
							dauer = Long.parseLong(zeit.substring(0, zeit.length() - 3));
							dauer *= 1000 * 60;
						} else if (zeit.endsWith("h")) {
							dauer = Long.parseLong(zeit.substring(0, zeit.length() - 1));
							dauer *= 1000 * 60 * 60;
						} else if (zeit.endsWith("d")) {
							dauer = Long.parseLong(zeit.substring(0, zeit.length() - 1));
							dauer *= 1000 * 60 * 60 * 24;
						} else if (zeit.endsWith("m")) {
							dauer = Long.parseLong(zeit.substring(0, zeit.length() - 1));
							dauer *= 1000 * 60 * 60 * 24 * 30;
						} else {
							dauer = Long.parseLong(zeit.replaceAll("\\D", ""));
							dauer *= 1000;
						}
						dauer += 3000;
					}
					grund = "";
					for (int i = 2; i < args.length; i++) {
						grund = grund + (args[i]);
						if (i >= args.length - 1) {
						} else {
							grund = grund + " ";
						}
					}
					ClazeBan.get().player.set("Spieler." + uuid + ".IP", ip);
					ClazeBan.get().player.set("Spieler." + uuid + ".Spieler", args[0]);
					ClazeBan.get().player.set("Spieler." + uuid + ".BanOperator", cs.getName());
					ClazeBan.get().player.set("Spieler." + uuid + ".BanGrund", grund);
					ClazeBan.get().player.set("Spieler." + uuid + ".BanDauer", dauer);
					ClazeBan.get().player.set("Spieler." + uuid + ".BanAnfang", System.currentTimeMillis());
					ClazeBan.get().player.set("IPs." + ip + ".UUID", uuid);
					try {
						ClazeBan.get().saveFiles();
					} catch (IOException exp) {
						exp.printStackTrace();
					}
					if (player != null) {
						player.disconnect(ClazeBan.get().banMessage(uuid));
					}
					ClazeBan.get().notify("Ban", uuid);
				} else {
					cs.sendMessage(ClazeBan.get().sendausgabe("Ban.Exempt"));
				}
			} else {
				cs.sendMessage((ClazeBan.get().sendausgabe("Allgemein.UUIDFehler").replaceAll("%Spieler%", args[0])));
			}
		} else {
			cs.sendMessage(ClazeBan.get().sendausgabe("Ban.Usage"));
		}

	}
}
