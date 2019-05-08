package me.bungeefan;

import java.io.IOException;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCMD extends Command {

	private CommandHelper instance;

	public ReloadCMD(CommandHelper instance, String name) {
		super(name);
		this.instance = instance;
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("cmdhelper.reload")) {
			try {
				cs.sendMessage("§cConfig wird reloaded!");
				instance.loadConfig();
				instance.registerCommands();
				cs.sendMessage("§aConfig wurde reloaded!");
			} catch (NullPointerException | IOException e) {
				cs.sendMessage("§4Fehler beim Config reloaden");
			}
		}
	}

}
