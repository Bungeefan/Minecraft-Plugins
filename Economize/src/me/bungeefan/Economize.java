package me.bungeefan;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Economize extends JavaPlugin {

	public boolean eco;

	public void onEnable() {
		loadConfig();
		saveConfig();
		getServer().getConsoleSender()
				.sendMessage(ChatColor.RED + "=======================================================");
		getServer().getConsoleSender()
				.sendMessage(ChatColor.RED + "     [ECONOMIZE] This Plugin is only useful on Bukkit!");
		getServer().getConsoleSender()
				.sendMessage(ChatColor.RED + "=======================================================");
		eco = getConfig().getBoolean("Economize.aktiviert");
		thread();
	}

	public void onDisable() {
		saveConfig();
		try {
			Bukkit.getScheduler().cancelTasks(this);
		} catch (Throwable throwable) {
		}
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("eco")) {
			if (cs.hasPermission("eco.toggle")) {
				if (eco) {
					eco = false;
					getConfig().set("Economize.aktiviert", false);
					cs.sendMessage("§cEconomize wurde deaktiviert");
				} else {
					eco = true;
					getConfig().set("Economize.aktiviert", true);
					cs.sendMessage("§aEconomize wurde aktiviert");
				}
			}
			return true;
		}
		return false;
	}

	public void thread() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			boolean firstRun = true;

			public void run() {
				if ((Bukkit.getServer().getOnlinePlayers().size() == 0) && (eco)) {
					if (firstRun) {
						for (World w : Bukkit.getWorlds()) {
							for (Chunk c : w.getLoadedChunks()) {
								c.unload(true);
							}
						}
						System.gc();
					}
					firstRun = false;
				} else {
					firstRun = true;
				}
			}
		}, 0, 1000);
	}

	private void loadConfig() {
		getConfig().addDefault("Economize.aktiviert", true);
		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();
	}
}
