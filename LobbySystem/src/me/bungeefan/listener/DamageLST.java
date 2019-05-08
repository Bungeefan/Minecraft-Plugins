package me.bungeefan.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.bungeefan.LobbySystem;

public class DamageLST implements Listener {

	public LobbySystem instance;

	public DamageLST(LobbySystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onDamageEntity(EntityDamageEvent e) {
		e.setCancelled(true);
	}

}
