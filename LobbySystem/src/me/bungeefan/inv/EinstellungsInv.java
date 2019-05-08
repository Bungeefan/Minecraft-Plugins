package me.bungeefan.inv;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Dye;

import me.bungeefan.LobbySystem;

public class EinstellungsInv {

	public EinstellungsInv(LobbySystem instance, Player p) {
		int size = 36;
		Inventory settings = p.getServer().createInventory(null, size, "§cEinstellungen");

		String name = instance.getConfig().getString("Fuellung.name");
		int fuellungid = instance.getConfig().getInt("Fuellung.Item-ID");
		ItemStack fuellung = new ItemStack(fuellungid, 1);
		fuellung.setData(new Dye(DyeColor.BLACK));
		ItemMeta fuellungmeta = fuellung.getItemMeta();
		fuellungmeta.setDisplayName(name);
		fuellung.setItemMeta(fuellungmeta);

		ItemStack itemback = new ItemStack(Material.SKULL_ITEM, 1);
		SkullMeta metaback = (SkullMeta) itemback.getItemMeta();
		metaback.setOwner("MHF_ArrowLeft");
		metaback.setDisplayName("§cZurück");
		itemback.setItemMeta(metaback);

		settings.setItem(27, itemback);

		for (int j = 0; j < --size; j++) {
			if (settings.getItem(j) == null) {
				settings.setItem(j, fuellung);
			}
		}
		p.openInventory(settings);
	}
}
