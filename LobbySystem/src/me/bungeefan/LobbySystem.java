package me.bungeefan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import me.bungeefan.commands.BuildCMD;
import me.bungeefan.commands.FlyCMD;
import me.bungeefan.commands.JumpCMD;
import me.bungeefan.listener.BuildLST;
import me.bungeefan.listener.DamageLST;
import me.bungeefan.listener.DeathLST;
import me.bungeefan.listener.DropLST;
import me.bungeefan.listener.FoodLST;
import me.bungeefan.listener.InteractLST;
import me.bungeefan.listener.JoinLeaveKickLST;
import me.bungeefan.listener.MoveLST;
import me.bungeefan.listener.TeleporterClickLST;
import me.bungeefan.listener.ToggleFlightLST;
import me.bungeefan.listener.WeatherLST;
import me.bungeefan.listener.InventoryLST;

public class LobbySystem extends JavaPlugin {

	public ArrayList<String> HideANDShow = new ArrayList<String>();
	public ArrayList<String> HiderCountdown = new ArrayList<String>();
	public ArrayList<String> NickCountdown = new ArrayList<String>();
	public ArrayList<String> Builders = new ArrayList<String>();
	public Inventory ExtrasSkulls;
	public Inventory ExtrasKleidung;
	public Inventory ExtrasBoots;

	public FileConfiguration msg;
	public FileConfiguration tp;
	public FileConfiguration kopf;
	// public FileConfiguration kleidung = YamlConfiguration.loadConfiguration(new
	// File(getDataFolder(), "dress.yml"));
	// public FileConfiguration pets = YamlConfiguration.loadConfiguration(new
	// File(getDataFolder(), "pets.yml"));

	public void onEnable() {
		saveDefaultConfig();
		createFiles();
		// kleidung.options().copyDefaults();
		// pets.options().copyDefaults();
		getCommand("fly").setExecutor(new FlyCMD(this));
		// getCommand("fliegen").setExecutor(new FlyCMD(this));
		getCommand("build").setExecutor(new BuildCMD(this));
		getCommand("jump").setExecutor(new JumpCMD(this));
		getCommand("jumppads").setExecutor(new JumpCMD(this));

		Bukkit.getPluginManager().registerEvents(new BuildLST(this), this);
		Bukkit.getPluginManager().registerEvents(new DamageLST(this), this);
		Bukkit.getPluginManager().registerEvents(new DeathLST(this), this);
		Bukkit.getPluginManager().registerEvents(new DropLST(this), this);
		Bukkit.getPluginManager().registerEvents(new FoodLST(this), this);
		Bukkit.getPluginManager().registerEvents(new InteractLST(this), this);
		Bukkit.getPluginManager().registerEvents(new InventoryLST(this), this);
		Bukkit.getPluginManager().registerEvents(new JoinLeaveKickLST(this), this);
		Bukkit.getPluginManager().registerEvents(new MoveLST(this), this);
		Bukkit.getPluginManager().registerEvents(new TeleporterClickLST(this), this);
		Bukkit.getPluginManager().registerEvents(new ToggleFlightLST(this), this);
		Bukkit.getPluginManager().registerEvents(new WeatherLST(this), this);
	}

	private void createFiles() {

		File msgf = new File(getDataFolder(), "messages.yml");
		File tpf = new File(getDataFolder(), "tp.yml");
		File kopff = new File(getDataFolder(), "heads.yml");

		if (!msgf.exists()) {
			msgf.getParentFile().mkdirs();
			saveResource("messages.yml", false);
		}
		if (!tpf.exists()) {
			tpf.getParentFile().mkdirs();
			saveResource("tp.yml", false);
		}
		if (!kopff.exists()) {
			kopff.getParentFile().mkdirs();
			saveResource("heads.yml", false);
		}

		msg = new YamlConfiguration();
		tp = new YamlConfiguration();
		kopf = new YamlConfiguration();
		try {
			msg.load(msgf);
			tp.load(tpf);
			kopf.load(kopff);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

}
