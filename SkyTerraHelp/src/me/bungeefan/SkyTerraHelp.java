package me.bungeefan;

import org.bukkit.plugin.java.JavaPlugin;

public class SkyTerraHelp extends JavaPlugin {

	@Override
	public void onEnable() {
		saveDefaultConfig();
		getCommand("skyterrahelp").setExecutor(new ReloadCMD(this));
		getCommand("hilfe").setExecutor(new CMD(this, "hilfe"));
		getCommand("skyterra").setExecutor(new CMD(this, "skyterra"));
		getCommand("shophilfe").setExecutor(new CMD(this, "shophilfe"));
		getCommand("team").setExecutor(new CMD(this, "team"));
		getCommand("skybox").setExecutor(new CMD(this, "skybox"));
		getCommand("news").setExecutor(new CMD(this, "news"));
		getCommand("vip").setExecutor(new CMD(this, "vip"));
		getCommand("spenden").setExecutor(new CMD(this, "spenden"));
		getCommand("frage").setExecutor(new CMD(this, "frage"));
		getCommand("ip").setExecutor(new CMD(this, "ip"));
	}
}