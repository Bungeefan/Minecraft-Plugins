package me.bungeefan.commands;

import java.io.IOException;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ChatFilterCMD extends Command {

	private BungeeSystem instance;

	public ChatFilterCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (cs.hasPermission("bs.chatfilter") || cs.hasPermission("bs.admin")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("list")) {
					cs.sendMessage(instance.prefix + "§cBlockedCMDs:");
					for (String blockedCMD : instance.blockedCMDs) {
						cs.sendMessage(instance.prefix + blockedCMD);
					}
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("add")) {
					instance.blockedCMDs.add(args[1]);
					instance.messages.set("BlockedCMDs.CMDs", instance.blockedCMDs);
					try {
						instance.saveFiles();
					} catch (NullPointerException | IOException e) {
						e.printStackTrace();
					}
					cs.sendMessage(instance.sendausgabe("ChatFilter.Add"));
				} else if (args[0].equalsIgnoreCase("remove")) {
					instance.blockedCMDs.remove(args[1]);
					instance.messages.set("BlockedCMDs.CMDs", instance.blockedCMDs);
					try {
						instance.saveFiles();
					} catch (NullPointerException | IOException e) {
						e.printStackTrace();
					}
					cs.sendMessage(instance.sendausgabe("ChatFilter.Remove"));
				}
			} else {
				cs.sendMessage(instance.sendausgabe("ChatFilter.Usage"));
			}
		}
	}

}
