package me.bungeefan.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.bungeefan.LobbySystem;
import me.bungeefan.API.JoinItems;

public class DeathLST implements Listener {

	public LobbySystem instance;

	public DeathLST(LobbySystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.setDeathMessage("");
		new JoinItems(instance, e.getEntity());
	}
}
