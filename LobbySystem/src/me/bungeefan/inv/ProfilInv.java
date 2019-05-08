package me.bungeefan.inv;

import me.bungeefan.LobbySystem;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Dye;

public class ProfilInv {

	public ProfilInv(LobbySystem instance, Player p) {
		int size = 36;
		Inventory profil = p.getServer().createInventory(null, size, "§5Profil");
		String name = instance.getConfig().getString("Fuellung.name");
		int fuellungid = instance.getConfig().getInt("Fuellung.Item-ID");
		ItemStack fuellung = new ItemStack(fuellungid, 1);
		fuellung.setData(new Dye(DyeColor.BLACK));
		ItemMeta fuellungmeta = fuellung.getItemMeta();
		fuellungmeta.setDisplayName(name);
		fuellung.setItemMeta(fuellungmeta);

		ItemStack itemOPT = new ItemStack(356, 1);
		ItemMeta metaOPT = itemOPT.getItemMeta();
		metaOPT.setDisplayName("§cEinstellungen");
		itemOPT.setItemMeta(metaOPT);

		ItemStack itemSTATS = new ItemStack(54, 1);
		ItemMeta metaSTATS = itemSTATS.getItemMeta();
		metaSTATS.setDisplayName("§eStatistiken");
		itemSTATS.setItemMeta(metaSTATS);

		ItemStack itemF = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta metaF = (SkullMeta) itemF.getItemMeta();
		metaF.setOwner(p.getName());
		metaF.setDisplayName("§5Freunde");
		itemF.setItemMeta(metaF);

		profil.setItem(13, itemF);
		profil.setItem(11, itemOPT);
		profil.setItem(15, itemSTATS);

		for (int j = 0; j < --size; j++) {
			if (profil.getItem(j) == null) {
				profil.setItem(j, fuellung);
			}
		}
		p.openInventory(profil);
	}
}
