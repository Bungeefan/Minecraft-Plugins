package me.bungeefan;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class PartikelInv {

	private Jump instance;

	public PartikelInv(Jump instance) {
		this.instance = instance;
	}

	public Inventory getInv() {
		int size = 27;
		Inventory partikel = Bukkit.createInventory(null, size, "§cPartikel");

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

		partikel.setItem(18, itemback);

		ItemStack ein = new ItemStack(Material.WOOL, 1, (short) DyeColor.GREEN.getDyeData());
		ItemMeta einmeta = ein.getItemMeta();
		einmeta.setDisplayName("§aEin");
		ein.setItemMeta(einmeta);

		partikel.setItem(12, ein);

		ItemStack aus = new ItemStack(Material.WOOL, 1, (short) DyeColor.RED.getDyeData());
		ItemMeta ausmeta = aus.getItemMeta();
		ausmeta.setDisplayName("§cAus");
		aus.setItemMeta(ausmeta);

		partikel.setItem(14, aus);

		for (int j = 0; j < --size; j++) {
			if (partikel.getItem(j) == null) {
				partikel.setItem(j, fuellung);
			}
		}
		return partikel;
	}

}
