package me.bungeefan.inv;

import java.util.ArrayList;

import me.bungeefan.API.ItemAPI;
import me.bungeefan.LobbySystem;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

public class LobbyInv {

	public LobbyInv(LobbySystem instance, Player p) {
		Inventory lobbytp = p.getServer().createInventory(null, 9, "§6Lobbys");

		String lobbyoff = instance.msg.getString("LobbySwitcher.Server.offline").replace("&", "§");
		String joinme = instance.msg.getString("LobbySwitcher.Server.join").replace("&", "§");
		String thislobby = instance.msg.getString("LobbySwitcher.Server.thisserver").replace("&", "§");
		String premium = instance.msg.getString("LobbySwitcher.Server.premium").replace("&", "§");

		ArrayList<String> thisserver = new ArrayList<String>();
		thisserver.add(thislobby);

		ArrayList<String> otherserver = new ArrayList<String>();
		otherserver.add(joinme);

		ArrayList<String> noPremi = new ArrayList<String>();
		noPremi.add(premium);

		ArrayList<String> off = new ArrayList<String>();
		off.add(lobbyoff);

		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		item.setData(new Dye(DyeColor.BLACK));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r");
		item.setItemMeta(meta);

		ItemStack item1 = ItemAPI.getHead("StackedGold", "§6Premium Lobby-1", 1);
		ItemMeta meta1 = item1.getItemMeta();
		if (instance.getConfig().getBoolean("Server.Lobbys.PremiumLobby.this")) {
			meta1.setLore(thisserver);
		} else if (p.hasPermission("lobby.premiumlobby")) {
			if (instance.getConfig().getBoolean("Server.Lobbys.PremiumLobby.online")) {
				meta1.setLore(otherserver);
			} else {
				meta1.setLore(off);
			}
		} else {
			meta1.setLore(noPremi);
		}
		item1.setItemMeta(meta1);

		ItemStack item2 = ItemAPI.getHead("metalhedd", "§eLobby-1", 1);
		ItemMeta meta2 = item2.getItemMeta();
		if (instance.getConfig().getBoolean("Server.Lobbys.Lobby-1.this")) {
			meta2.setLore(thisserver);
		} else if (instance.getConfig().getBoolean("Server.Lobbys.Lobby-1.online")) {
			meta2.setLore(otherserver);
		} else {
			meta2.setLore(off);
		}
		item2.setItemMeta(meta2);

		ItemStack item3 = ItemAPI.getHead("metalhedd", "§eLobby-2", 1);
		ItemMeta meta3 = item3.getItemMeta();
		if (instance.getConfig().getBoolean("Server.Lobbys.Lobby-2.this")) {
			meta3.setLore(thisserver);
		} else if (instance.getConfig().getBoolean("Server.Lobbys.Lobby-2.online")) {
			meta3.setLore(otherserver);
		} else {
			meta3.setLore(off);
		}
		item3.setItemMeta(meta3);

		ItemStack item4 = ItemAPI.getHead("metalhedd", "§eLobby-3", 1);
		ItemMeta meta4 = item4.getItemMeta();
		if (instance.getConfig().getBoolean("Server.Lobbys.Lobby-3.this")) {
			meta4.setLore(thisserver);
		} else if (instance.getConfig().getBoolean("Server.Lobbys.Lobby-3.online")) {
			meta4.setLore(otherserver);
		} else {
			meta4.setLore(off);
		}
		item4.setItemMeta(meta4);

		ItemStack item5 = ItemAPI.getHead("metalhedd", "§eLobby-4", 1);
		ItemMeta meta5 = item5.getItemMeta();
		if (instance.getConfig().getBoolean("Server.Lobbys.Lobby-4.this")) {
			meta5.setLore(thisserver);
		} else if (instance.getConfig().getBoolean("Server.Lobbys.Lobby-4.online")) {
			meta5.setLore(otherserver);
		} else {
			meta5.setLore(off);
		}
		item5.setItemMeta(meta5);

		lobbytp.setItem(0, item1);
		lobbytp.setItem(1, item);
		lobbytp.setItem(2, item2);
		lobbytp.setItem(3, item);
		lobbytp.setItem(4, item3);
		lobbytp.setItem(5, item);
		lobbytp.setItem(6, item4);
		lobbytp.setItem(7, item);
		lobbytp.setItem(8, item5);

		p.openInventory(lobbytp);
	}
}
