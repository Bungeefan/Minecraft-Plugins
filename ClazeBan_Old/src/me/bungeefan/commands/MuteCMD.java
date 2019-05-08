package me.bungeefan.commands;

import java.io.IOException;
import java.util.UUID;

import me.bungeefan.ClazeBan;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MuteCMD extends Command {

	public ProxiedPlayer player;
	public String uuid;
	public long dauer;
	public long ende = 0;
	public String grund = "";

	public MuteCMD() {
		super("mute");
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (args.length >= 3) {
			if (BungeeCord.getInstance().getPlayer(args[0]) != null) {
				player = BungeeCord.getInstance().getPlayer(args[0]);
				uuid = player.getUniqueId().toString();
			} else {
				uuid = ClazeBan.get().getUUID(args[0]);
				player = ClazeBan.get().getProxy().getPlayer(uuid);
			}
			if (uuid != null) {
				if (!player.hasPermission("cb.admin") || !player.hasPermission("cb.mute.exempt")) {
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
						}
						dauer += 5000;
					}

					grund = "";
					for (int i = 2; i < args.length; i++) {
						grund = grund + (args[i]);
						if (i >= args.length - 1) {
						} else {
							grund = grund + " ";
						}
					}
					ClazeBan.get().player.set("Spieler." + uuid + ".Spieler", args[0]);
					ClazeBan.get().player.set("Spieler." + uuid + ".MuteOperator", cs.getName());
					ClazeBan.get().player.set("Spieler." + uuid + ".MuteGrund", grund);
					ClazeBan.get().player.set("Spieler." + uuid + ".MuteDauer", dauer);
					ClazeBan.get().player.set("Spieler." + uuid + ".MuteAnfang", System.currentTimeMillis());
					try {
						ClazeBan.get().saveFiles();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (player != null) {
						// Spieler muten
					}
					ClazeBan.get().notify("Mute", uuid);
				} else {
					cs.sendMessage(ClazeBan.get().sendausgabe("Allgemein.UUIDFehler"));
				}
			} else {
				cs.sendMessage(ClazeBan.get().sendausgabe("Mute.Exempt"));
			}
		} else {
			cs.sendMessage(ClazeBan.get().sendausgabe(("Mute.Usage")));
		}
	}
}
