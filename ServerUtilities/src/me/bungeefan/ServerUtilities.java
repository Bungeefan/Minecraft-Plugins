package me.bungeefan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerUtilities extends JavaPlugin implements Listener {

	private boolean erneut = false;
	private HashMap<String, String> ticket = new HashMap<String, String>();
	private ArrayList<String> teamchat = new ArrayList<String>();

	public void onEnable() {
		loadConfig();
		saveConfig();
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("utilities") || label.equalsIgnoreCase("util")) {
			if (args.length == 1) {
				if (cs.hasPermission("util.use")) {
					if (args[0].equalsIgnoreCase("reload")) {
						String config = getConfig().getString("Utilities.Prefix").replaceAll("&", "§");
						cs.sendMessage(config + "§cDie Config wird reloaded.");
						reloadConfig();
						cs.sendMessage(config + "§aDie Config wurde erfolgreich reloaded.");
						return true;
					} else if (args[0].equalsIgnoreCase("deaktivieren")) {
						if (erneut) {
							cs.sendMessage((getConfig().getString("Utilities.Prefix")).replaceAll("&", "§")
									+ "§cPlugin wurde deaktivert. Um es wieder zu aktiveren, Server neustarten oder reloaden!");
							Bukkit.getPluginManager().disablePlugin(this);
						} else {
							cs.sendMessage(
									"§cBist du dir sicher ?\n§4Das kann nicht rückgängig gemacht werden !\n§cFühre diesen Command erneut aus!");
							erneut = true;
						}
						return true;
					}
				} else {
					cs.sendMessage(sendausgabe("Utilities.KeinRecht"));
					return true;
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("invsee")) {
			if (cs.hasPermission("util.invsee")) {
				if (args.length == 1) {
					if (cs instanceof Player) {
						Player p = (Player) cs;
						Player targetplayer = Bukkit.getPlayer(args[0]);
						if (targetplayer == null) {
							p.sendMessage(sendausgabe("Utilities.Nichtgefunden"));
						} else {
							p.openInventory(targetplayer.getInventory());
						}
					} else {
						cs.sendMessage(sendausgabe("Utilities.KeinSpieler"));
					}
				} else {
					return false;
				}
			} else {
				cs.sendMessage(sendausgabe("Utilities.KeinRecht"));
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("ticket")) {
			if (args.length >= 1) {
				if (cs instanceof Player) {
					Player p = (Player) cs;
					String uuid = p.getUniqueId().toString();
					String nachricht = "";
					for (int i = 0; i < args.length; i++) {
						nachricht = nachricht + " " + args[i];
					}
					nachricht = nachricht.replaceAll("&", "§");
					String prefix = ((getConfig().getString("Ticket").replaceAll("&", "§")).replace("[Player]",
							p.getName()));
					ticket.put(uuid, nachricht);
					Bukkit.broadcast(prefix + nachricht, "util.answer");
				} else {
					cs.sendMessage(sendausgabe("Utilities.KeinSpieler"));
				}
				return true;
			}
		} else if (cmd.getName().equalsIgnoreCase("answer") || label.equalsIgnoreCase("antwort")) {
			if (cs.hasPermission("util.ticket")) {
				if (args.length >= 2) {
					if (cs instanceof Player) {
						Player p = (Player) cs;
						String answer = "";
						for (int i = 1; i < args.length; i++) {
							answer = answer + " " + args[i];
						}
						answer = answer.replaceAll("&", "§");
						String prefix = ((getConfig().getString("Ticket").replaceAll("&", "§")).replace("[Player]",
								p.getName()));
						Player target = Bukkit.getPlayer(args[0]);
						if (target != null && ticket.containsKey(target.getUniqueId().toString())) {
							p.sendMessage(prefix + answer);
							target.sendMessage(prefix + answer);
							ticket.remove(target.getUniqueId().toString());
						} else {
							cs.sendMessage(sendausgabe("Utilities.Nichtgefunden"));
						}
					} else {
						cs.sendMessage(sendausgabe("Utilities.KeinSpieler"));
					}
					return true;
				}
			} else {
				cs.sendMessage(sendausgabe("Utilities.KeinRecht"));
				return true;
			}
		} else if (cmd.getName().equalsIgnoreCase("teamchat") || label.equalsIgnoreCase("tc")) {
			if (cs.hasPermission("util.teamchat")) {
				if (cs instanceof Player) {
					Player p = (Player) cs;
					String uuid = p.getUniqueId().toString();
					if (args.length == 0) {
						if (!teamchat.contains(uuid)) {
							teamchat.add(uuid);
							p.sendMessage("§aTeamchat wurde aktiviert!");
						} else {
							teamchat.remove(uuid);
							p.sendMessage("§cTeamchat wurde deaktiviert!");
						}
					} else if (teamchat.contains(uuid)) {
						String nachricht = "";
						for (int i = 0; i < args.length; i++) {
							nachricht = nachricht + " " + args[i];
						}
						for (int i = 0; i < teamchat.size(); i++) {
							Player target = Bukkit.getPlayer(UUID.fromString(teamchat.get(i)));
							if (target != null) {
								target.sendMessage(getConfig().getString("Teamchat").replaceAll("&", "§")
										.replace("[Player]", p.getName()) + nachricht);
							}
						}
					} else {
						cs.sendMessage("Teamchat ist deaktiviert!");
					}
				} else {
					cs.sendMessage(sendausgabe("Utilities.KeinSpieler"));
				}

			} else {
				cs.sendMessage(sendausgabe("Utilities.KeinRecht"));
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("fake"))

		{
			if (cs.hasPermission("util.fake")) {
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("join")) {
						Bukkit.getServer()
								.broadcastMessage((getConfig().getString("Utilities.FakeJoin").replaceAll("&", "§"))
										.replace("[Player]", args[1]));
					}
					if (args[0].equalsIgnoreCase("leave")) {
						Bukkit.getServer()
								.broadcastMessage((getConfig().getString("Utilities.FakeLeave").replaceAll("&", "§"))
										.replace("[Player]", args[1]));
					}
					return true;
				} else if (args.length > 2) {
					cs.sendMessage(
							getConfig().getString("Utilities.Prefix").replaceAll("&", "§") + "§4Zu viele Argumente!");
				} else {
					cs.sendMessage(getConfig().getString("Utilities.Prefix").replaceAll("&", "§")
							+ "§4Nicht genug Argumente!");
				}
			}

		} else if (cmd.getName().equalsIgnoreCase("uuid")) {
			if (cs.hasPermission("util.uuid")) {
				if (args.length == 0) {
					if (cs instanceof Player) {
						Player player = (Player) cs;
						cs.sendMessage("§bDeine UUID: §a" + player.getUniqueId());
						return true;
					} else {
						cs.sendMessage(getConfig().getString("Utilities.Prefix").replaceAll("&", "§")
								+ "§cEs muss ein Spieler angegeben werden.");
					}
				} else if (args.length == 1) {
					if (Bukkit.getServer().getPlayer(args[0]) != null) {
						cs.sendMessage("§bUUID von §6" + args[0] + "§r: §a"
								+ Bukkit.getServer().getPlayer(args[0]).getUniqueId());
					} else {
						cs.sendMessage("§bUUID von §6" + args[0] + "§r: §a"
								+ Bukkit.getServer().getOfflinePlayer(args[0]).getUniqueId());
					}
					return true;
				} else {
					cs.sendMessage(
							getConfig().getString("Utilities.Prefix").replaceAll("&", "§") + "§4Zu viele Argumente!");
				}
			} else {
				cs.sendMessage(sendausgabe("Utilities.Kein Recht"));
				return true;
			}
		} else if (cmd.getName().equalsIgnoreCase("custom")) {
			if (cs.hasPermission("util.custom")) {
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("autorespawn")) {
						if (args[1].equalsIgnoreCase("ein")) {
							getConfig().set("Utilities.AutoRespawn", true);
							cs.sendMessage(sendausgabe2("AutoRespawn wurde aktiviert!"));
						} else if (args[1].equalsIgnoreCase("aus")) {
							getConfig().set("Utilities.AutoRespawn", false);
							cs.sendMessage(sendausgabe2("AutoRespawn wurde deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("unlimitedfood")) {
						if (args[1].equalsIgnoreCase("ein")) {
							getConfig().set("Utilities.UnlimitedFood", true);
							cs.sendMessage(sendausgabe2("UnlimitedFood wurde aktiviert!"));
						} else if (args[1].equalsIgnoreCase("aus")) {
							getConfig().set("Utilities.UnlimitedFood", false);
							cs.sendMessage(sendausgabe2("UnlimitedFood wurde deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("falldamage")) {
						if (args[1].equalsIgnoreCase("ein")) {
							getConfig().set("Utilities.FallDamage", true);
							cs.sendMessage(sendausgabe2("FallDamage wurde aktiviert!"));
						} else if (args[1].equalsIgnoreCase("aus")) {
							getConfig().set("Utilities.FallDamage", false);
							cs.sendMessage(sendausgabe2("FallDamage wurde deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("pluginscmdblocked")) {
						if (args[1].equalsIgnoreCase("ein")) {
							getConfig().set("Utilities.PluginsCMDBlocked", true);
							cs.sendMessage(sendausgabe2("PluginsCMDBlocked wurde aktiviert!"));
						} else if (args[1].equalsIgnoreCase("aus")) {
							getConfig().set("Utilities.PluginsCMDBlocked", false);
							cs.sendMessage(sendausgabe2("PluginsCMDBlocked wurde deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("playerchat")) {
						if (args[1].equalsIgnoreCase("ein")) {
							getConfig().set("Custom.PlayerChat", true);
							cs.sendMessage(sendausgabe2("PlayerChat wurde aktiviert!"));
						} else if (args[1].equalsIgnoreCase("aus")) {
							getConfig().set("Custom.PlayerChat", false);
							cs.sendMessage(sendausgabe2("PlayerChat wurde deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("join")) {
						if (args[1].equalsIgnoreCase("ein")) {
							getConfig().set("Custom.Join", true);
							cs.sendMessage(sendausgabe2("CustomJoin wurde aktiviert!"));
						} else if (args[1].equalsIgnoreCase("aus")) {
							getConfig().set("Custom.Join", false);
							cs.sendMessage(sendausgabe2("CustomJoin wurde deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("leave")) {
						if (args[1].equalsIgnoreCase("ein")) {
							getConfig().set("Custom.Leave", true);
							cs.sendMessage(sendausgabe2("CustomLeave wurde aktiviert!"));
						} else if (args[1].equalsIgnoreCase("aus")) {
							getConfig().set("Custom.Leave", false);
							cs.sendMessage(sendausgabe2("CustomLeave wurde deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("kick")) {
						if (args[1].equalsIgnoreCase("ein")) {
							getConfig().set("Custom.Kick", true);
							cs.sendMessage(sendausgabe2("CustomKick wurde aktiviert!"));
						} else if (args[1].equalsIgnoreCase("aus")) {
							getConfig().set("Custom.Kick", false);
							cs.sendMessage(sendausgabe2("CustomKick wurde deaktiviert!"));
						}
					}
					saveConfig();
					return true;
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("autorespawn")) {
						if (getConfig().getBoolean("Utilities.AutoRespawn")) {
							cs.sendMessage(sendausgabe2("AutoRespawn ist aktiviert!"));
						} else {
							cs.sendMessage(sendausgabe2("AutoRespawn ist deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("unlimitedfood")) {
						if (getConfig().getBoolean("Utilities.UnlimitedFood")) {
							cs.sendMessage(sendausgabe2("UnlimitedFood ist aktiviert!"));
						} else {
							cs.sendMessage(sendausgabe2("UnlimitedFood ist deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("falldamage")) {
						if (getConfig().getBoolean("Utilities.FallDamage")) {
							cs.sendMessage(sendausgabe2("FallDamage ist aktiviert!"));
						} else {
							cs.sendMessage(sendausgabe2("FallDamage ist deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("pluginscmdblocked")) {
						if (getConfig().getBoolean("Utilities.PluginsCMDBlocked")) {
							cs.sendMessage(sendausgabe2("PluginsCMDBlocked ist aktiviert!"));
						} else {
							cs.sendMessage(sendausgabe2("PluginsCMDBlocked ist deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("playerchat")) {
						if (getConfig().getBoolean("Custom.PlayerChat")) {
							cs.sendMessage(sendausgabe2("PlayerChat ist aktiviert!"));
						} else {
							cs.sendMessage(sendausgabe2("PlayerChat ist deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("join")) {
						if (getConfig().getBoolean("Custom.Join")) {
							cs.sendMessage(sendausgabe2("CustomJoin ist aktiviert!"));
						} else {
							cs.sendMessage(sendausgabe2("CustomJoin ist deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("leave")) {
						if (getConfig().getBoolean("Custom.Leave")) {
							cs.sendMessage(sendausgabe2("CustomLeave ist aktiviert!"));
						} else {
							cs.sendMessage(sendausgabe2("CustomLeave ist deaktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("kick")) {
						if (getConfig().getBoolean("Custom.Kick")) {
							cs.sendMessage(sendausgabe2("CustomKick ist aktiviert!"));
						} else {
							cs.sendMessage(sendausgabe2("CustomKick ist deaktiviert!"));
						}
					}
					return true;
				}
			} else {
				cs.sendMessage(sendausgabe("Utilities.KeinRecht"));
				return true;
			}
		} else if (cmd.getName().equalsIgnoreCase("nickname")) {
			if (cs.hasPermission("util.nickname")) {
				if (args.length == 3) {
					if (args[0].equalsIgnoreCase("set")) {
						Player p = Bukkit.getPlayer(args[1]);
						p.setDisplayName(args[2].replaceAll("&", "§") + "§r");
						p.setPlayerListName(args[2].replaceAll("&", "§") + "§r");
						cs.sendMessage(sendausgabe("Nickname.Gesetzt"));
						return true;
					}
				} else if (args.length == 2) {
					if (args[0].equalsIgnoreCase("reset")) {
						Player p = Bukkit.getPlayer(args[1]);
						p.setDisplayName(null);
						p.setPlayerListName(null);
						cs.sendMessage(sendausgabe("Nickname.Reset"));
						return true;
					}
				}
				cs.sendMessage(sendausgabe("Utilities.Fehler"));
				return true;
			} else {
				cs.sendMessage(sendausgabe("Utilities.KeinRecht"));
				return true;
			}
		}
		return false;
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if ((e.getCause() == EntityDamageEvent.DamageCause.FALL) && (e.getEntity() instanceof Player)
				&& (getConfig().getBoolean("Utilities.FallDamage"))) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onUnknownCMD(PlayerCommandPreprocessEvent e) {
		if (getConfig().getBoolean("Custom.UnknownCMD") && !e.getPlayer().hasPermission("custom.unkown")) {
			HelpTopic help = Bukkit.getHelpMap().getHelpTopic(e.getMessage());
			if (help == null) {
				e.getPlayer().sendMessage((getConfig().getString("Custom.UnknownCMDMessage").replaceAll("&", "§")));
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlugins(PlayerCommandPreprocessEvent e) {
		if (getConfig().getBoolean("Utilities.PluginsCMDBlocked") && !e.getPlayer().hasPermission("custom.plugins")) {
			if (e.getMessage().equalsIgnoreCase("/plugins") || e.getMessage().equalsIgnoreCase("/pl")) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		if (getConfig().getBoolean("Utilities.UnlimitedFood")) {
			Player p = (Player) e.getEntity();
			p.setFoodLevel(20);
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (getConfig().getBoolean("Utilities.AutoRespawn")) {
			Player player = e.getEntity();
			player.spigot().respawn();
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (getConfig().getBoolean("Custom.PlayerChat")) {
			String msg = e.getMessage();
			e.setFormat(((getConfig().getString("Custom.PlayerChatName") + msg).replaceAll("&", "§"))
					.replace("[Player]", e.getPlayer().getDisplayName()));
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (getConfig().getBoolean("Custom.Join")) {
			e.setJoinMessage(((getConfig().getString("Custom.JoinMessage")).replaceAll("&", "§")).replace("[Player]",
					e.getPlayer().getDisplayName()));
		}
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {
		if (getConfig().getBoolean("Custom.Kick")) {
			e.setLeaveMessage(((getConfig().getString("Custom.KickMessage")).replaceAll("&", "§")).replace("[Player]",
					e.getPlayer().getDisplayName()));
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		ticket.remove(e.getPlayer().getUniqueId().toString());
		if (getConfig().getBoolean("Custom.Leave")) {
			e.setQuitMessage(((getConfig().getString("Custom.LeaveMessage")).replaceAll("&", "§")).replace("[Player]",
					e.getPlayer().getDisplayName()));
		}
	}

	public String sendausgabe(String message) {
		return (getConfig().getString("Utilities.Prefix") + (getConfig().getString(message))).replaceAll("&", "§");
	}

	public String sendausgabe2(String message) {
		return (getConfig().getString("Utilities.Prefix").replaceAll("&", "§") + message);
	}

	private void loadConfig() {
		getConfig().options().header("Hier kannst du alles ändern.");
		getConfig().addDefault("Utilities.Prefix", "&3[ServerUtilities]&r ");
		getConfig().addDefault("Utilities.KeinRecht", "&cDu hast keine Rechte um diesen Command ausführen!");
		getConfig().addDefault("Utilities.KeinSpieler", "&cDu musst diesen Command als Spieler ausführen!");
		getConfig().addDefault("Utilities.Nichtgefunden", "&cSpieler nicht gefunden!");
		getConfig().addDefault("Utilities.Fehler", "&cFehler! Bitte versuche es erneut.");
		getConfig().addDefault("Utilities.FakeJoin", "&e[Player] hat das Spiel betreten");
		getConfig().addDefault("Utilities.FakeLeave", "&e[Player] hat das Spiel verlassen");
		getConfig().addDefault("Utilities.AutoRespawn", false);
		getConfig().addDefault("Utilities.UnlimitedFood", false);
		getConfig().addDefault("Utilities.FallDamage", false);
		getConfig().addDefault("Utilities.PluginsCMDBlocked", false);
		getConfig().addDefault("Custom.UnknownCMD", false);
		getConfig().addDefault("Custom.UnknownCMDMessage", "&cDieser Command wurde nicht gefunden!");
		getConfig().addDefault("Custom.PlayerChat", false);
		getConfig().addDefault("Custom.PlayerChatName", "&a[Player]&7:&r ");
		getConfig().addDefault("Custom.Join", false);
		getConfig().addDefault("Custom.JoinMessage", "&a[Player]&r &ehat den Server betreten.");
		getConfig().addDefault("Custom.Leave", false);
		getConfig().addDefault("Custom.LeaveMessage", "&a[Player]&r &ehat den Server verlassen.");
		getConfig().addDefault("Custom.Kick", false);
		getConfig().addDefault("Custom.KickMessage", "&a[Player]&r &cwurde vom Server gekickt.");
		getConfig().addDefault("Teamchat", "&a[&dTeamchat&a] &6[Player] &8:&r");
		getConfig().addDefault("Ticket", "&a[&dTicket&a] &6[Player] &8:&r");
		getConfig().addDefault("Nickname.Gesetzt", "&aName wurde erfolgreich gesetzt!");
		getConfig().addDefault("Nickname.Reset", "&aName wurde erfolgreich zurückgesetzt!");
		getConfig().options().copyDefaults(true);

		saveConfig();
		reloadConfig();
	}

}
