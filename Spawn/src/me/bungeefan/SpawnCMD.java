package me.bungeefan;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Iterables;

public class SpawnCMD implements CommandExecutor {

	private String console;
	private Collection<? extends Player> onlineplayer;
	private boolean erneut = false;

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("spawn")) {
			String str = "spawn.";
			World w = Bukkit.getWorld(Spawn.get().getConfig().getString(str + "welt"));
			double x = Spawn.get().getConfig().getDouble(str + "x");
			double y = Spawn.get().getConfig().getDouble(str + "y");
			double z = Spawn.get().getConfig().getDouble(str + "z");
			Location loc = new Location(w, x, y, z);

			if (args.length == 0) {
				if (cs instanceof Player) {
					Player p = (Player) cs;
					Location ploc = p.getLocation();
					float yaw = ploc.getYaw();
					float pitch = ploc.getPitch();
					loc = new Location(w, x, y, z, yaw, pitch);
					p.teleport(loc);
					cs.sendMessage(sendausgabe("Spawn.Spawn"));
				} else {
					cs.sendMessage(sendausgabe("Spawn.Kein Spieler"));
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("all")) {
					if (cs.hasPermission("spawn.spawnall")) {
						onlineplayer = Bukkit.getOnlinePlayers();
						if (onlineplayer.size() == 0) {
							cs.sendMessage(sendausgabe("Spawn.Nicht gefunden"));
						} else {
							for (int i = 0; i < onlineplayer.size(); i++) {
								Player player = Iterables.get(onlineplayer, i);
								Location ploc = player.getLocation();
								float yaw = ploc.getYaw();
								float pitch = ploc.getPitch();
								loc = new Location(w, x, y, z, yaw, pitch);
								player.teleport(loc);
							}
							Bukkit.getServer().broadcastMessage(
									(Spawn.get().getConfig().getString("Spawn.Alle Teleportiert").replaceAll("&", "§"))
											.replace("[Player]", cs.getName()));
						}
					} else {
						cs.sendMessage(sendausgabe("Spawn.Kein Recht"));
					}
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (cs.hasPermission("spawn.reload")) {
						console = (Spawn.get().getConfig().getString("Spawn.Prefix")).replaceAll("&", "§");
						cs.sendMessage(console + "§cDie Config wird reloaded.");
						Spawn.get().reloadConfig();
						Spawn.get().saveConfig();
						cs.sendMessage(console + "§aDie Config wurde erfolgreich reloaded.");
					} else {
						cs.sendMessage(sendausgabe("Spawn.Kein Recht"));
					}
				} else if (args[0].equalsIgnoreCase("deaktivieren")) {
					if (cs.hasPermission("spawn.deactivate")) {
						if (erneut) {
							cs.sendMessage((Spawn.get().getConfig().getString("Spawn.Prefix")).replaceAll("&", "§")
									+ "§cPlugin wurde deaktivert. Um es wieder zu aktiveren, Server neustarten oder reloaden!");
							Bukkit.getPluginManager().disablePlugin(Spawn.get());
						} else {
							cs.sendMessage(
									"§cBist du dir sicher ?\n§4Das kann nicht rückgängig gemacht werden !\n§cFühre diesen Command erneut aus!");
							erneut = true;
						}
					} else {
						cs.sendMessage(sendausgabe("Spawn.Kein Recht"));
					}
				} else if (args[0].equalsIgnoreCase("help")) {
					if (cs.hasPermission("spawn.help")) {
						cs.sendMessage((Spawn.get().getConfig().getString("Spawn.Prefix")).replaceAll("&", "§")
								+ "§eBefehle:§r\n§b/setspawn (X) (Y) (Z)\n/spawn (all/reload/help/deaktivieren/Spielername)");
					} else {
						cs.sendMessage(sendausgabe("Spawn.Kein Recht"));
					}
				} else if (cs.hasPermission("spawn.spawnplayer")) {
					Player ziel = (Bukkit.getServer().getPlayer(args[0]));
					if (ziel != null) {
						Location ploc = ziel.getLocation();
						float yaw = ploc.getYaw();
						float pitch = ploc.getPitch();
						loc = new Location(w, x, y, z, yaw, pitch);
						ziel.teleport(loc);
						ziel.sendMessage(sendausgabe("Spawn.Teleportiert"));
						cs.sendMessage((Spawn.get().getConfig().getString("Spawn.Teleportiert2").replaceAll("&", "§"))
								.replace("[Player]", ziel.getDisplayName()));
					} else {
						cs.sendMessage(sendausgabe("Spawn.Nicht gefunden"));
					}
				} else {
					cs.sendMessage(sendausgabe("Spawn.Kein Recht"));
				}
			} else {
				cs.sendMessage(sendausgabe("Spawn.Fehler"));
				return false;
			}
			return true;
		}
		return false;
	}

	public String sendausgabe(String message) {
		return (Spawn.get().getConfig().getString("Spawn.Prefix") + (Spawn.get().getConfig().getString(message)))
				.replaceAll("&", "§");
	}

}
