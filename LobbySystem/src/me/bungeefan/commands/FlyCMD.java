package me.bungeefan.commands;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Iterables;

import me.bungeefan.LobbySystem;

public class FlyCMD implements CommandExecutor {

	public LobbySystem instance;
	private Collection<? extends Player> onlineplayer;
	
	public FlyCMD(LobbySystem instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("fly") || label.equalsIgnoreCase("fliegen")) {
			if (args.length == 0) {
				if (cs instanceof Player) {
					if (cs.hasPermission("lobby.fly")) {
						Player player = (Player) cs;
						if (!player.getAllowFlight()) {
							player.setAllowFlight(true);
							cs.sendMessage(sendausgabe("Fly.aktiviert"));
						} else {
							player.setAllowFlight(false);
							cs.sendMessage(sendausgabe("Fly.deaktiviert"));
						}
					} else {
						cs.sendMessage(sendausgabe("KeinRecht"));
					}
				} else {
					cs.sendMessage(sendausgabe("KeinSpieler"));
				}
				return true;
			} else if (args.length == 1) {
				if (cs.hasPermission("lobby.fly.all")) {
					if (args[0].equalsIgnoreCase("all-on")) {
						onlineplayer = Bukkit.getOnlinePlayers();
						if (!(onlineplayer.size() == 0)) {
							for (int i = 0; i < onlineplayer.size(); i++) {
								Player player = Iterables.get(onlineplayer, i);
								player.setAllowFlight(true);
							}
							Bukkit.getServer().broadcastMessage(
									(instance.getConfig().getString("Fly.Alle-ein").replaceAll("&", "§"))
											.replace("%Player%", cs.getName()));
						} else {
							cs.sendMessage(sendausgabe("Nichtgefunden"));
						}
						return true;
					} else if (args[0].equalsIgnoreCase("all-off")) {
						onlineplayer = Bukkit.getOnlinePlayers();
						if (!(onlineplayer.size() == 0)) {
							for (int i = 0; i < onlineplayer.size(); i++) {
								Player player = Iterables.get(onlineplayer, i);
								player.setAllowFlight(false);
							}
							Bukkit.getServer().broadcastMessage(
									(instance.getConfig().getString("Fly.Alle-aus").replaceAll("&", "§"))
											.replace("[Player]", cs.getName()));
						} else {
							cs.sendMessage(sendausgabe("Nichtgefunden"));
						}
						return true;
					} else {
						cs.sendMessage(sendausgabe("KeinRecht"));
					}
				} else if (cs.hasPermission("lobby.fly.player")) {
					Player ziel = (Bukkit.getServer().getPlayer(args[0]));
					if (ziel != null) {
						if (!ziel.getAllowFlight()) {
							ziel.setAllowFlight(true);
							ziel.sendMessage(sendausgabe("Fly.aktiviert"));
							cs.sendMessage(
									(instance.getConfig().getString("Fly.Spieler-ein").replaceAll("&", "§"))
											.replace("%Player%", ziel.getDisplayName()));
						} else {
							ziel.setAllowFlight(false);
							ziel.sendMessage(sendausgabe("Fly.deaktiviert"));
							cs.sendMessage(
									(instance.getConfig().getString("Fly.Spieler-aus").replaceAll("&", "§"))
											.replace("%Player%", ziel.getDisplayName()));
						}
					} else {
						cs.sendMessage(sendausgabe("Nichtgefunden"));
					}
				} else {
					cs.sendMessage(sendausgabe("KeinRecht"));
				}
				return true;
			}
		}
		return false;
	}

	public String sendausgabe(String message) {
		return (instance.getConfig().getString("Prefix") + (instance.getConfig().getString(message)))
				.replaceAll("&", "§");
	}

}
