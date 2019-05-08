package me.bungeefan.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.bungeefan.LobbySystem;
import me.bungeefan.commands.BuildCMD;

public class InventoryLST implements Listener {

	public LobbySystem instance;

	public InventoryLST(LobbySystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (!instance.Builders.contains(p)) {
			e.setCancelled(true);
		}
	}
}
