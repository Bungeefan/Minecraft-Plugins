package me.bungeefan;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Ping extends JavaPlugin {

	public void onEnable() {
		loadConfig();
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ping")) {
			Player p;
			if (args.length == 0) {
				if (cs instanceof Player) {
					p = (Player) cs;
					cs.sendMessage(getPingMSG(p, false));
					return true;
				} else {
					cs.sendMessage(sendausgabe("Ping.KeinSpieler"));
					return true;
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					if (cs.hasPermission("ping.reload")) {
						String console = (getConfig().getString("Ping.Prefix")).replaceAll("&", "§");
						cs.sendMessage(console + "§cDie Config wird reloaded.");
						loadConfig();
						cs.sendMessage(console + "§aDie Config wurde erfolgreich reloaded.");
					} else {
						cs.sendMessage(sendausgabe("Ping.KeinRecht"));
					}
				} else if (cs.hasPermission("ping.other")) {
					p = Bukkit.getPlayer(args[0]);
					if (p != null) {
						cs.sendMessage(getPingMSG(p, true));
					} else {
						cs.sendMessage(sendausgabe("Ping.Nichtgefunden"));
					}
					return true;
				} else {
					cs.sendMessage(sendausgabe("Ping.KeinRecht"));
					return true;
				}
				return true;
			}
		}
		return false;
	}

	public int getPing(Player p) {
		return ((CraftPlayer) p).getHandle().ping;
	}

	public String getPingMSG(Player p, boolean other) {
		int ping = getPing(p);
		int highping = getConfig().getInt("Ping.HighPing");
		String pingmsg;
		if (ping >= highping) {
			pingmsg = "§c" + String.valueOf(ping);
		} else {
			pingmsg = "§a" + String.valueOf(ping);
		}
		String msg;
		if (!other) {
			msg = sendausgabe("Ping.PingMSG").replaceAll("%ping%", pingmsg);
		} else {
			msg = sendausgabe("Ping.OtherPingMSG").replaceAll("%ping%", pingmsg).replaceAll("%Spieler%", p.getName());
		}
		return msg;
	}

	public String sendausgabe(String message) {
		return (getConfig().getString("Ping.Prefix") + (getConfig().getString(message))).replaceAll("&", "§");
	}

	private void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();
	}
}
