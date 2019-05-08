package me.bungeefan;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setspawn")) {
			if (cs.hasPermission("spawn.setspawn")) {
				if (args.length == 0) {
					if (cs instanceof Player) {
						Player p = (Player) cs;
						Location loc = p.getLocation();
						String str = "spawn.";
						Spawn.get().getConfig().set(str + "welt", loc.getWorld().getName());
						Spawn.get().getConfig().set(str + "x", ((int) loc.getX()) - 0.5);
						Spawn.get().getConfig().set(str + "y", (int) loc.getY());
						Spawn.get().getConfig().set(str + "z", ((int) loc.getZ()) + 0.5);
						Spawn.get().saveConfig();
						cs.sendMessage(sendausgabe("Spawn.Gesetzt"));
					}
				} else if (args.length == 3) {
					if (args[0].contains("~") || args[1].contains("~") || args[2].contains("~")) {
						if (cs instanceof Player) {
							Player p = (Player) cs;
							Location loc = p.getLocation();
							int x = 0;
							int y = 0;
							int z = 0;
							if (!(args[0].replaceAll("~", "")).isEmpty()) {
								x = Integer.parseInt((args[0].replaceAll("~", "")));
							}
							if (!(args[1].replaceAll("~", "")).isEmpty()) {
								y = Integer.parseInt((args[1].replaceAll("~", "")));
							}
							if (!(args[2].replaceAll("~", "")).isEmpty()) {
								z = Integer.parseInt((args[2].replaceAll("~", "")));
							}
							String str = "spawn.";
							Spawn.get().getConfig().set(str + "welt", loc.getWorld().getName());
							Spawn.get().getConfig().set(str + "x", ((int) loc.getX() + (x)) - 0.5);
							Spawn.get().getConfig().set(str + "y", ((int) loc.getY() + (y)));
							Spawn.get().getConfig().set(str + "z", ((int) loc.getZ() + (z)) + 0.5);
							Spawn.get().saveConfig();
							cs.sendMessage(sendausgabe("Spawn.Gesetzt"));
						} else {
							cs.sendMessage(sendausgabe("Spawn.Kein Spieler"));
						}
					} else {
						String str = "spawn.";
						Spawn.get().getConfig().set(str + "x", (Integer.parseInt(args[0])) - 0.5);
						Spawn.get().getConfig().set(str + "y", (Integer.parseInt(args[1])));
						Spawn.get().getConfig().set(str + "z", (Integer.parseInt(args[2])) + 0.5);
						Spawn.get().saveConfig();
						cs.sendMessage(sendausgabe("Spawn.Gesetzt"));
					}
				} else {
					cs.sendMessage(sendausgabe("Spawn.Fehler"));
					return false;
				}
			} else {
				cs.sendMessage(sendausgabe("Spawn.Kein Recht"));
			}
			return true;
		}
		return false;
	}

	public String sendausgabe(String message) {
		return (Spawn.get().getConfig().getString("Spawn.Prefix") + (Spawn.get().getConfig().getString(message))).replaceAll("&",
				"§");
	}

}
