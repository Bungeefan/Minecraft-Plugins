package me.bungeefan.listener;

import me.bungeefan.BungeeSystem;
import me.bungeefan.MySQL;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectedListener implements Listener {

	private BungeeSystem instance;

	public ServerConnectedListener(BungeeSystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onServerConnected(ServerConnectedEvent e) {
		ProxiedPlayer p = (ProxiedPlayer) e.getPlayer();
		instance.joinMe.remove(p);
	}
}