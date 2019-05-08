package me.bungeefan;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCMD implements CommandExecutor {

	private SkyTerraHelp instance;

	public ReloadCMD(SkyTerraHelp instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("skyterrahelp") && args.length == 1
				&& cs.hasPermission("skyterrahelp.reload")) {
			if (args[0].equalsIgnoreCase("reload")) {
				cs.sendMessage("§cConfig wird reloaded!");
				instance.reloadConfig();
				cs.sendMessage("§aConfig wurde reloaded!");
				return true;
			}
		}
		return false;
	}

}
