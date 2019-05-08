package me.bungeefan;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Jump extends JavaPlugin implements Listener {

	public Sound sound;
	public float volume;
	public ArrayList<Player> playerfalldmg = new ArrayList<Player>();
	public ArrayList<Player> cooldown = new ArrayList<Player>();
	public Inventory settings;
	public Inventory doublejump;
	public Inventory sounds;
	public Inventory partikel;

	public void onEnable() {
		loadConfig();
		saveConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new InventoryLST(this), this);
		settings = new SettingsInv(this).getInv();
		doublejump = new DoubleJumpInv(this).getInv();
		sounds = new SoundsInv(this).getInv();
		partikel = new PartikelInv(this).getInv();
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cs.hasPermission("jump.change")) {
			if (cmd.getName().equalsIgnoreCase("jump")) {
				if (args.length == 0) {
					if (getConfig().getBoolean("DoubleJump.aktiviert")) {
						getConfig().set("DoubleJump.aktiviert", false);
						saveConfig();
						cs.sendMessage(sendausgabe2("§cDoubleJump wurde deaktiviert!"));
						return true;
					} else {
						getConfig().set("DoubleJump.aktiviert", true);
						saveConfig();
						cs.sendMessage(sendausgabe2("§aDoubleJump wurde aktiviert!"));
						return true;
					}
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("test")) {
						if (cs instanceof Player) {
							Player p = (Player) cs;
							p.openInventory(settings);
						}
					}
					if (args[0].equalsIgnoreCase("sound")) {
						if (getConfig().getBoolean("JumpPads.Sound.aktiviert")) {
							getConfig().set("JumpPads.Sound.aktiviert", false);
							cs.sendMessage(sendausgabe2("§aJumpPads Sound wurde deaktiviert!"));
						} else {
							getConfig().set("JumpPads.Sound.aktiviert", true);
							cs.sendMessage(sendausgabe2("§cJumpPads Sound wurde aktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("partikel")) {
						if (getConfig().getBoolean("JumpPads.Partikel.aktiviert")) {
							getConfig().set("JumpPads.Partikel.aktiviert", false);
							cs.sendMessage(sendausgabe2("§aJumpPads Partikel wurde deaktiviert!"));
						} else {
							getConfig().set("JumpPads.Partikel.aktiviert", true);
							cs.sendMessage(sendausgabe2("§cJumpPads Partikel wurde aktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("reload")) {
						if (cs.hasPermission("jump.reload")) {
							String config = getConfig().getString("Jump.Prefix").replaceAll("&", "§");
							cs.sendMessage(config + "§cDie Config wird reloaded.");
							reloadConfig();
							saveConfig();
							List<String> platelocs = getConfig().getStringList("JumpPads.Orte");
							for (int i = 0; i < platelocs.size(); i++) {
								String koordinaten = platelocs.get(i);
								String[] split = koordinaten.split("/");
								Location loc = new Location(getServer().getWorld(split[0]), Integer.valueOf(split[1]),
										Integer.valueOf(split[2]), Integer.valueOf(split[3]));
								if (loc.getBlock().getType() == Material.AIR) {
									loc.getBlock().setType(Material.valueOf(getConfig().getString("JumpPads.Plate")));
								}
							}
							cs.sendMessage(config + "§aDie Config wurde erfolgreich reloaded.");
						} else {
							cs.sendMessage(sendausgabe("KeinRecht"));
						}
						return true;
					}
				} else if (args.length == 2) {
					if (args[0].equalsIgnoreCase("stärke")) {
						getConfig().set("JumpPads.Stärke", Float.valueOf(args[1]));
						saveConfig();
						cs.sendMessage(sendausgabe2("§aStärke wurde geändert!"));
					} else if (args[0].equalsIgnoreCase("höhe")) {
						getConfig().set("JumpPads.Höhe", Float.valueOf(args[1]));
						saveConfig();
						cs.sendMessage(sendausgabe2("§aHöhe wurde geändert!"));
					}
				} else {
					return false;
				}

			} else if (cmd.getName().equalsIgnoreCase("jumppads")) {
				if (args.length == 0) {
					if (getConfig().getBoolean("JumpPads.aktiviert")) {
						getConfig().set("JumpPads.aktiviert", false);
						cs.sendMessage(sendausgabe2("§aJumpPads wurden deaktiviert!"));
					} else {
						getConfig().set("JumpPads.aktiviert", true);
						cs.sendMessage(sendausgabe2("§cJumpPads wurden aktiviert!"));
					}
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("ändern")) {
						if (getConfig().getBoolean("JumpPads.ändern")) {
							getConfig().set("JumpPads.ändern", false);
							cs.sendMessage(sendausgabe2("§cJumpPads können nun nicht mehr geändert!"));
						} else {
							getConfig().set("JumpPads.ändern", true);
							cs.sendMessage(sendausgabe2("§aJumpPads können nun gesetzt oder geändert!"));
						}
					} else if (args[0].equalsIgnoreCase("sound")) {
						if (getConfig().getBoolean("JumpPads.Sound.aktiviert")) {
							getConfig().set("JumpPads.Sound.aktiviert", false);
							cs.sendMessage(sendausgabe2("§aJumpPads Sound wurde deaktiviert!"));
						} else {
							getConfig().set("JumpPads.Sound.aktiviert", true);
							cs.sendMessage(sendausgabe2("§cJumpPads Sound wurde aktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("partikel")) {
						if (getConfig().getBoolean("JumpPads.Partikel.aktiviert")) {
							getConfig().set("JumpPads.Partikel.aktiviert", false);
							cs.sendMessage(sendausgabe2("§aJumpPads Partikel wurde deaktiviert!"));
						} else {
							getConfig().set("JumpPads.Partikel.aktiviert", true);
							cs.sendMessage(sendausgabe2("§cJumpPads Partikel wurde aktiviert!"));
						}
					}
					return true;
				} else if (args.length == 2) {
					if (args[0].equalsIgnoreCase("stärke")) {
						getConfig().set("JumpPads.Stärke", Float.valueOf(args[1]));
						cs.sendMessage(sendausgabe2("§aStärke wurde geändert!"));
					} else if (args[0].equalsIgnoreCase("höhe")) {
						getConfig().set("JumpPads.Höhe", Float.valueOf(args[1]));
						cs.sendMessage(sendausgabe2("§aHöhe wurde geändert!"));
					}
					saveConfig();
					return true;
				}
			}
		} else {
			cs.sendMessage(sendausgabe("KeinRecht"));
			return true;
		}
		return false;
	}

	public String sendausgabe(String message) {
		return (getConfig().getString("Jump.Prefix") + (getConfig().getString(message))).replaceAll("&", "§");
	}

	public String sendausgabe2(String message) {
		return (getConfig().getString("Jump.Prefix").replaceAll("&", "§") + message);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if ((e.getCause() == EntityDamageEvent.DamageCause.FALL) && (e.getEntity() instanceof Player)) {
			Player p = (Player) e.getEntity();
			if (playerfalldmg.contains(p)) {
				playerfalldmg.remove(p);
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if ((p.getGameMode() != GameMode.CREATIVE)
				&& (p.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.AIR)
				&& p.hasPermission("doublejump.use")) {
			p.setAllowFlight(true);
		}
	}

	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
		double stärke = getConfig().getDouble("DoubleJump.Stärke");
		double höhe = getConfig().getDouble("DoubleJump.Höhe");
		Player p = e.getPlayer();
		if (cooldown.contains(p)) {
			e.setCancelled(true);
			p.setFlying(false);
			p.setAllowFlight(false);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 10.0F, 10.0F);
			p.sendMessage(
					"§9DoubleJump>> §7Du musst " + (getConfig().getInt("DoubleJump.Cooldown")) + " Sekunden warten!");
		} else {
			if ((e.getPlayer().getGameMode() != GameMode.CREATIVE)) {
				p.setFlying(false);
				p.setAllowFlight(false);
				p.setVelocity(p.getLocation().getDirection().multiply(stärke).setY(höhe));
				if ((getConfig().getBoolean("DoubleJump.Sound.aktiviert"))) {
					p.playSound(p.getLocation(), Sound.valueOf(getConfig().getString("DoubleJump.Sound.name")),
							Float.valueOf(getConfig().getString("DoubleJump.Sound.volume")), 2.0F);
				}
				if ((getConfig().getBoolean("DoubleJump.Partikel.aktiviert"))) {
					p.spawnParticle(Particle.valueOf(getConfig().getString("DoubleJump.Partikel.name")),
							p.getLocation().subtract(0.0D, 1.0D, 0.0D),
							getConfig().getInt("DoubleJump.Partikel.stärke"));
				}
				e.setCancelled(true);
				cooldown.add(p);

				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					public void run() {
						cooldown.remove(p);
					}
				}, 20 * (getConfig().getInt("DoubleJump.Cooldown")));
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if ((e.getBlock().getType() == Material.getMaterial(getConfig().getString("JumpPads.Plate")))
				&& (getConfig().getBoolean("JumpPads.ändern")) && ((p.hasPermission("jumppads.change")))) {
			Location location = e.getBlock().getLocation();
			String koordinaten = location.getWorld().getName() + "/" + location.getBlockX() + "/" + location.getBlockY()
					+ "/" + location.getBlockZ();
			p.sendMessage(sendausgabe("JumpPads.Gesetzt"));
			List<String> platelocs = getConfig().getStringList("JumpPads.Orte");
			if (!platelocs.contains(koordinaten)) {
				platelocs.add(koordinaten);
				getConfig().set("JumpPads.Orte", platelocs);
			}
			saveConfig();
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Location location = e.getBlock().getLocation();
		String koordinaten = location.getWorld().getName() + "/" + location.getBlockX() + "/" + location.getBlockY()
				+ "/" + location.getBlockZ();
		List<String> platelocs = getConfig().getStringList("JumpPads.Orte");
		if ((e.getBlock().getType() == Material.getMaterial(getConfig().getString("JumpPads.Plate")))
				&& (platelocs.contains(koordinaten)) && (p.hasPermission("jumppads.change"))
				&& (getConfig().getBoolean("JumpPads.ändern"))) {
			platelocs.remove(koordinaten);
			getConfig().set("JumpPads.Orte", platelocs);
			saveConfig();
			p.sendMessage(sendausgabe("JumpPads.Entfernt"));
		} else if ((e.getBlock().getType() == Material.getMaterial(getConfig().getString("JumpPads.Plate")))
				&& (platelocs.contains(koordinaten))
				&& ((!p.hasPermission("jumppads.change")) || (!getConfig().getBoolean("JumpPads.ändern")))) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPressurePlate(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if ((p.hasPermission("jumppads.use")) && (getConfig().getBoolean("JumpPads.aktiviert")) && (p.getLocation()
				.getBlock().getType() == Material.getMaterial(getConfig().getString("JumpPads.Plate")))) {
			double stärke = getConfig().getDouble("JumpPads.Stärke");
			double höhe = getConfig().getDouble("JumpPads.Höhe");
			Location location = p.getLocation();
			String koordinaten = location.getWorld().getName() + "/" + location.getBlockX() + "/" + location.getBlockY()
					+ "/" + location.getBlockZ();
			List<String> platelocs = getConfig().getStringList("JumpPads.Orte");

			if (platelocs.contains(koordinaten)) {
				p.setVelocity(p.getLocation().getDirection().multiply(stärke).setY(höhe));
				if ((getConfig().getBoolean("JumpPads.Sound.aktiviert"))) {
					p.playSound(p.getLocation(), Sound.valueOf(getConfig().getString("JumpPads.Sound.name")),
							Float.valueOf(getConfig().getString("JumpPads.Sound.volume")), 2.0F);
				}
				if ((getConfig().getBoolean("JumpPads.Partikel.aktiviert"))) {
					p.spawnParticle(Particle.valueOf(getConfig().getString("JumpPads.Partikel.name")),
							p.getLocation().subtract(0.0D, 1.0D, 0.0D), getConfig().getInt("JumpPads.Partikel.stärke"));
				}
				if (!playerfalldmg.contains(p)) {
					playerfalldmg.add(p);
				}
			}
		}
	}

	private void loadConfig() {
		getConfig().options().header("Hier kannst du alles ändern.");
		getConfig().addDefault("Jump.Prefix", "&b[Jump]&r ");
		getConfig().addDefault("Jump.Kein Recht", "&cDu hast keine Rechte um diesen Command ausführen!");
		getConfig().addDefault("Jump.Kein Spieler", "&cDu musst diesen Command als Spieler ausführen!");
		getConfig().addDefault("Jump.Nicht gefunden", "&cSpieler nicht gefunden!");
		getConfig().addDefault("Jump.Fehler", "&cFehler! Bitte versuche es erneut.");
		getConfig().addDefault("DoubleJump.aktiviert", true);
		getConfig().addDefault("DoubleJump.Stärke", 2.0);
		getConfig().addDefault("DoubleJump.Höhe", 1.5);
		getConfig().addDefault("DoubleJump.Cooldown", 2);
		getConfig().addDefault("DoubleJump.Sound.aktiviert", true);
		getConfig().addDefault("DoubleJump.Sound.name", "ENTITY_ENDERDRAGON_FLAP");
		getConfig().addDefault("DoubleJump.Sound.volume", 10.0);
		getConfig().addDefault("DoubleJump.Partikel.aktiviert", true);
		getConfig().addDefault("DoubleJump.Partikel.name", "DRAGON_BREATH");
		getConfig().addDefault("DoubleJump.Partikel.stärke", 50);
		getConfig().addDefault("JumpPads.aktiviert", true);
		getConfig().addDefault("JumpPads.Gesetzt", "&aJumpPad wurde gesetzt!");
		getConfig().addDefault("JumpPads.Entfernt", "&cJumpPad wurde entfernt!");
		getConfig().addDefault("JumpPads.Plate", "GOLD_PLATE");
		getConfig().addDefault("JumpPads.ändern", true);
		getConfig().addDefault("JumpPads.Stärke", 3.0);
		getConfig().addDefault("JumpPads.Höhe", 1.5);
		getConfig().addDefault("JumpPads.Orte", "");
		getConfig().addDefault("JumpPads.Sound.aktiviert", true);
		getConfig().addDefault("JumpPads.Sound.name", "ENTITY_ENDERDRAGON_FLAP");
		getConfig().addDefault("JumpPads.Sound.volume", 10.0);
		getConfig().addDefault("JumpPads.Partikel.aktiviert", true);
		getConfig().addDefault("JumpPads.Partikel.name", "DRAGON_BREATH");
		getConfig().addDefault("JumpPads.Partikel.stärke", 50);
		getConfig().options().copyDefaults(true);

		saveConfig();
		reloadConfig();
	}

}
