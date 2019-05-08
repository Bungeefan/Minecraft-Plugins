package me.bungeefan.listener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

import me.bungeefan.LobbySystem;
import me.bungeefan.commands.BuildCMD;
import me.bungeefan.inv.CompassInv;
import me.bungeefan.inv.ExtrasInv;
import me.bungeefan.inv.LobbyInv;

public class InteractLST implements Listener {

	public LobbySystem instance;

	public InteractLST(LobbySystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getItem() != null) {
			if (!instance.Builders.contains(p)) {
				if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))
						&& (e.getItem().getItemMeta().getDisplayName()
								.equals(instance.getConfig().getString("Teleporter.name").replace("&", "§")))) {
					e.setCancelled(true);
					new CompassInv(instance, p);
				}
				if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))
						&& (e.getItem().getItemMeta().getDisplayName()
								.equals(instance.getConfig().getString("Extras.name").replace("&", "§")))) {
					e.setCancelled(true);
					p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
					new ExtrasInv(instance, p);
				}
				if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))
						&& (e.getItem().getItemMeta().getDisplayName()
								.equals(instance.getConfig().getString("Friends.name").replace("&", "§")))) {
					e.setCancelled(true);
					String cmd = instance.getConfig().getString("Command.Skull");
					p.performCommand(cmd);
				}
				if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))
						&& ((e.getItem().getItemMeta().getDisplayName()
								.equals(instance.getConfig().getString("Nicknamer.name2").replace("&", "§")))
								|| (e.getItem().getItemMeta().getDisplayName().equals(
										instance.getConfig().getString("Nicknamer.name1").replace("&", "§"))))) {
					e.setCancelled(true);
					/*
					 * if (SQLApi.AutoNick(p.getUniqueId().toString()).booleanValue ()) { if
					 * (instance.NickCountdown.contains(p.getName())) {
					 * p.sendMessage(instance.msg.getString("Prefix") +
					 * instance.msg.getString("countdown").replace("&", "§")); return; }
					 * instance.NickCountdown.add(p.getName());
					 * Bukkit.getScheduler().scheduleSyncDelayedTask(LobbySystem .get(), new
					 * Runnable() { public void run() { instance.NickCountdown.remove(p.getName());
					 * } }, 40L); SQLApi.setAutoNick(p.getUniqueId().toString(),
					 * Boolean.valueOf(false)); int sort = instance.getConfig().getInt(
					 * "options.Nicknamer.Slot"); ItemStack nick = new ItemStack(ItemAPI.create(421,
					 * 0, 1, "§c" + instance.getConfig().getString(
					 * "options.Nicknamer.Name2").replace("&", "§"), ""));
					 * 
					 * p.getInventory().setItem(sort, nick);
					 * p.sendMessage(instance.msg.getString("Prefix") +
					 * instance.msg.getString("AutoNick.disable"). replace("&", "§")); } else { if
					 * (instance.NickCountdown.contains(p.getName())) {
					 * p.sendMessage(instance.msg.getString("Prefix") +
					 * instance.msg.getString("countdown").replace("&", "§")); return; }
					 * instance.NickCountdown.add(p.getName());
					 * Bukkit.getScheduler().scheduleSyncDelayedTask(LobbySystem .get(), new
					 * Runnable() { public void run() { instance.NickCountdown.remove(p.getName());
					 * } }, 40L); SQLApi.setAutoNick(p.getUniqueId().toString(),
					 * Boolean.valueOf(true)); int sort = instance.getConfig().getInt(
					 * "options.Nicknamer.Slot"); ItemStack Nick = new ItemStack(ItemAPI.create(421,
					 * 0, 1, "§a" + instance.getConfig().getString(
					 * "options.Nicknamer.Name1").replace("&", "§"), ""));
					 * p.getInventory().setItem(sort, Nick);
					 * p.sendMessage(instance.msg.getString("Prefix") +
					 * instance.msg.getString("AutoNick.enable"). replace("&", "§")); }
					 */
				}
				if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))
						&& (e.getItem().getItemMeta().getDisplayName()
								.equals(instance.getConfig().getString("Lobbys.name").replace("&", "§")))) {
					e.setCancelled(true);
					new LobbyInv(instance, p);
				}
				String showname = instance.getConfig().getString("HideShowPlayer.name1").replace("&", "§");
				String hidename = instance.getConfig().getString("HideShowPlayer.name2").replace("&", "§");
				if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))
						&& ((e.getItem().getItemMeta().getDisplayName().equals(showname))
								|| (e.getItem().getItemMeta().getDisplayName().equals(hidename)))) {
					e.setCancelled(true);
					if (instance.HiderCountdown.contains(p.getName())) {
						p.sendMessage(instance.msg.getString("Prefix")
								+ instance.msg.getString("countdown").replace("&", "§"));
						return;
					}
					instance.HiderCountdown.add(p.getName());
					Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
						public void run() {
							instance.HiderCountdown.remove(p.getName());
						}
					}, 40L);
					if (instance.HideANDShow.contains(p.getName())) {
						instance.HideANDShow.remove(p.getName());
						for (Player all : Bukkit.getOnlinePlayers()) {
							p.showPlayer(all);
						}
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
						ItemStack hideplayer = new ItemStack(351, 1);
						hideplayer.setData(new Dye(DyeColor.GREEN));
						ItemMeta hideplayermeta = hideplayer.getItemMeta();
						hideplayermeta.setDisplayName(hidename);
						hideplayer.setItemMeta(hideplayermeta);
						int slot = instance.getConfig().getInt("HideShowPlayer.Slot");
						p.getInventory().setItem(slot, hideplayer);

						String prefix = instance.getConfig().getString("Prefix");
						prefix = prefix.replace("&", "§");
						String hide = instance.msg.getString("Hide.disable");
						hide = hide.replace("&", "§");
						p.sendMessage(prefix + hide);
					} else {
						instance.HideANDShow.add(p.getName());
						for (Player all : Bukkit.getOnlinePlayers()) {
							p.hidePlayer(all);
						}
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
						ItemStack hideplayer = new ItemStack(351, 1);
						hideplayer.setData(new Dye(DyeColor.GRAY));
						ItemMeta hideplayermeta = hideplayer.getItemMeta();
						hideplayermeta.setDisplayName(showname);
						hideplayer.setItemMeta(hideplayermeta);
						int slot = instance.getConfig().getInt("HideShowPlayer.Slot");
						p.getInventory().setItem(slot, hideplayer);

						String prefix = instance.getConfig().getString("Prefix");
						prefix = prefix.replace("&", "§");
						String hide = instance.msg.getString("Hide.enable");
						hide = hide.replace("&", "§");
						p.sendMessage(prefix + hide);
					}
				}
				if (e.getItem().getItemMeta().getDisplayName().equals("§cSilentLobby")) {
					e.setCancelled(true);

					// SQLApi.setLoc(p.getUniqueId().toString(),
					// p.getLocation().getX(), p.getLocation().getY(),
					// p.getLocation().getZ());
					if (instance.getConfig().getBoolean("Lobbys.SilentLobby.online")) {
						if (instance.getConfig().getBoolean("Lobbys.SilentLobby.this")) {
							String Server = instance.getConfig().getString("Lobbys.Lobby-1.Name");

							ByteArrayOutputStream b = new ByteArrayOutputStream();
							DataOutputStream out = new DataOutputStream(b);
							try {
								out.writeUTF("Connect");
								out.writeUTF(Server);
							} catch (IOException ex) {
								System.err.println("ERROR [Lobby1]");
							}
							p.sendPluginMessage(instance, "BungeeCord", b.toByteArray());

							String prefix = instance.getConfig().getString("Prefix");
							prefix = prefix.replace("&", "§");
							String silent = instance.msg.getString("SilentLobby.leave");
							silent = silent.replace("&", "§");
							p.sendMessage(prefix + silent);
						} else {
							String Server = instance.getConfig().getString("Lobbys.SilentLobby.Name");

							ByteArrayOutputStream b = new ByteArrayOutputStream();
							DataOutputStream out = new DataOutputStream(b);
							try {
								out.writeUTF("Connect");
								out.writeUTF(Server);
							} catch (IOException ex) {
								System.err.println("ERROR [SilentLobby]");
							}
							p.sendPluginMessage(instance, "BungeeCord", b.toByteArray());
						}
					} else {
						String prefix = instance.getConfig().getString("Prefix");
						prefix = prefix.replace("&", "§");
						String join = instance.msg.getString("Lobby.offline");
						join = join.replace("&", "§");
						p.sendMessage(prefix + join);
					}
				}
				if (instance.getConfig().getBoolean("Lobbys.SilentLobby.this")) {
					String name = instance.getConfig().getString("HideShowPlayer.name2").replace("&", "§");
					if (e.getItem().getItemMeta().getDisplayName().equals("§r" + name)) {
						e.setCancelled(true);
						String prefix = instance.getConfig().getString("Prefix");
						prefix = prefix.replace("&", "§");
						String silent = instance.msg.getString("Hide.Silent");
						silent = silent.replace("&", "§");
						p.sendMessage(prefix + silent);
					}
				}
			}
		}
	}
}
