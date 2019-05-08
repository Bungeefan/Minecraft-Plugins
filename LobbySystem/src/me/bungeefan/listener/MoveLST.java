package me.bungeefan.listener;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.bungeefan.LobbySystem;

public class MoveLST implements Listener {

	public LobbySystem instance;

	public MoveLST(LobbySystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if ((p.getLocation().getY() <= 5.0D) && (p.getGameMode() != GameMode.CREATIVE)
				&& (p.getGameMode() != GameMode.SPECTATOR)) {
			p.teleport(p.getWorld().getSpawnLocation());
		}
		if ((p.getGameMode() != GameMode.CREATIVE) && p.hasPermission("lobby.doublejump")
				&& (p.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.AIR)) {
			p.setAllowFlight(true);
		}
		if ((p.hasPermission("lobby.jumppads")) && (instance.getConfig().getBoolean("JumpPads.aktiviert"))
				&& (p.getLocation().getBlock().getType() == Material
						.getMaterial(instance.getConfig().getString("JumpPads.Plate")))) {
			double stärke = instance.getConfig().getDouble("JumpPads.stärke");
			double höhe = instance.getConfig().getDouble("JumpPads.höhe");
			Location location = p.getLocation();
			String koordinaten = location.getWorld().getName() + "/" + location.getBlockX() + "/" + location.getBlockY()
					+ "/" + location.getBlockZ();
			List<String> platelocs = instance.getConfig().getStringList("JumpPads.Orte");
			if (platelocs.contains(koordinaten)) {
				p.setVelocity(p.getLocation().getDirection().multiply(stärke).setY(höhe));
				if ((instance.getConfig().getBoolean("JumpPads.Sound.aktiviert"))) {
					p.playSound(p.getLocation(), Sound.valueOf(instance.getConfig().getString("JumpPads.Sound.name")),
							Float.valueOf(instance.getConfig().getString("JumpPads.Sound.volume")), 2.0F);
				}
				if (instance.getConfig().getBoolean("JumpPads.Partikel.aktiviert")) {
					p.spawnParticle(Particle.valueOf(instance.getConfig().getString("JumpPads.Partikel.name")),
							p.getLocation().subtract(0.0D, 1.0D, 0.0D),
							instance.getConfig().getInt("JumpPads.Partikel.stärke"));
				}
			}
		}
	}
}
