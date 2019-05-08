package me.bungeefan.listener;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerKickListener implements Listener {

	private BungeeSystem instance;

	public ServerKickListener(BungeeSystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onServerKick(ServerKickEvent e) {
		ProxiedPlayer p = (ProxiedPlayer) e.getPlayer();
		String lobby = instance.messages.getString("Allgemein.LobbyServer");
		if (p.getServer() != null) {
			if (ProxyServer.getInstance().getServerInfo(p.getServer().getInfo().getName()) != ProxyServer.getInstance()
					.getServerInfo(lobby)) {
				e.setCancelled(true);
				e.setCancelServer(ProxyServer.getInstance().getServerInfo(lobby));
				p.sendMessage(e.getKickReason());
			}
		}
	}
}