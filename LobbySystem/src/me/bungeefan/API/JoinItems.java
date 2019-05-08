package me.bungeefan.API;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Dye;

import me.bungeefan.LobbySystem;

public class JoinItems {

	public JoinItems(LobbySystem instance, Player p) {
		p.getInventory().clear();
		if (instance.getConfig().getBoolean("Teleporter.aktiviert")) {
			ItemStack compass = new ItemStack(345, 1);
			ItemMeta meta = compass.getItemMeta();
			meta.setDisplayName(instance.getConfig().getString("Teleporter.Name").replace("&", "§"));
			compass.setItemMeta(meta);
			p.getInventory().setItem(instance.getConfig().getInt("Teleporter.Slot"), compass);
		}
		String showname = instance.getConfig().getString("HideShowPlayer.Name1").replace("&", "§");
		String hidename = instance.getConfig().getString("HideShowPlayer.Name2").replace("&", "§");
		if (instance.getConfig().getBoolean("HideShowPlayer.enable")) {
			ItemStack hideplayer;
			if (instance.getConfig().getBoolean("Lobbys.SilentLobby.this")) {
				hideplayer = new ItemStack(351, 1);
				hideplayer.setData(new Dye(DyeColor.GRAY));
				ItemMeta meta = hideplayer.getItemMeta();
				meta.setDisplayName("§r" + hidename);
				hideplayer.setItemMeta(meta);
			} else if (!instance.HideANDShow.contains(p.getName())) {
				hideplayer = new ItemStack(351, 1);
				hideplayer.setData(new Dye(DyeColor.PURPLE));
				ItemMeta meta = hideplayer.getItemMeta();
				meta.setDisplayName(hidename);
				hideplayer.setItemMeta(meta);
			} else {
				hideplayer = new ItemStack(351, 1);
				hideplayer.setData(new Dye(DyeColor.GRAY));
				ItemMeta meta = hideplayer.getItemMeta();
				meta.setDisplayName(showname);
				hideplayer.setItemMeta(meta);
			}
			p.getInventory().setItem(instance.getConfig().getInt("HideShowPlayer.Slot"), hideplayer);
		}
		if ((instance.getConfig().getBoolean("SilentLobby.enable")) && (p.hasPermission("lobby.silentlobby"))) {
			ItemStack silent = new ItemStack(46, 1);
			ItemMeta meta = silent.getItemMeta();
			meta.setDisplayName("§cSilentLobby");
			silent.setItemMeta(meta);
			p.getInventory().setItem(instance.getConfig().getInt("SilentLobby.Slot"), silent);
		}
		if ((instance.getConfig().getBoolean("Nicknamer.enable")) && (p.hasPermission("lobby.nick"))) {
			/*
			 * if (SQLApi.AutoNick(p.getUniqueId().toString()).booleanValue()) { ItemStack
			 * nick = new ItemStack(421, 1); ItemMeta meta = nick.getItemMeta();
			 * meta.setDisplayName( "§a" +
			 * instance.getConfig().getString("Nicknamer.Name1"). replace("&", "§"));
			 * nick.setItemMeta(meta); p.getInventory().setItem(instance.getConfig().getInt(
			 * "Nicknamer.Slot"), nick); } else { ItemStack nick = new ItemStack(421, 1);
			 * ItemMeta meta = nick.getItemMeta(); meta.setDisplayName( "§c" +
			 * instance.getConfig().getString("Nicknamer.Name2"). replace("&", "§"));
			 * nick.setItemMeta(meta); p.getInventory().setItem(instance.getConfig().getInt(
			 * "Nicknamer.Slot"), nick); }
			 */
		}
		if (instance.getConfig().getBoolean("Extras.enable")) {
			ItemStack extras = new ItemStack(54, 1);
			ItemMeta meta = extras.getItemMeta();
			meta.setDisplayName(instance.getConfig().getString("Extras.Name").replace("&", "§"));
			extras.setItemMeta(meta);
			p.getInventory().setItem(instance.getConfig().getInt("Extras.Slot"), extras);
		}
		if (instance.getConfig().getBoolean("Lobbys.enable")) {
			ItemStack lobby = new ItemStack(399, 1);
			ItemMeta meta = lobby.getItemMeta();
			meta.setDisplayName(instance.getConfig().getString("Lobbys.Name").replace("&", "§"));
			lobby.setItemMeta(meta);
			p.getInventory().setItem(instance.getConfig().getInt("Lobbys.Slot"), lobby);
		}
		if (instance.getConfig().getBoolean("Friends.enable")) {
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1);
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setOwner(p.getName());
			meta.setDisplayName(instance.getConfig().getString("Friends.Name").replace("&", "§"));
			skull.setItemMeta(meta);
			p.getInventory().setItem(instance.getConfig().getInt("Friends.Slot"), skull);
		}
	}
}
