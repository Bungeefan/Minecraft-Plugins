package me.bungeefan.inv;

import me.bungeefan.LobbySystem;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

public class HideShowPlayerInv {

	public static Inventory HideShowPlayers;

	public static void createInv(Player p) {
		HideShowPlayers = p.getServer().createInventory(null, 9, "§7Spieler verstecken");

		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		item.setData(new Dye(DyeColor.BLACK));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r");
		item.setItemMeta(meta);

		ItemStack item1 = new ItemStack(Material.getMaterial(351), 1);
		item1.setData(new Dye(DyeColor.GREEN));
		ItemMeta meta1 = item1.getItemMeta();
		meta1.setDisplayName("§aAlle Spieler anzeigen");
		item1.setItemMeta(meta1);

		ItemStack item2 = new ItemStack(Material.getMaterial(351), 1);
		item2.setData(new Dye(DyeColor.PURPLE));
		ItemMeta meta2 = item2.getItemMeta();
		meta2.setDisplayName("§5Nur VIPs anzeigen");
		item2.setItemMeta(meta2);

		ItemStack item3 = new ItemStack(Material.getMaterial(351), 1);
		item3.setData(new Dye(DyeColor.GRAY));
		ItemMeta meta3 = item3.getItemMeta();
		meta3.setDisplayName("§8Keine Spieler anzeigen");
		item3.setItemMeta(meta3);

		HideShowPlayers.setItem(0, item1);
		HideShowPlayers.setItem(4, item2);
		HideShowPlayers.setItem(8, item3);

		p.openInventory(HideShowPlayers);
	}
}
