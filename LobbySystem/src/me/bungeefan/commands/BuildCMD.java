package me.bungeefan.commands;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.bungeefan.LobbySystem;
import me.bungeefan.API.JoinItems;

public class BuildCMD implements CommandExecutor {

	public LobbySystem instance;

	public BuildCMD(LobbySystem instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("build")) {
			if (cs instanceof Player) {
				Player p = (Player) cs;
				if (p.hasPermission("lobby.build")) {
					if (instance.Builders.contains(p.getUniqueId().toString())) {
						instance.Builders.remove(p.getUniqueId().toString());
						p.setGameMode(GameMode.SURVIVAL);
						new JoinItems(instance, p);
						p.sendMessage(sendausgabe("Build.deaktiviert"));
					} else {
						instance.Builders.add(p.getUniqueId().toString());
						p.setGameMode(GameMode.CREATIVE);
						p.sendMessage(sendausgabe("Build.aktiviert"));
					}
				} else {
					cs.sendMessage(sendausgabe("KeinRecht"));
				}
			}
			return true;
		}
		return false;
	}

	public String sendausgabe(String message) {
		return (instance.getConfig().getString("Prefix") + (instance.getConfig().getString(message)))
				.replaceAll("&", "§");
	}

}
