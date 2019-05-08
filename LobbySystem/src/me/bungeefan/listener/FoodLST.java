package me.bungeefan.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import me.bungeefan.LobbySystem;

public class FoodLST implements Listener {
	
	public LobbySystem instance;
	
	public FoodLST(LobbySystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		Player p = (Player) e.getEntity();
		p.setFoodLevel(20);
		e.setCancelled(true);
	}

}
