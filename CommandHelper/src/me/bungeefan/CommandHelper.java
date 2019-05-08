package me.bungeefan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import me.bungeefan.CMD;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class CommandHelper extends Plugin {

	private File configfile;
	public Configuration config;
	public List<String> cmdlist;

	@Override
	public void onEnable() {
		try {
			loadDefaultFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cmdlist = config.getStringList("Commands");
		registerCommands();
	}

	@Override
	public void onDisable() {

	}

	public void registerCommands() {
		getProxy().getPluginManager().registerCommand(this, new ReloadCMD(this, "cmdhelper"));
		for (int i = 0; i < cmdlist.size(); i += 2) {
			getProxy().getPluginManager().registerCommand(this, new CMD(this, cmdlist.get(i), i));
		}
	}

	public void loadDefaultFiles() throws IOException {
		if (!getDataFolder().exists())
			getDataFolder().mkdir();

		configfile = new File(getDataFolder(), "config.yml");

		if (!configfile.exists()) {
			try (InputStream in = getResourceAsStream("config.yml")) {
				Files.copy(in, configfile.toPath());
			} catch (IOException | NullPointerException e) {
				BungeeCord.getInstance().getConsole().sendMessage("SYSTEM: §4Config (config.yml) Fehler");
				throw new IOException();
			}
		}

		try {
			loadConfig();
		} catch (IOException | NullPointerException e) {
			BungeeCord.getInstance().getConsole().sendMessage("SYSTEM: §4Allgemeiner Config Fehler");
			throw new IOException();
		}
	}

	public void loadConfig() throws IOException, NullPointerException {
		if (configfile.exists()) {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configfile);
			cmdlist = config.getStringList("Commands");
		}
	}

	public void saveConfig() throws IOException, NullPointerException {
		if (configfile.exists()) {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
		}
	}

}
