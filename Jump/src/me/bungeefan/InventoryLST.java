package me.bungeefan;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryLST implements Listener {

	public Jump instance;

	public InventoryLST(Jump instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getName().equals(instance.settings.getName())) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aDoubleJump §aEin§f/§cAus")) {
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(instance.doublejump);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cSounds §aEin§f/§cAus")) {
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(instance.sounds);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§dPartikel §aEin§f/§cAus")) {
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(instance.partikel);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cZurück")) {
				e.setCancelled(true);
				p.closeInventory();
			}
		} else if (e.getInventory().getName().equals(instance.doublejump.getName())) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aEin")) {
				e.setCancelled(true);
				instance.getConfig().set("DoubleJump.aktiviert", true);
				instance.saveConfig();
				p.sendMessage(instance.sendausgabe2("§aDoubleJump wurde aktiviert!"));
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cAus")) {
				e.setCancelled(true);
				instance.getConfig().set("DoubleJump.aktiviert", false);
				instance.saveConfig();
				p.sendMessage(instance.sendausgabe2("§cDoubleJump wurde deaktiviert!"));
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cZurück")) {
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(instance.settings);
			}
		} else if (e.getInventory().getName().equals(instance.sounds.getName())) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aEin")) {
				e.setCancelled(true);
				instance.getConfig().set("JumpPads.Sound.aktiviert", true);
				instance.saveConfig();
				p.sendMessage(instance.sendausgabe2("§cJumpPads Sound wurde aktiviert!"));
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cAus")) {
				e.setCancelled(true);
				instance.getConfig().set("JumpPads.Sound.aktiviert", false);
				instance.saveConfig();
				p.sendMessage(instance.sendausgabe2("§aJumpPads Sound wurde deaktiviert!"));
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cZurück")) {
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(instance.settings);
			}
		} else if (e.getInventory().getName().equals(instance.partikel.getName())) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aEin")) {
				e.setCancelled(true);
				instance.getConfig().set("JumpPads.Partikel.aktiviert", true);
				instance.saveConfig();
				p.sendMessage(instance.sendausgabe2("§cJumpPads Partikel wurde aktiviert!"));
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cAus")) {
				e.setCancelled(true);
				instance.getConfig().set("JumpPads.Partikel.aktiviert", false);
				instance.saveConfig();
				p.sendMessage(instance.sendausgabe2("§aJumpPads Partikel wurde deaktiviert!"));
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cZurück")) {
				e.setCancelled(true);
				p.closeInventory();
				p.openInventory(instance.settings);
			}
		}
	}
}
