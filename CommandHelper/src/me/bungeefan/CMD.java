package me.bungeefan;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CMD extends Command {

	private CommandHelper instance;
	private int index;

	public CMD(CommandHelper instance, String name, int index) {
		super(name);
		this.instance = instance;
		this.index = index;
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		cs.sendMessage(instance.cmdlist.get(index + 1).replaceAll("&", "§"));
	}

}
