package me.bungeefan;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MOTD extends JavaPlugin implements Listener {

	public boolean motd;
	public String line1 = "";
	public String line2 = "";
	public int slots;
	private boolean erneut = false;

	public void onEnable() {
		loadConfig();
		saveConfig();
		motd = getConfig().getBoolean("MOTD.Status");
		slots = getConfig().getInt("MOTD.Slots");
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		motd = getConfig().getBoolean("MOTD.Status");
		if (cmd.getName().equalsIgnoreCase("motd")) {
			if (args.length >= 1) {
				if (cs.hasPermission("motd.use")) {
					if (args[0].equalsIgnoreCase("an")) {
						if (getConfig().getBoolean("MOTD.Status") != true) {
							getConfig().set("MOTD.Status", true);
							motd = true;
							cs.sendMessage(sendausgabe("MOTD.aktiviert"));
							saveConfig();
						}
						return true;
					} else if (args[0].equalsIgnoreCase("aus")) {
						if (getConfig().getBoolean("MOTD.Status") != false) {
							getConfig().set("MOTD.Status", false);
							motd = false;
							cs.sendMessage(sendausgabe("MOTD.deaktiviert"));
							saveConfig();
						}
						return true;
					} else if (args[0].equalsIgnoreCase("setline1")) {
						line1 = "";
						for (int i = 1; i < args.length; i++) {
							line1 = line1 + (args[i] + " ");
						}
						getConfig().set("MOTD.Line1", line1);
						cs.sendMessage(sendausgabe("MOTD.setLine"));
						saveConfig();
						return true;
					} else if (args[0].equalsIgnoreCase("setline2")) {
						line2 = "";
						for (int i = 1; i < args.length; i++) {
							line2 = line2 + (args[i] + " ");
						}
						getConfig().set("MOTD.Line2", line2);
						cs.sendMessage(sendausgabe("MOTD.setLine"));
						saveConfig();
						return true;
					} else if (args[0].equalsIgnoreCase("setslots")) {
						slots = Integer.valueOf(args[1]);
						getConfig().set("MOTD.Slots", slots);
						cs.sendMessage(sendausgabe("MOTD.setSlots"));
						saveConfig();
						return true;
					} else if (args[0].equalsIgnoreCase("reload")) {
						if (cs.hasPermission("motd.reload")) {
							String config = getConfig().getString("MOTD.Prefix").replaceAll("&", "§");
							cs.sendMessage(config + "§cDie Config wird reloaded.");
							reloadConfig();
							saveConfig();
							cs.sendMessage(config + "§aDie Config wurde erfolgreich reloaded.");
						} else {
							cs.sendMessage(sendausgabe("MOTD.Kein Recht"));
						}
						return true;
					} else if (args[0].equalsIgnoreCase("deaktivieren")) {
						if (cs.hasPermission("motd.deactivate")) {
							if (erneut) {
								cs.sendMessage((getConfig().getString("MOTD.Prefix")).replaceAll("&", "§")
										+ "§cPlugin wurde deaktivert. Um es wieder zu aktiveren, Server neustarten oder reloaden!");
								Bukkit.getPluginManager().disablePlugin(this);
							} else {
								cs.sendMessage(
										"§cBist du dir sicher ?\n§4Das kann nicht rückgängig gemacht werden !\n§cFühre diesen Command erneut aus!");
								erneut = true;
							}
						} else {
							cs.sendMessage(sendausgabe("MOTD.Kein Recht"));
						}
						return true;
					}
				} else {
					cs.sendMessage(sendausgabe("MOTD.Kein Recht"));
					return true;
				}
				if (args[0].equalsIgnoreCase("help")) {
					if (cs.hasPermission("motd.help")) {
						cs.sendMessage((getConfig().getString("MOTD.Prefix")
								+ "§eBefehle:§r\n/motd (an/aus/setline1/setline2/setslots/reload/help/deaktivieren)")
										.replaceAll("&", "§"));
					} else {
						cs.sendMessage(sendausgabe("MOTD.Kein Recht"));
					}
					return true;
				}

			} else if (args.length == 0) {
				if (motd) {
					cs.sendMessage(getConfig().getString("MOTD.Prefix").replaceAll("&", "§") + "§aMODT ist aktiviert!");
				} else {
					cs.sendMessage(
							getConfig().getString("MOTD.Prefix").replaceAll("&", "§") + "§cMODT ist deaktiviert!");
				}
				return true;
			}
		}
		return false;
	}

	@EventHandler
	public void onPing(ServerListPingEvent e) {
		if (motd) {
			e.setMaxPlayers(getConfig().getInt("MOTD.Slots"));
			e.setMotd((getConfig().getString("MOTD.Line1") + "\n" + getConfig().getString("MOTD.Line2")).replaceAll("&",
					"§"));
		}
	}

	public String sendausgabe(String message) {
		return (getConfig().getString("MOTD.Prefix") + (getConfig().getString(message))).replaceAll("&", "§");
	}

	private void loadConfig() {
		getConfig().options().header("Hier kannst du alles ändern.");
		getConfig().addDefault("MOTD.Status", true);
		getConfig().addDefault("MOTD.Prefix", "&9[&cM&eO&dT&bD&9]&r ");
		getConfig().addDefault("MOTD.aktiviert", "&aMOTD wurde aktiviert!");
		getConfig().addDefault("MOTD.deaktiviert", "&cMOTD wurde deaktiviert!");
		getConfig().addDefault("MOTD.setSlots", "&aSlots gesetzt!");
		getConfig().addDefault("MOTD.setLine", "&aMOTD gesetzt!");
		getConfig().addDefault("MOTD.Kein Recht", "&cDu hast keine Rechte um diesen Command ausführen!");
		getConfig().addDefault("MOTD.Slots", 0);
		getConfig().addDefault("MOTD.Line1", "&9Das ist eine BeispielMOTD&r");
		getConfig().addDefault("MOTD.Line2", "&bDiese kann in der Config oder per Befehl geändert werden!&r");
		getConfig().options().copyDefaults(true);

		saveConfig();
		reloadConfig();
	}

}
