package me.bungeefan.commands;

import java.io.IOException;

import me.bungeefan.ClazeBan;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCMD extends Command {

	public ReloadCMD() {
		super("reload");
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		System.out.println("§cConfig wird reloded!");
		try {
			ClazeBan.get().loadDefaultFiles();
			ClazeBan.get().loadFiles();
			System.out.println("§aConfig wurde reloded!");
		} catch (IOException e) {
			System.out.println("§cConfig konnte nicht geladen werden!");
			ClazeBan.get().getProxy().getPluginManager().getPlugin("ClazeBan").onDisable();
		}
	}

}
