package me.bungeefan;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Iterables;

public class Fly extends JavaPlugin {

	private String console;
	private Collection<? extends Player> onlineplayer;
	private boolean erneut = false;

	public void onEnable() {
		loadConfig();
		saveConfig();
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("fliegen") || label.equalsIgnoreCase("fly")) {
			if (args.length == 0) {
				if (cs instanceof Player) {
					if (cs.hasPermission("fly.toggle")) {
						Player player = (Player) cs;
						if (!player.getAllowFlight()) {
							player.setAllowFlight(true);
							cs.sendMessage(sendausgabe("Fly.aktiviert"));
						} else {
							player.setAllowFlight(false);
							cs.sendMessage(sendausgabe("Fly.deaktiviert"));
						}
					} else {
						cs.sendMessage(sendausgabe("Fly.Kein Recht"));
					}
				} else {
					cs.sendMessage(sendausgabe("Fly.Kein Spieler"));
				}
				return true;
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("all-on")) {
					if (cs.hasPermission("fly.all")) {
						onlineplayer = Bukkit.getOnlinePlayers();
						if (!(onlineplayer.size() == 0)) {
							for (int i = 0; i < onlineplayer.size(); i++) {
								Player player = Iterables.get(onlineplayer, i);
								player.setAllowFlight(true);
							}
							Bukkit.getServer()
									.broadcastMessage((getConfig().getString("Fly.Alle ein").replaceAll("&", "§"))
											.replace("[Player]", cs.getName()));
						} else {
							cs.sendMessage(sendausgabe("Fly.Nicht gefunden"));
						}
					} else {
						cs.sendMessage(sendausgabe("Fly.Kein Recht"));
					}
					return true;
				} else if (args[0].equalsIgnoreCase("all-off")) {
					if (cs.hasPermission("fly.all")) {
						onlineplayer = Bukkit.getOnlinePlayers();
						if (!(onlineplayer.size() == 0)) {
							for (int i = 0; i < onlineplayer.size(); i++) {
								Player player = Iterables.get(onlineplayer, i);
								player.setAllowFlight(false);
							}
							Bukkit.getServer()
									.broadcastMessage((getConfig().getString("Fly.Alle aus").replaceAll("&", "§"))
											.replace("[Player]", cs.getName()));
						} else {
							cs.sendMessage(sendausgabe("Fly.Nicht gefunden"));
						}
					} else {
						cs.sendMessage(sendausgabe("Fly.Kein Recht"));
					}
					return true;
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (cs.hasPermission("fly.reload")) {
						console = (getConfig().getString("Fly.Prefix")).replaceAll("&", "§");
						cs.sendMessage(console + "§cDie Config wird reloaded.");
						reloadConfig();
						saveConfig();
						cs.sendMessage(console + "§aDie Config wurde erfolgreich reloaded.");
					} else {
						cs.sendMessage(sendausgabe("Fly.Kein Recht"));
					}
					return true;
				} else if (args[0].equalsIgnoreCase("deaktivieren")) {
					if (cs.hasPermission("fly.deactivate")) {
						if (erneut) {
							cs.sendMessage((getConfig().getString("Fly.Prefix")).replaceAll("&", "§")
									+ "§cPlugin wurde deaktivert. Um es wieder zu aktiveren, Server neustarten oder reloaden!");
							Bukkit.getPluginManager().disablePlugin(this);
						} else {
							cs.sendMessage(
									"§cBist du dir sicher ?\n§4Das kann nicht rückgängig gemacht werden !\n§cFühre diesen Command erneut aus!");
							erneut = true;
						}
					} else {
						cs.sendMessage(sendausgabe("Fly.Kein Recht"));
					}
					return true;
				} else if (args[0].equalsIgnoreCase("help")) {
					cs.sendMessage((getConfig().getString("Fly.Prefix")).replaceAll("&", "§")
							+ "§eBefehle:§r\n§b/fly [all-on/all-off/reload/help/deaktivieren/Spielername]");
					return true;
				} else if (cs.hasPermission("fly.player")) {
					Player ziel = (Bukkit.getServer().getPlayer(args[0]));
					if (ziel != null) {
						if (!ziel.getAllowFlight()) {
							ziel.setAllowFlight(true);
							ziel.sendMessage(sendausgabe("Fly.aktiviert"));
							cs.sendMessage((getConfig().getString("Fly.Fern ein").replaceAll("&", "§"))
									.replace("[Player]", ziel.getDisplayName()));
						} else {
							ziel.setAllowFlight(false);
							ziel.sendMessage(sendausgabe("Fly.deaktiviert"));
							cs.sendMessage((getConfig().getString("Fly.Fern aus").replaceAll("&", "§"))
									.replace("[Player]", ziel.getDisplayName()));
						}
					} else {
						cs.sendMessage(sendausgabe("Fly.Nicht gefunden"));
					}
				} else {
					cs.sendMessage(sendausgabe("Fly.Kein Recht"));
				}
				return true;
			}
		}
		return false;
	}

	public String sendausgabe(String message) {
		return (getConfig().getString("Fly.Prefix") + (getConfig().getString(message))).replaceAll("&", "§");
	}

	private void loadConfig() {
		getConfig().options().copyDefaults(true);

		saveConfig();
		reloadConfig();
	}

}
