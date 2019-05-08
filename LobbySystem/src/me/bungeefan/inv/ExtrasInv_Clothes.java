package me.bungeefan.inv;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

import me.bungeefan.LobbySystem;
import me.bungeefan.API.ItemAPI;

public class ExtrasInv_Clothes {

	public ExtrasInv_Clothes(LobbySystem instance, Player p) {
		Inventory extrasClothes = p.getServer().createInventory(null, 36,
				instance.getConfig().getString("Extras.name").replace("&", "§"));

		ItemStack item = new ItemStack(160, 1);
		item.setData(new Dye(DyeColor.BLACK));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r");
		extrasClothes.setItem(27, ItemAPI.getHead("MHF_ArrowLeft", "§eZurück", 1));
		extrasClothes.setItem(0, item);
		extrasClothes.setItem(1, item);
		extrasClothes.setItem(2, item);
		extrasClothes.setItem(3, item);
		extrasClothes.setItem(4, item);
		extrasClothes.setItem(5, item);
		extrasClothes.setItem(6, item);
		extrasClothes.setItem(7, item);
		extrasClothes.setItem(8, item);
		extrasClothes.setItem(9, item);
		extrasClothes.setItem(10, item);
		extrasClothes.setItem(11, item);
		extrasClothes.setItem(12, item);
		extrasClothes.setItem(13, item);
		extrasClothes.setItem(14, item);
		extrasClothes.setItem(15, item);
		extrasClothes.setItem(16, item);
		extrasClothes.setItem(17, item);
		extrasClothes.setItem(18, item);
		extrasClothes.setItem(19, item);
		extrasClothes.setItem(20, item);
		extrasClothes.setItem(21, item);
		extrasClothes.setItem(22, item);
		extrasClothes.setItem(23, item);
		extrasClothes.setItem(24, item);
		extrasClothes.setItem(25, item);
		extrasClothes.setItem(26, item);

		extrasClothes.setItem(28, item);
		extrasClothes.setItem(29, item);
		extrasClothes.setItem(30, item);
		extrasClothes.setItem(31, item);
		extrasClothes.setItem(32, item);
		extrasClothes.setItem(33, item);
		extrasClothes.setItem(34, item);
		extrasClothes.setItem(35, item);
		ItemStack item4 = new ItemStack(Material.DIAMOND_HELMET);
		ItemMeta meta4 = item4.getItemMeta();
		meta4.setDisplayName("§bDiamant Helm");
		item4.setItemMeta(meta4);

		ItemStack item5 = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemMeta meta5 = item5.getItemMeta();
		meta5.setDisplayName("§bDiamant Brustplatte");
		item5.setItemMeta(meta5);

		ItemStack item6 = new ItemStack(Material.DIAMOND_LEGGINGS);
		ItemMeta meta6 = item6.getItemMeta();
		meta6.setDisplayName("§bDiamant Hose");
		item6.setItemMeta(meta6);

		ItemStack item7 = new ItemStack(Material.DIAMOND_BOOTS);
		ItemMeta meta7 = item7.getItemMeta();
		meta7.setDisplayName("§bDiamant Schuhe");
		item7.setItemMeta(meta7);

		ItemStack item8 = new ItemStack(Material.GOLD_HELMET);
		ItemMeta meta8 = item8.getItemMeta();
		meta8.setDisplayName("§6Gold Helm");
		item8.setItemMeta(meta8);

		ItemStack item9 = new ItemStack(Material.GOLD_CHESTPLATE);
		ItemMeta meta9 = item9.getItemMeta();
		meta9.setDisplayName("§6Gold Brustplatte");
		item9.setItemMeta(meta9);

		ItemStack item10 = new ItemStack(Material.GOLD_LEGGINGS);
		ItemMeta meta10 = item10.getItemMeta();
		meta10.setDisplayName("§6Gold Hose");
		item10.setItemMeta(meta10);

		ItemStack item11 = new ItemStack(Material.GOLD_BOOTS);
		ItemMeta meta11 = item11.getItemMeta();
		meta11.setDisplayName("§6Gold Schuhe");
		item11.setItemMeta(meta11);

		ItemStack einsen1 = new ItemStack(306, 1);
		meta = einsen1.getItemMeta();
		meta.setDisplayName("§7Eisen Helm");
		einsen1.setItemMeta(meta);
		ItemStack einsen2 = new ItemStack(307, 1);
		meta = einsen1.getItemMeta();
		meta.setDisplayName("§7Eisen Brustplatte");
		einsen1.setItemMeta(meta);
		ItemStack einsen3 = new ItemStack(308, 1);
		meta = einsen1.getItemMeta();
		meta.setDisplayName("§7Eisen Hose");
		einsen1.setItemMeta(meta);
		ItemStack einsen4 = new ItemStack(309, 1);
		meta = einsen1.getItemMeta();
		meta.setDisplayName("§7Eisen Schuhe");
		einsen1.setItemMeta(meta);

		ItemStack ketten1 = new ItemStack(302, 1);
		meta = einsen1.getItemMeta();
		meta.setDisplayName("§7Ketten Helm");
		einsen1.setItemMeta(meta);
		ItemStack ketten2 = new ItemStack(303, 1);
		meta = einsen1.getItemMeta();
		meta.setDisplayName("§7Ketten Brustplatte");
		einsen1.setItemMeta(meta);
		ItemStack ketten3 = new ItemStack(304, 1);
		meta = einsen1.getItemMeta();
		meta.setDisplayName("§7Ketten Hose");
		einsen1.setItemMeta(meta);
		ItemStack ketten4 = new ItemStack(305, 1);
		meta = einsen1.getItemMeta();
		meta.setDisplayName("§7Ketten Schuhe");
		einsen1.setItemMeta(meta);

		ItemStack leder1 = new ItemStack(298, 1);
		meta = einsen1.getItemMeta();
		meta.setDisplayName("§8Leder Helm");
		einsen1.setItemMeta(meta);
		ItemStack leder2 = new ItemStack(299, 1);
		meta = einsen1.getItemMeta();
		meta.setDisplayName("§8Leder Brustplatte");
		einsen1.setItemMeta(meta);
		ItemStack leder3 = new ItemStack(300, 1);
		meta = einsen1.getItemMeta();
		meta.setDisplayName("§8Leder Hose");
		einsen1.setItemMeta(meta);
		ItemStack leder4 = new ItemStack(301, 1);
		meta = einsen1.getItemMeta();
		meta.setDisplayName("§8Leder Schuhe");
		einsen1.setItemMeta(meta);

		extrasClothes.setItem(2, item4);
		extrasClothes.setItem(11, item5);
		extrasClothes.setItem(20, item6);
		extrasClothes.setItem(29, item7);

		extrasClothes.setItem(3, item8);
		extrasClothes.setItem(12, item9);
		extrasClothes.setItem(21, item10);
		extrasClothes.setItem(30, item11);

		extrasClothes.setItem(4, einsen1);
		extrasClothes.setItem(13, einsen2);
		extrasClothes.setItem(22, einsen3);
		extrasClothes.setItem(31, einsen4);

		extrasClothes.setItem(5, ketten1);
		extrasClothes.setItem(14, ketten2);
		extrasClothes.setItem(23, ketten3);
		extrasClothes.setItem(32, ketten4);

		extrasClothes.setItem(6, leder1);
		extrasClothes.setItem(15, leder2);
		extrasClothes.setItem(24, leder3);
		extrasClothes.setItem(33, leder4);
		p.openInventory(extrasClothes);
	}
}
