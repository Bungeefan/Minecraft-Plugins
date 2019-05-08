package me.bungeefan.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.bungeefan.LobbySystem;

public class DropLST implements Listener {

	public LobbySystem instance;
	
	public DropLST(LobbySystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

}
