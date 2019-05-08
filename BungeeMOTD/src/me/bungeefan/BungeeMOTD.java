package me.bungeefan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class BungeeMOTD extends Plugin implements Listener {

	public boolean motd;
	public String line1 = "";
	public String line2 = "";
	public int slots;
	public Favicon favicon;
	public Configuration config;
	private boolean erneut = false;

	public void onEnable() {
		loadDefaultConfig();
		try {
			loadConfig();
		} catch (IOException e) {
			System.out.println("Config konnte nicht geladen werden!");
			getProxy().getPluginManager().getPlugin("BungeeMOTD").onDisable();
		}
		motd = config.getBoolean("MOTD.Status");
		slots = config.getInt("MOTD.Slots");
		BungeeCord.getInstance().getPluginManager().registerListener(this, this);
	}

	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		motd = config.getBoolean("MOTD.Status");
		if (cmd.getName().equalsIgnoreCase("motd")) {
			if (args.length >= 1) {
				if (cs.hasPermission("motd.use")) {
					if (args[0].equalsIgnoreCase("an")) {
						if (config.getBoolean("MOTD.Status") != true) {
							config.set("MOTD.Status", true);
							motd = true;
							cs.sendMessage(sendausgabe("MOTD.aktiviert"));
							saveConfig();
						}
						return true;
					} else if (args[0].equalsIgnoreCase("aus")) {
						if (config.getBoolean("MOTD.Status") != false) {
							config.set("MOTD.Status", false);
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
						config.set("MOTD.Line1", line1);
						cs.sendMessage(sendausgabe("MOTD.setLine"));
						saveConfig();
						return true;
					} else if (args[0].equalsIgnoreCase("setline2")) {
						line2 = "";
						for (int i = 1; i < args.length; i++) {
							line2 = line2 + (args[i] + " ");
						}
						config.set("MOTD.Line2", line2);
						cs.sendMessage(sendausgabe("MOTD.setLine"));
						saveConfig();
						return true;
					} else if (args[0].equalsIgnoreCase("setslots")) {
						slots = Integer.valueOf(args[1]);
						config.set("MOTD.Slots", slots);
						cs.sendMessage(sendausgabe("MOTD.setSlots"));
						saveConfig();
						return true;
					} else if (args[0].equalsIgnoreCase("reload")) {
						if (cs.hasPermission("motd.reload")) {
							// String config =
							// getConfig().getString("MOTD.Prefix").replaceAll("&",
							// "§");
							cs.sendMessage(config + "§cDie Config wird reloaded.");
							saveConfig();
							try {
								loadConfig();
								cs.sendMessage(config + "§aDie Config wurde erfolgreich reloaded.");
							} catch (IOException e) {
								System.out.println(e.getMessage());
							}

						} else {
							cs.sendMessage(sendausgabe("MOTD.Kein Recht"));
						}
						return true;
					} else if (args[0].equalsIgnoreCase("deaktivieren")) {
						if (cs.hasPermission("motd.deactivate")) {
							if (erneut) {
								cs.sendMessage((config.getString("MOTD.Prefix")).replaceAll("&", "§")
										+ "§cPlugin wurde deaktivert. Um es wieder zu aktiveren, Server neustarten oder reloaden!");
								BungeeCord.getInstance().pluginManager.getPlugin("BungeeMOTD");
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
						cs.sendMessage((config.getString("MOTD.Prefix")
								+ "§eBefehle:§r\n/motd (an/aus/setline1/setline2/setslots/reload/help/deaktivieren)")
										.replaceAll("&", "§"));
					} else {
						cs.sendMessage(sendausgabe("MOTD.Kein Recht"));
					}
					return true;
				}

			} else if (args.length == 0) {
				if (motd) {
					cs.sendMessage(config.getString("MOTD.Prefix").replaceAll("&", "§") + "§aMODT ist aktiviert!");
				} else {
					cs.sendMessage(config.getString("MOTD.Prefix").replaceAll("&", "§") + "§cMODT ist deaktiviert!");
				}
				return true;
			}
		}
		return false;
	}

	@EventHandler
	public void onPing(ProxyPingEvent e) {
		if (motd) {
			ServerPing ping = e.getResponse();
			String motd = ((config.getString("MOTD.Line1") + "\n" + config.getString("MOTD.Line2")).replaceAll("&",
					"§"));
			ping.setPlayers(new ServerPing.Players(config.getInt("MOTD.Slots"), config.getInt("MOTD.Version"), null));
			ping.setVersion(new ServerPing.Protocol("prot", config.getInt("MOTD.Version")));
			ping.setDescription(motd);
			if (favicon != null) {
				ping.setFavicon(favicon);
			}
			e.setResponse(ping);
		}
	}

	public String sendausgabe(String message) {
		return (config.getString("MOTD.Prefix") + (config.getString(message))).replaceAll("&", "§");
	}

	public void loadDefaultConfig() {
		if (!getDataFolder().exists())
			getDataFolder().mkdir();

		File file = new File(getDataFolder(), "config.yml");

		if (!file.exists()) {
			try (InputStream in = getResourceAsStream("config.yml")) {
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadConfig() throws IOException {
		config = ConfigurationProvider.getProvider(YamlConfiguration.class)
				.load(new File(getDataFolder(), "config.yml"));
	}

	public void saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config,
					new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
