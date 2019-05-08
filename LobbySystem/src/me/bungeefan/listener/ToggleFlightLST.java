package me.bungeefan.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import me.bungeefan.LobbySystem;
import me.bungeefan.commands.JumpCMD;

public class ToggleFlightLST implements Listener {

	public LobbySystem instance;

	public ToggleFlightLST(LobbySystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
		double stärke = instance.getConfig().getDouble("DoubleJump.stärke");
		double höhe = instance.getConfig().getDouble("DoubleJump.höhe");
		Player p = e.getPlayer();
		if (JumpCMD.cooldown.contains(p.getName())) {
			e.setCancelled(true);
			p.setFlying(false);
			p.setAllowFlight(false);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 10.0F, 10.0F);
		} else {
			if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
				p.setFlying(false);
				p.setAllowFlight(false);
				p.setVelocity(p.getLocation().getDirection().multiply(stärke).setY(höhe));
				if (instance.getConfig().getBoolean("DoubleJump.Sound.aktiviert")) {
					p.playSound(p.getLocation(), Sound.valueOf(instance.getConfig().getString("DoubleJump.Sound.name")),
							Float.valueOf(instance.getConfig().getString("DoubleJump.Sound.volume")), 2.0F);
				}
				if (instance.getConfig().getBoolean("DoubleJump.Partikel.aktiviert")) {
					p.spawnParticle(Particle.valueOf(instance.getConfig().getString("DoubleJump.Partikel.name")),
							p.getLocation().subtract(0.0D, 1.0D, 0.0D),
							instance.getConfig().getInt("DoubleJump.Partikel.stärke"));
				}
				e.setCancelled(true);
				JumpCMD.cooldown.add(p.getName());
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
					public void run() {
						JumpCMD.cooldown.remove(p.getName());
					}
				}, 20 * (instance.getConfig().getInt("DoubleJump.Cooldown")));
			}
		}
	}
}
