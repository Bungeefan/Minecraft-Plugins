package me.bungeefan.commands;

import me.bungeefan.ClazeBan;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CheckCMD extends Command {

	public String uuid;
	public String grund = "";

	public CheckCMD() {
		super("check");
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (args.length == 1) {
			if (BungeeCord.getInstance().getPlayer(args[0]) != null) {
				uuid = BungeeCord.getInstance().getPlayer(args[0]).getUniqueId().toString();
			} else {
				uuid = ClazeBan.get().getUUID(args[0]);
			}
			if (ClazeBan.get().player.getString("Spieler." + uuid + ".Spieler") != "") {
				String ip = ClazeBan.get().player.getString("Spieler." + uuid + ".IP").replaceAll("/", ".");
				String spieler = ClazeBan.get().player.getString("Spieler." + uuid + ".Spieler");
				String banoperator = ClazeBan.get().player.getString("Spieler." + uuid + ".BanOperator");
				String muteoperator = ClazeBan.get().player.getString("Spieler." + uuid + ".MuteOperator");
				String bangrund = ClazeBan.get().player.getString("Spieler." + uuid + ".BanGrund");
				String mutegrund = ClazeBan.get().player.getString("Spieler." + uuid + ".MuteGrund");
				long bandauer = ClazeBan.get().player.getLong("Spieler." + uuid + ".BanDauer");
				long bananfang = ClazeBan.get().player.getLong("Spieler." + uuid + ".BanAnfang");
				long mutedauer = ClazeBan.get().player.getLong("Spieler." + uuid + ".MuteDauer");
				long muteanfang = ClazeBan.get().player.getLong("Spieler." + uuid + ".MuteAnfang");

				String checkheader = (ClazeBan.get().messages.getString("Check.Header").replaceAll("%Spieler%",
						spieler)).replaceAll("&", "§");
				String checkuuid = (ClazeBan.get().messages.getString("Check.UUID").replaceAll("%UUID%",
						uuid.toString())).replaceAll("&", "§");
				String checkip = (ClazeBan.get().messages.getString("Check.IP").replaceAll("%IP%", ip)).replaceAll("&",
						"§");
				String checkban = (ClazeBan.get().messages.getString("Check.Ban").replaceAll("%OPERATOR%", banoperator))
						.replaceAll("&", "§");
				String checkmute = (ClazeBan.get().messages.getString("Check.Mute").replaceAll("%OPERATOR%",
						muteoperator)).replaceAll("&", "§");
				String checkbanreason = "";
				if (bangrund != "") {
					checkbanreason = (ClazeBan.get().messages.getString("Check.BanReason").replaceAll("%Grund%",
							bangrund)).replaceAll("&", "§");
				}
				String checkmutereason = "";
				if (mutegrund != "") {
					checkmutereason = (ClazeBan.get().messages.getString("Check.MuteReason").replaceAll("%Grund%",
							mutegrund)).replaceAll("&", "§");
				}

				String remainingTime = "";
				if (bangrund != "") {
					if (bandauer == -1) {
						remainingTime = "§4§lPermanent";
					} else {
						long current = System.currentTimeMillis();
						long end = bananfang + bandauer;
						long difference = end - current;
						difference /= 1000;
						int minuten = 0;
						int stunden = 0;
						int tage = 0;
						int monate = 0;
						while (difference >= 60L) {
							difference -= 60L;
							minuten++;
						}
						while (minuten >= 60) {
							minuten -= 60;
							stunden++;
						}
						while (stunden >= 24) {
							stunden -= 24;
							tage++;
						}
						while (tage >= 30) {
							tage -= 30;
							monate++;
						}
						remainingTime = "§e" + monate + " Monat(e) " + tage + " Tag(e), " + stunden + " Stunde(n), "
								+ minuten + " Minute(n) ";
					}
					checkban = checkban.replaceAll("%BanDauer%", remainingTime);
				} else {
					checkban = "§cNicht gebannt!";
				}

				if (mutegrund != "") {
					if (mutedauer == -1) {
						remainingTime = "§4§lPermanent";
					} else {
						long current = System.currentTimeMillis();
						long end = muteanfang + mutedauer;
						long difference = end - current;
						difference /= 1000;
						int minuten = 0;
						int stunden = 0;
						int tage = 0;
						int monate = 0;
						while (difference >= 60L) {
							difference -= 60L;
							minuten++;
						}
						while (minuten >= 60) {
							minuten -= 60;
							stunden++;
						}
						while (stunden >= 24) {
							stunden -= 24;
							tage++;
						}
						while (tage >= 30) {
							tage -= 30;
							monate++;
						}
						remainingTime = "§e" + monate + " Monat(e) " + tage + " Tag(e), " + stunden + " Stunde(n), "
								+ minuten + " Minute(n) ";
					}
					checkmute = checkmute.replaceAll("%MuteDauer%", remainingTime);
				} else {
					checkmute = "§cNicht gemutet!";
				}

				String checkmessage = checkheader + "\n" + checkuuid + "\n" + checkip + "\n" + checkban + "\n"
						+ checkbanreason + "\n" + checkmute + "\n" + checkmutereason;
				cs.sendMessage(checkmessage);
			} else {
				cs.sendMessage(ClazeBan.get().sendausgabe(("Check.NotFound")).replaceAll("%Spieler%", args[0]));
			}

		} else {
			cs.sendMessage(ClazeBan.get().sendausgabe(("Check.Usage")));
		}
	}
}
