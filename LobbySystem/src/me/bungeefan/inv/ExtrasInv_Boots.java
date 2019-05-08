package me.bungeefan.inv;

import me.bungeefan.API.ItemAPI;
import me.bungeefan.LobbySystem;

import org.bukkit.DyeColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

public class ExtrasInv_Boots {

	public ExtrasInv_Boots(LobbySystem instance, Player p) {
		Inventory extrasBoots = p.getServer().createInventory(null, 36,
				instance.getConfig().getString("Extras.name").replace("&", "§"));

		ItemStack item = new ItemStack(160, 1);
		item.setData(new Dye(DyeColor.BLACK));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r");
		extrasBoots.setItem(27, ItemAPI.getHead("MHF_ArrowLeft", "§eZurück", 1));
		extrasBoots.setItem(0, item);
		extrasBoots.setItem(1, item);
		extrasBoots.setItem(2, item);
		extrasBoots.setItem(3, item);
		extrasBoots.setItem(4, item);
		extrasBoots.setItem(5, item);
		extrasBoots.setItem(6, item);
		extrasBoots.setItem(7, item);
		extrasBoots.setItem(8, item);
		extrasBoots.setItem(9, item);
		extrasBoots.setItem(10, item);
		extrasBoots.setItem(11, item);
		extrasBoots.setItem(12, item);
		extrasBoots.setItem(13, item);
		extrasBoots.setItem(14, item);
		extrasBoots.setItem(15, item);
		extrasBoots.setItem(16, item);
		extrasBoots.setItem(17, item);
		extrasBoots.setItem(18, item);
		extrasBoots.setItem(19, item);
		extrasBoots.setItem(20, item);
		extrasBoots.setItem(21, item);
		extrasBoots.setItem(22, item);
		extrasBoots.setItem(23, item);
		extrasBoots.setItem(24, item);
		extrasBoots.setItem(25, item);
		extrasBoots.setItem(26, item);

		extrasBoots.setItem(28, item);
		extrasBoots.setItem(29, item);
		extrasBoots.setItem(30, item);
		extrasBoots.setItem(31, item);
		extrasBoots.setItem(32, item);
		extrasBoots.setItem(33, item);
		extrasBoots.setItem(34, item);
		extrasBoots.setItem(35, item);

		ItemStack itemSOON = new ItemStack(54, 1);
		ItemMeta metaSOON = item.getItemMeta();
		metaSOON.setDisplayName("§5Bald verf§gbar!");
		extrasBoots.setItem(13, itemSOON);

		p.openInventory(extrasBoots);
	}
}
