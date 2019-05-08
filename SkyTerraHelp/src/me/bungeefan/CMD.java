package me.bungeefan;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CMD implements CommandExecutor {

	private SkyTerraHelp instance;
	private String name;

	public CMD(SkyTerraHelp instance, String name) {
		this.instance = instance;
		this.name = name;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase(name)) {
			cs.sendMessage(instance.getConfig().getString("SkyTerraHelp." + name + ".Message").replaceAll("&", "§"));
			return true;
		}
		return false;
	}

}
