package me.bungeefan.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.bungeefan.LobbySystem;

public class JumpCMD implements CommandExecutor {

	public LobbySystem instance;
	public Sound sound;
	public float volume;
	public static ArrayList<String> cooldown = new ArrayList<String>();

	public JumpCMD(LobbySystem instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cs.hasPermission("lobby.jump")) {
			if (cmd.getName().equalsIgnoreCase("jump")) {
				if (args.length == 0) {
					if (instance.getConfig().getBoolean("DoubleJump.aktiviert")) {
						instance.getConfig().set("DoubleJump.aktiviert", false);
						instance.saveConfig();
						cs.sendMessage(sendausgabe2("DoubleJump wurde deaktiviert!"));
						return true;
					} else {
						instance.getConfig().set("DoubleJump.aktiviert", true);
						instance.saveConfig();
						cs.sendMessage(sendausgabe2("DoubleJump wurde aktiviert!"));
						return true;
					}
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("sound")) {
						if (instance.getConfig().getBoolean("JumpPads.Sound.aktiviert")) {
							instance.getConfig().set("JumpPads.Sound.aktiviert", false);
							cs.sendMessage(sendausgabe2("�aJumpPads Sound wurde deaktiviert!"));
						} else {
							instance.getConfig().set("JumpPads.Sound.aktiviert", true);
							cs.sendMessage(sendausgabe2("�cJumpPads Sound wurde aktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("partikel")) {
						if (instance.getConfig().getBoolean("JumpPads.Partikel.aktiviert")) {
							instance.getConfig().set("JumpPads.Partikel.aktiviert", false);
							cs.sendMessage(sendausgabe2("�aJumpPads Partikel wurde deaktiviert!"));
						} else {
							instance.getConfig().set("JumpPads.Partikel.aktiviert", true);
							cs.sendMessage(sendausgabe2("�cJumpPads Partikel wurde aktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("reload")) {
						if (cs.hasPermission("jump.reload")) {
							String config = instance.getConfig().getString("Jump.Prefix").replaceAll("&", "�");
							cs.sendMessage(config + "�cDie Config wird reloaded.");
							instance.reloadConfig();
							instance.saveConfig();
							List<String> platelocs = instance.getConfig().getStringList("JumpPads.Orte");
							for (int i = 0; i < platelocs.size(); i++) {
								String koordinaten = platelocs.get(i);
								String[] split = koordinaten.split("/");
								Location loc = new Location(instance.getServer().getWorld(split[0]),
										Integer.valueOf(split[1]), Integer.valueOf(split[2]),
										Integer.valueOf(split[3]));
								if (loc.getBlock().getType() == Material.AIR) {
									loc.getBlock().setType(
											Material.valueOf(instance.getConfig().getString("JumpPads.Plate")));
								}
							}
							cs.sendMessage(config + "�aDie Config wurde erfolgreich reloaded.");
						} else {
							cs.sendMessage(sendausgabe("KeinRecht"));
						}
						return true;
					}
				} else if (args.length == 2) {
					if (args[0].equalsIgnoreCase("st�rke")) {
						instance.getConfig().set("JumpPads.St�rke", Float.valueOf(args[1]));
						instance.saveConfig();
						cs.sendMessage(sendausgabe2("�aSt�rke wurde ge�ndert!"));
					} else if (args[0].equalsIgnoreCase("h�he")) {
						instance.getConfig().set("JumpPads.H�he", Float.valueOf(args[1]));
						instance.saveConfig();
						cs.sendMessage(sendausgabe2("�aH�he wurde ge�ndert!"));
					}
				} else {
					return false;
				}

			} else if (cmd.getName().equalsIgnoreCase("jumppads")) {
				if (args.length == 0) {
					if (instance.getConfig().getBoolean("JumpPads.aktiviert")) {
						instance.getConfig().set("JumpPads.aktiviert", false);
						cs.sendMessage(sendausgabe2("�aJumpPads wurden deaktiviert!"));
					} else {
						instance.getConfig().set("JumpPads.aktiviert", true);
						cs.sendMessage(sendausgabe2("�cJumpPads wurden aktiviert!"));
					}
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("�ndern")) {
						if (instance.getConfig().getBoolean("JumpPads.�ndern")) {
							instance.getConfig().set("JumpPads.�ndern", false);
							cs.sendMessage(sendausgabe2("�cJumpPads k�nnen nun nicht mehr ge�ndert!"));
						} else {
							instance.getConfig().set("JumpPads.�ndern", true);
							cs.sendMessage(sendausgabe2("�aJumpPads k�nnen nun gesetzt oder ge�ndert!"));
						}
					} else if (args[0].equalsIgnoreCase("sound")) {
						if (instance.getConfig().getBoolean("JumpPads.Sound.aktiviert")) {
							instance.getConfig().set("JumpPads.Sound.aktiviert", false);
							cs.sendMessage(sendausgabe2("�aJumpPads Sound wurde deaktiviert!"));
						} else {
							instance.getConfig().set("JumpPads.Sound.aktiviert", true);
							cs.sendMessage(sendausgabe2("�cJumpPads Sound wurde aktiviert!"));
						}
					} else if (args[0].equalsIgnoreCase("partikel")) {
						if (instance.getConfig().getBoolean("JumpPads.Partikel.aktiviert")) {
							instance.getConfig().set("JumpPads.Partikel.aktiviert", false);
							cs.sendMessage(sendausgabe2("�aJumpPads Partikel wurde deaktiviert!"));
						} else {
							instance.getConfig().set("JumpPads.Partikel.aktiviert", true);
							cs.sendMessage(sendausgabe2("�cJumpPads Partikel wurde aktiviert!"));
						}
					}
					return true;
				} else if (args.length == 2) {
					if (args[0].equalsIgnoreCase("st�rke")) {
						instance.getConfig().set("JumpPads.St�rke", Float.valueOf(args[1]));
						cs.sendMessage(sendausgabe2("�aSt�rke wurde ge�ndert!"));
					} else if (args[0].equalsIgnoreCase("h�he")) {
						instance.getConfig().set("JumpPads.H�he", Float.valueOf(args[1]));
						cs.sendMessage(sendausgabe2("�aH�he wurde ge�ndert!"));
					}
					instance.saveConfig();
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
		return (instance.getConfig().getString("Jump.Prefix") + (instance.getConfig().getString(message)))
				.replaceAll("&", "�");
	}

	public String sendausgabe2(String message) {
		return (instance.getConfig().getString("Jump.Prefix").replaceAll("&", "�") + message);
	}

}
