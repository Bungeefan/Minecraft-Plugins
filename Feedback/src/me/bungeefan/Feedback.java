package me.bungeefan;

import org.bukkit.plugin.java.JavaPlugin;

public class Feedback extends JavaPlugin {

	public String prefix;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		prefix = getConfig().getString("Feedback.Prefix").replaceAll("&", "§");
		getCommand("feedback").setExecutor(new FeedbackCMD(this));
		getCommand("feedbacks").setExecutor(new FeedbacksCMD(this));
	}

	@Override
	public void onDisable() {
	}

}
