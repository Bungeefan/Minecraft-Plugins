package me.bungeefan.inv;

import java.util.ArrayList;
import java.util.UUID;
import me.bungeefan.LobbySystem;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Dye;

public class StatsInv {

	public StatsInv(LobbySystem instance, Player p) {
		int size = 36;
		Inventory stats = p.getServer().createInventory(null, size, "§eStatistiken");

		String name = instance.getConfig().getString("Fuellung.name");
		int fuellungid = instance.getConfig().getInt("Fuellung.Item-ID");
		ItemStack fuellung = new ItemStack(fuellungid, 1);
		fuellung.setData(new Dye(DyeColor.BLACK));
		ItemMeta fuellungmeta = fuellung.getItemMeta();
		fuellungmeta.setDisplayName(name);
		fuellung.setItemMeta(fuellungmeta);

		// ArrayList<String> lore1 = new ArrayList();
		// lore1.add("§7Deine Coins: §e" +
		// SQLApi.getCoins(p.getUniqueId().toString()));
		ItemStack itemCOINS = new ItemStack(266, 1);
		ItemMeta metaCOINS = itemCOINS.getItemMeta();
		metaCOINS.setDisplayName("§6Coins-NICHT AKTIV");
		// metaCOINS.setLore(lore1);
		itemCOINS.setItemMeta(metaCOINS);

		ItemStack itemback = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta metaback = (SkullMeta) itemback.getItemMeta();
		metaback.setOwner("MHF_ArrowLeft");
		metaback.setDisplayName("§cZurück");
		itemback.setItemMeta(metaback);

		stats.setItem(27, itemback);
		stats.setItem(13, itemCOINS);
		stats.setItem(23, fuellung);

		for (int j = 0; j < --size; j++) {
			if (stats.getItem(j) == null) {
				stats.setItem(j, fuellung);
			}
		}

		p.openInventory(stats);
	}
}
