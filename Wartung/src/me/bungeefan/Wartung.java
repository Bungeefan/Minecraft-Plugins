package me.bungeefan;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Iterables;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;

public class Wartung extends JavaPlugin implements Listener {

	private Collection<? extends Player> onlineplayer;
	public boolean wartung;
	public boolean reload = true;
	public boolean autowartung;
	public int endhour = 0;
	public int endminute;
	public int hour;
	public int minute;
	public Calendar cal;
	public Calendar start;
	public Calendar end;
	private static Wartung instance;

	// public Inventory settings;

	public static Wartung get() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		wartung = getConfig().getBoolean("Wartung.Aktiv");
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("wartung").setExecutor(new WartungCMD());
		autowartung();
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if ((wartung) && (!e.getPlayer().hasPermission("wartung.exempt"))) {
			e.disallow(null,
					(((getConfig().getString("Kick.Line1")) + "\n" + (getConfig().getString("Kick.Line2")) + "\n"
							+ (getConfig().getString("Kick.Line3")) + "\n" + (getConfig().getString("Kick.Line4")))
									.replaceAll("&", "§")));
		}
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {
		if (wartung) {
			if (e.getPlayer().hasPermission("wartung.exempt") && getConfig().getBoolean("Wartung.KickON")) {
				e.setLeaveMessage(((getConfig().getString("Wartung.Prefix") + getConfig().getString("Wartung.Kick"))
						.replaceAll("&", "§")).replace("[Player]", e.getPlayer().getDisplayName()));
			} else {
				e.setLeaveMessage("");
			}
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (wartung) {
			if (e.getPlayer().hasPermission("wartung.exempt") && getConfig().getBoolean("Wartung.LeaveON")) {
				e.setQuitMessage(((getConfig().getString("Wartung.Prefix") + getConfig().getString("Wartung.Leave"))
						.replaceAll("&", "§")).replace("[Player]", e.getPlayer().getDisplayName()));
			} else {
				e.setQuitMessage("");
			}
		}
	}

	@EventHandler
	public void onPing(ServerListPingEvent e) {
		if (wartung) {
			e.setMaxPlayers(getConfig().getInt("Wartung.Slots"));
			e.setMotd((getConfig().getString("MOTD.Line1") + "\n" + getConfig().getString("MOTD.Line2")).replaceAll("&",
					"§"));
		}
	}

	/*
	 * @EventHandler public void onInvClick(InventoryClickEvent e) {
	 * System.out.println("Trigger"); if
	 * (e.getInventory().getName().equalsIgnoreCase(settings.getName())) {
	 * e.getWhoClicked().sendMessage("Item clicked"); if (e.getCurrentItem() !=
	 * null) { if (e.getCurrentItem().getItemMeta().getDisplayName().
	 * equalsIgnoreCase("§cWartungsmodus ein/aus")) { if (wartung) {
	 * toggleWartung(false); } else { toggleWartung(true); } } }
	 * e.setCancelled(true); } }
	 */

	public void toggleWartung(boolean ein) {
		if (ein) {
			Bukkit.broadcastMessage(
					(getConfig().getString("Wartung.Prefix") + "§cDer Server befindet sich nun im Wartungsmodus!")
							.replaceAll("&", "§"));
			onlineplayer = Bukkit.getOnlinePlayers();
			if (getConfig().getBoolean("Wartung.KickAll")) {
				for (int i = 0; i < onlineplayer.size(); i++) {
					Player player = Iterables.get(onlineplayer, i);
					if (!player.hasPermission("wartung.exempt")) {
						player.kickPlayer((getConfig().getString("Kick.Line1").replaceAll("&", "§")) + "\n"
								+ (getConfig().getString("Kick.Line2").replaceAll("&", "§")) + "\n"
								+ (getConfig().getString("Kick.Line3").replaceAll("&", "§")) + "\n"
								+ (getConfig().getString("Kick.Line4").replaceAll("&", "§")));
					}
				}
			}
			wartung = true;
		} else {
			wartung = false;
			Bukkit.broadcastMessage((getConfig().getString("Wartung.Prefix")
					+ "§cDer Server befindet sich nun nicht mehr im Wartungsmodus!").replaceAll("&", "§"));
		}
		getConfig().set("Wartung.Aktiv", wartung);
		saveConfig();
	}

	public void autowartung() {
		long seconds = 0;
		cal = Calendar.getInstance();
		if (cal.get(Calendar.SECOND) > 0) {
			seconds = 60 - cal.get(Calendar.SECOND);
		}
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				cal = Calendar.getInstance();
				if (reload) {
					reload = false;
					autowartung = getConfig().getBoolean("AutoWartung.aktiviert");
					String uhrzeit = getConfig().getString("AutoWartung.Uhrzeit");
					String enduhrzeit = getConfig().getString("AutoWartung.EndUhrzeit");
					String[] split = uhrzeit.split(":");
					String[] endsplit = enduhrzeit.split(":");
					hour = Integer.valueOf(split[0]);
					minute = Integer.valueOf(split[1]);
					start = Calendar.getInstance();
					start.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), hour, minute);
					endhour = Integer.valueOf(endsplit[0]);
					endminute = Integer.valueOf(endsplit[1]);
					end = Calendar.getInstance();
					end.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), endhour,
							endminute);
				}
				if (autowartung) {
					if (!wartung && cal.compareTo(start) >= 0 && cal.compareTo(end) < 0) {
						toggleWartung(true);
					}
					if (wartung && cal.compareTo(end) == 0) {
						toggleWartung(false);
					}
				}
			}
		}, seconds, 1200);
	}
}
