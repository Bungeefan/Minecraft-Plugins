package me.bungeefan.inv;

import me.bungeefan.LobbySystem;

import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

public class CompassInv {

	public CompassInv(LobbySystem instance, Player p) {
		Inventory compass;
		int size1 = 9;
		String name = instance.getConfig().getString("Fuellung.name");
		int fuellungid = instance.getConfig().getInt("Fuellung.Item-ID");
		ItemStack fuellung = new ItemStack(fuellungid, 1);
		fuellung.setData(new Dye(DyeColor.BLACK));
		ItemMeta fuellungmeta = fuellung.getItemMeta();
		fuellungmeta.setDisplayName(name);
		fuellung.setItemMeta(fuellungmeta);

		List<String> warps = instance.tp.getStringList("Warps.List");
		for (int i = 0; i < warps.size(); i++) {
			int slot = instance.tp.getInt("Warps." + warps.get(i) + ".slot");
			if (slot >= size1) {
				size1 = slot;
			}
		}
		if ((size1 % 9) > 0) {
			size1 = ((int) size1 / 9) + 1;
		} else {
			size1 = (int) size1 / 9;
		}
		int size = size1 * 9;
		compass = p.getServer().createInventory(null, size, instance.tp.getString("Teleporter.Name").replace("&", "§"));

		for (int i = 0; i < warps.size(); i++) {
			String warp = warps.get(i);
			int slot = instance.tp.getInt("Warps." + warp + ".slot");
			int id = instance.tp.getInt("Warps." + warp + ".Item-ID");
			ItemStack item = new ItemStack(id, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(warp);
			item.setItemMeta(meta);
			compass.setItem(slot, item);
		}

		for (int j = 0; j < --size; j++) {
			if (compass.getItem(j) == null) {
				compass.setItem(j, fuellung);
			}
		}
		p.openInventory(compass);
	}
}
