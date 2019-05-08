package me.bungeefan.commands;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCMD extends Command {

	private BungeeSystem instance;

	public LobbyCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		String lobby = instance.messages.getString("Allgemein.LobbyServer");
		if (cs instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) cs;
			if (args.length == 0) {
				if (!p.getServer().getInfo().getName().contains(lobby)) {
					p.connect(ProxyServer.getInstance().getServerInfo(lobby));
				} else {
					p.sendMessage(instance.sendausgabe("Lobby.InLobby"));
				}
			} else {
				p.sendMessage(instance.sendausgabe("Lobby.Usage"));
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinSpieler"));
		}
	}
}