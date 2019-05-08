package me.bungeefan;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Dye;

public class SoundsInv {

	private Jump instance;

	public SoundsInv(Jump instance) {
		this.instance = instance;
	}

	public Inventory getInv() {
		int size = 27;
		Inventory sounds = Bukkit.createInventory(null, size, "§cSounds");

		String name = "§r";
		ItemStack fuellung = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) DyeColor.BLACK.getDyeData());
		ItemMeta fuellungmeta = fuellung.getItemMeta();
		fuellungmeta.setDisplayName(name);
		fuellung.setItemMeta(fuellungmeta);

		ItemStack itemback = new ItemStack(Material.SKULL_ITEM);
		SkullMeta metaback = (SkullMeta) itemback.getItemMeta();
		metaback.setOwner("MHF_ArrowLeft");
		metaback.setDisplayName("§cZurück");
		itemback.setItemMeta(metaback);

		sounds.setItem(18, itemback);

		ItemStack ein = new ItemStack(Material.WOOL, 1, (short) DyeColor.GREEN.getDyeData());
		ItemMeta einmeta = ein.getItemMeta();
		einmeta.setDisplayName("§aEin");
		ein.setItemMeta(einmeta);

		sounds.setItem(12, ein);

		ItemStack aus = new ItemStack(Material.WOOL, 1, (short) DyeColor.RED.getDyeData());
		ItemMeta ausmeta = aus.getItemMeta();
		ausmeta.setDisplayName("§cAus");
		aus.setItemMeta(ausmeta);

		sounds.setItem(14, aus);

		for (int j = 0; j < --size; j++) {
			if (sounds.getItem(j) == null) {
				sounds.setItem(j, fuellung);
			}
		}
		return sounds;
	}

}
