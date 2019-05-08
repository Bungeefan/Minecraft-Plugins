package me.bungeefan;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class SkyEasterEggs extends JavaPlugin {

	public Economy econ = null;

	@Override
	public void onEnable() {
		if (setupEconomy() && econ.isEnabled()) {
			saveDefaultConfig();
			getCommand("SkyTerra2017").setExecutor(new CMD(this));
			getCommand("SYTR").setExecutor(new CMD(this));
			getCommand("Halloween").setExecutor(new CMD(this));
			getCommand("Weihnachten").setExecutor(new CMD(this));
			getCommand("Gelesen").setExecutor(new CMD(this));
		} else {
			Bukkit.getServer().getConsoleSender().sendMessage("§c[SkyEasterEggs] Economy Plugin is not enabled!");
		}
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			econ = economyProvider.getProvider();
		}

		return (econ != null);
	}
}
