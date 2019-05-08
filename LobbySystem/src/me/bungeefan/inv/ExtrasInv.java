package me.bungeefan.inv;

import me.bungeefan.LobbySystem;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

public class ExtrasInv {

	public ExtrasInv(LobbySystem instance, Player p) {
		int size = 36;
		Inventory extras = p.getServer().createInventory(null, size,
				instance.getConfig().getString("Extras.name").replace("&", "§"));

		String name = instance.getConfig().getString("Fuellung.name");
		int fuellungid = instance.getConfig().getInt("Fuellung.Item-ID");
		ItemStack fuellung = new ItemStack(fuellungid, 1);
		fuellung.setData(new Dye(DyeColor.BLACK));
		ItemMeta fuellungmeta = fuellung.getItemMeta();
		fuellungmeta.setDisplayName(name);
		fuellung.setItemMeta(fuellungmeta);

		ItemStack remove = new ItemStack(166, 1);
		ItemMeta removemeta = remove.getItemMeta();
		removemeta.setDisplayName("§cAlles entfernen");
		remove.setItemMeta(removemeta);
		extras.setItem(35, remove);

		ItemStack item1 = new ItemStack(369, 1);
		ItemMeta meta1 = item1.getItemMeta();
		meta1.setDisplayName("§eGadgets");
		item1.setItemMeta(meta1);
		ItemStack item2 = new ItemStack(397, 1);
		ItemMeta meta2 = item2.getItemMeta();
		meta2.setDisplayName("§aKöpfe");
		item2.setItemMeta(meta2);
		ItemStack item3 = new ItemStack(377, 1);
		ItemMeta meta3 = item1.getItemMeta();
		meta3.setDisplayName("§cEffekte");
		item3.setItemMeta(meta3);
		ItemStack item4 = new ItemStack(307, 1);
		ItemMeta meta4 = item4.getItemMeta();
		meta4.setDisplayName("§6Kleiderschrank");
		item4.setItemMeta(meta4);
		ItemStack item5 = new ItemStack(317, 1);
		ItemMeta meta5 = item1.getItemMeta();
		meta5.setDisplayName("§bBoots");
		item5.setItemMeta(meta5);

		extras.setItem(11, item1);
		extras.setItem(13, item2);
		extras.setItem(15, item3);
		extras.setItem(21, item4);
		extras.setItem(23, item5);

		for (int j = 0; j < --size; j++) {
			if (extras.getItem(j) == null) {
				extras.setItem(j, fuellung);
			}
		}

		p.openInventory(extras);
	}
}
