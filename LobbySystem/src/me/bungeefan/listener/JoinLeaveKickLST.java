package me.bungeefan.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.bungeefan.LobbySystem;
import me.bungeefan.API.JoinItems;

public class JoinLeaveKickLST implements Listener {

	public LobbySystem instance;

	public JoinLeaveKickLST(LobbySystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (instance.getConfig().getBoolean("Custom.Join")) {
			e.setJoinMessage(((instance.getConfig().getString("Custom.JoinMessage")).replaceAll("&", "§"))
					.replace("%Player%", p.getDisplayName()));
		}
		p.setFoodLevel(20);
		p.setHealth(20);
		new JoinItems(instance, p);
		if (instance.getConfig().getBoolean("Lobbys.SilentLobby.this")) {
			p.sendMessage(sendausgabe("SilentLobbyJoin"));
			if (!p.hasPermission("lobby.SilentLobby")) {
				p.kickPlayer("");
			}
			for (Player all : Bukkit.getOnlinePlayers()) {
				all.hidePlayer(p);
				p.hidePlayer(all);
			}
		}
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {
		if (instance.getConfig().getBoolean("Custom.Kick")) {
			e.setLeaveMessage(((instance.getConfig().getString("Custom.KickMessage")).replaceAll("&", "§"))
					.replace("%Player%", e.getPlayer().getDisplayName()));
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (instance.getConfig().getBoolean("Custom.Leave")) {
			e.setQuitMessage(((instance.getConfig().getString("Custom.LeaveMessage")).replaceAll("&", "§"))
					.replace("%Player%", e.getPlayer().getDisplayName()));
		}
	}

	public String sendausgabe(String message) {
		return (instance.getConfig().getString("Prefix") + (instance.getConfig().getString(message))).replaceAll("&",
				"§");
	}
}
