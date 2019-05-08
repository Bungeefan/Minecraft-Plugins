package me.bungeefan.commands;

import java.util.UUID;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanlistCMD extends Command {

	public ProxiedPlayer player;
	public UUID uuid;
	public int dauer = 0;
	public String grund = "";

	public BanlistCMD() {
		super("banlist");
	}

	@Override
	public void execute(CommandSender cs, String[] args) {

	}

}
