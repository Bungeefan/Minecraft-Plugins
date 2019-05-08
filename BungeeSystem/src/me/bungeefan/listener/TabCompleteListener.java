package me.bungeefan.listener;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TabCompleteListener implements Listener {

	private BungeeSystem instance;

	public TabCompleteListener(BungeeSystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onTabComplete(TabCompleteEvent e) {
		/*
		 * String[] args = e.getCursor().split(" ");
		 * 
		 * String checked = (args.length > 0 ? args[args.length - 1] :
		 * e.getCursor()).toLowerCase();
		 * 
		 * if (!e.getSuggestions().isEmpty() | !e.getCursor().startsWith("/")) { return;
		 * }
		 * 
		 * for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) { if
		 * (players.getName().toLowerCase().startsWith(checked)) {
		 * e.getSuggestions().add(players.getName()); } }
		 */
	}
}