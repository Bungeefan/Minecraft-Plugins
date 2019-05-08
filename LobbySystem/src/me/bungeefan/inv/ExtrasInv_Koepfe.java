package me.bungeefan.inv;

import me.bungeefan.LobbySystem;
import me.bungeefan.API.ItemAPI;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

public class ExtrasInv_Koepfe {

	public ExtrasInv_Koepfe(LobbySystem instance, Player p) {
		Inventory extrasSkulls = p.getServer().createInventory(null, 36,
				instance.getConfig().getString("Extras.name").replace("&", "§"));

		ItemStack item = new ItemStack(160, 1);
		item.setData(new Dye(DyeColor.BLACK));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r");
		extrasSkulls.setItem(27, ItemAPI.getHead("MHF_ArrowLeft", "§eZurück", 1));

		extrasSkulls.setItem(0, item);
		extrasSkulls.setItem(1, item);
		extrasSkulls.setItem(2, item);
		extrasSkulls.setItem(3, item);
		extrasSkulls.setItem(4, item);
		extrasSkulls.setItem(5, item);
		extrasSkulls.setItem(6, item);
		extrasSkulls.setItem(7, item);
		extrasSkulls.setItem(8, item);
		extrasSkulls.setItem(9, item);
		extrasSkulls.setItem(10, item);
		extrasSkulls.setItem(11, item);
		extrasSkulls.setItem(12, item);
		extrasSkulls.setItem(13, item);
		extrasSkulls.setItem(14, item);
		extrasSkulls.setItem(15, item);
		extrasSkulls.setItem(16, item);
		extrasSkulls.setItem(17, item);
		extrasSkulls.setItem(18, item);
		extrasSkulls.setItem(19, item);
		extrasSkulls.setItem(20, item);
		extrasSkulls.setItem(21, item);
		extrasSkulls.setItem(22, item);
		extrasSkulls.setItem(23, item);
		extrasSkulls.setItem(24, item);
		extrasSkulls.setItem(25, item);
		extrasSkulls.setItem(26, item);

		extrasSkulls.setItem(28, item);
		extrasSkulls.setItem(29, item);
		extrasSkulls.setItem(30, item);
		extrasSkulls.setItem(31, item);
		extrasSkulls.setItem(32, item);
		extrasSkulls.setItem(33, item);
		extrasSkulls.setItem(34, item);
		extrasSkulls.setItem(35, item);
		String kopf1 = instance.kopf.getString("Kopf.1.Name");
		String kopf2 = instance.kopf.getString("Kopf.2.Name");
		String kopf3 = instance.kopf.getString("Kopf.3.Name");
		String kopf4 = instance.kopf.getString("Kopf.4.Name");
		String kopf5 = instance.kopf.getString("Kopf.5.Name");
		String kopf6 = instance.kopf.getString("Kopf.6.Name");
		String kopf7 = instance.kopf.getString("Kopf.7.Name");
		String kopf8 = instance.kopf.getString("Kopf.8.Name");
		String kopf9 = instance.kopf.getString("Kopf.9.Name");
		String kopf10 = instance.kopf.getString("Kopf.10.Name");
		String kopf11 = instance.kopf.getString("Kopf.11.Name");
		String kopf12 = instance.kopf.getString("Kopf.12.Name");
		String kopf13 = instance.kopf.getString("Kopf.13.Name");
		String kopf14 = instance.kopf.getString("Kopf.14.Name");
		String kopf15 = instance.kopf.getString("Kopf.15.Name");

		extrasSkulls.setItem(2, ItemAPI.getHead(kopf1, "§7Kopf von §e" + kopf1, 1));
		extrasSkulls.setItem(3, ItemAPI.getHead(kopf2, "§7Kopf von §e" + kopf2, 1));
		extrasSkulls.setItem(4, ItemAPI.getHead(kopf3, "§7Kopf von §e" + kopf3, 1));
		extrasSkulls.setItem(5, ItemAPI.getHead(kopf4, "§7Kopf von §e" + kopf4, 1));
		extrasSkulls.setItem(6, ItemAPI.getHead(kopf5, "§7Kopf von §e" + kopf5, 1));
		extrasSkulls.setItem(11, ItemAPI.getHead(kopf6, "§7Kopf von §e" + kopf6, 1));
		extrasSkulls.setItem(12, ItemAPI.getHead(kopf7, "§7Kopf von §e" + kopf7, 1));
		extrasSkulls.setItem(13, ItemAPI.getHead(kopf8, "§7Kopf von §e" + kopf8, 1));
		extrasSkulls.setItem(14, ItemAPI.getHead(kopf9, "§7Kopf von §e" + kopf9, 1));
		extrasSkulls.setItem(15, ItemAPI.getHead(kopf10, "§7Kopf von §e" + kopf10, 1));
		extrasSkulls.setItem(20, ItemAPI.getHead(kopf11, "§7Kopf von §e" + kopf11, 1));
		extrasSkulls.setItem(21, ItemAPI.getHead(kopf12, "§7Kopf von §e" + kopf12, 1));
		extrasSkulls.setItem(22, ItemAPI.getHead(kopf13, "§7Kopf von §e" + kopf13, 1));
		extrasSkulls.setItem(23, ItemAPI.getHead(kopf14, "§7Kopf von §e" + kopf14, 1));
		extrasSkulls.setItem(24, ItemAPI.getHead(kopf15, "§7Kopf von §e" + kopf15, 1));
		p.openInventory(extrasSkulls);
	}
}
