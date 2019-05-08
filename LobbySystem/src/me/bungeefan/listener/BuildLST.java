package me.bungeefan.listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.bungeefan.LobbySystem;
import me.bungeefan.commands.BuildCMD;

public class BuildLST implements Listener {
	
	public LobbySystem instance;
	
	public BuildLST(LobbySystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (!instance.Builders.contains(e.getPlayer())) {
			e.setCancelled(true);
		} else {
			Player p = e.getPlayer();
			Location location = e.getBlock().getLocation();
			String koordinaten = location.getWorld().getName() + "/" + location.getBlockX() + "/" + location.getBlockY()
					+ "/" + location.getBlockZ();
			List<String> platelocs = instance.getConfig().getStringList("JumpPads.Orte");
			if ((e.getBlock().getType() == Material
					.getMaterial(instance.getConfig().getString("JumpPads.Plate")))
					&& (platelocs.contains(koordinaten))
					&& (instance.getConfig().getBoolean("JumpPads.ändern"))) {
				platelocs.remove(koordinaten);
				instance.getConfig().set("JumpPads.Orte", platelocs);
				instance.saveConfig();
				p.sendMessage(sendausgabe("JumpPads.Entfernt"));
			} else if ((e.getBlock().getType() == Material
					.getMaterial(instance.getConfig().getString("JumpPads.Plate")))
					&& (platelocs.contains(koordinaten))
					|| (!instance.getConfig().getBoolean("JumpPads.ändern"))) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (!instance.Builders.contains(e.getPlayer())) {
			e.setCancelled(true);
		} else if ((e.getBlock().getType() == Material
				.getMaterial(instance.getConfig().getString("JumpPads.Plate")))
				&& (instance.getConfig().getBoolean("JumpPads.ändern"))) {
			Player p = e.getPlayer();
			Location location = e.getBlock().getLocation();
			String koordinaten = location.getWorld().getName() + "/" + location.getBlockX() + "/" + location.getBlockY()
					+ "/" + location.getBlockZ();
			p.sendMessage(sendausgabe("JumpPads.Gesetzt"));
			List<String> platelocs = instance.getConfig().getStringList("JumpPads.Orte");
			if (!platelocs.contains(koordinaten)) {
				platelocs.add(koordinaten);
				instance.getConfig().set("JumpPads.Orte", platelocs);

			}
			instance.saveConfig();
		}
	}

	public String sendausgabe(String message) {
		return (instance.getConfig().getString("Prefix") + (instance.getConfig().getString(message)))
				.replaceAll("&", "§");
	}
}
