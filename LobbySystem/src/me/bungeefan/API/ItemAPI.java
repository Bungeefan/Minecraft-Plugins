package me.bungeefan.API;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemAPI {

	public static ItemStack getHead(String PlayerName, String name, int amount) {
		ItemStack itemstack = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
		SkullMeta meta = (SkullMeta) itemstack.getItemMeta();
		meta.setOwner(PlayerName);
		meta.setDisplayName(name);
		itemstack.setItemMeta(meta);
		return itemstack;
	}
}
