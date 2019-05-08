package me.bungeefan.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.bungeefan.LobbySystem;

public class TeleporterClickLST implements Listener {

	public LobbySystem instance;

	public TeleporterClickLST(LobbySystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getCurrentItem() != null) {
			if (e.getCurrentItem().getItemMeta() != null) {
				String name = instance.getConfig().getString("Teleporter.Name").replace("&", "§");
				if (e.getInventory().getName().equals(name)) {
					e.setCancelled(true);
					String warp = e.getCurrentItem().getItemMeta().getDisplayName();
					if (instance.tp.getString("Warps." + warp) != "") {
						String w = instance.tp.getString("Warps." + warp + ".world");
						double x = instance.tp.getDouble("Warps." + warp + ".x");
						double y = instance.tp.getDouble("Warps." + warp + ".y");
						double z = instance.tp.getDouble("Warps." + warp + ".z");
						float yaw = instance.tp.getInt("Warps." + warp + ".yaw");
						float pitch = instance.tp.getInt("Warp." + warp + ".pitch");

						Location loc = new Location(Bukkit.getWorld(w), x, y, z, yaw, pitch);
						p.teleport(loc);
						p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
						/*
						 * if (instance.getConfig().getBoolean( "options.Teleporter.TeleportMessage")) {
						 * String msg = instance.msg_config.getString( "Teleporter.teleport"); msg =
						 * msg.replace("&", "§"); msg = msg.replace("%warp%",
						 * e.getCurrentItem().getItemMeta().getDisplayName());
						 * p.sendMessage(instance.prefix + msg); }
						 */
					}
				}
			}
		}
	}
}
