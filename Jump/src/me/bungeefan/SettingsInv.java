package me.bungeefan;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SettingsInv {

	private Jump instance;

	public SettingsInv(Jump instance) {
		this.instance = instance;
	}

	public Inventory getInv() {
		int size = 27;
		Inventory settings = Bukkit.createInventory(null, size, "§cEinstellungen");

		String name = "§r";
		ItemStack fuellung = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) DyeColor.BLACK.getDyeData());
		ItemMeta fuellungmeta = fuellung.getItemMeta();
		fuellungmeta.setDisplayName(name);
		fuellung.setItemMeta(fuellungmeta);

		ItemStack itemback = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta metaback = (SkullMeta) itemback.getItemMeta();
		metaback.setOwner("MHF_ArrowLeft");
		metaback.setDisplayName("§cZurück");
		itemback.setItemMeta(metaback);

		settings.setItem(18, itemback);

		ItemStack jump = new ItemStack(Material.GOLD_PLATE);
		ItemMeta jumpmeta = jump.getItemMeta();
		jumpmeta.setDisplayName("§aDoubleJump §aEin§f/§cAus");
		jump.setItemMeta(jumpmeta);

		settings.setItem(11, jump);

		ItemStack sound = new ItemStack(Material.NOTE_BLOCK);
		ItemMeta soundmeta = sound.getItemMeta();
		soundmeta.setDisplayName("§cSounds §aEin§f/§cAus");
		sound.setItemMeta(soundmeta);

		settings.setItem(13, sound);

		ItemStack partikel = new ItemStack(Material.NOTE_BLOCK);
		ItemMeta partikelmeta = partikel.getItemMeta();
		partikelmeta.setDisplayName("§dPartikel §aEin§f/§cAus");
		partikel.setItemMeta(partikelmeta);

		settings.setItem(15, partikel);

		for (int j = 0; j < --size; j++) {
			if (settings.getItem(j) == null) {
				settings.setItem(j, fuellung);
			}
		}
		return settings;
	}

}
