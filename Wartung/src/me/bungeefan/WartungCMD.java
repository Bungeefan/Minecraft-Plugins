package me.bungeefan;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.bungeefan.inv.EinstellungsInv;

public class WartungCMD implements CommandExecutor {

	private boolean wartung = false;
	private boolean erneut = false;

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		wartung = Wartung.get().getConfig().getBoolean("Wartung.Status");
		if (cmd.getName().equalsIgnoreCase("wartung")) {
			if (cs.hasPermission("wartung.use")) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("aus")) {
						Wartung.get().toggleWartung(false);
						return true;
					}
					if (args[0].equalsIgnoreCase("an")) {
						Wartung.get().toggleWartung(true);
						return true;
					}
					if (args[0].equalsIgnoreCase("reload")) {
						String config = Wartung.get().getConfig().getString("Wartung.Prefix").replaceAll("&", "§");
						cs.sendMessage(config + "§cDie Config wird reloaded.");
						Wartung.get().reloadConfig();
						Wartung.get().reload = true;
						cs.sendMessage(config + "§aDie Config wurde erfolgreich reloaded.");
						return true;
					}
					if (args[0].equalsIgnoreCase("help")) {
						cs.sendMessage((Wartung.get().getConfig().getString("Wartung.Prefix")
								+ "§eBefehle:§r\n/wartung (an/aus/reload/help/deaktivieren)").replaceAll("&", "§"));
						return true;
					}
					if (args[0].equalsIgnoreCase("deaktivieren")) {
						if (erneut) {
							cs.sendMessage((Wartung.get().getConfig().getString("Wartung.Prefix")).replaceAll("&", "§")
									+ "§cWartung wurde deaktivert. Um es wieder zu aktiveren, Server neustarten oder reloaden!");
							Bukkit.getPluginManager().disablePlugin(Wartung.get());
						} else {
							cs.sendMessage(
									"§cBist du dir sicher ?\n§4Das kann nicht rückgängig gemacht werden !\n§cFühre diesen Command erneut aus!");
							erneut = true;
						}
						return true;
					}
					/*
					 * if (args[0].equalsIgnoreCase("beta")) { if (cs instanceof Player) { Player p
					 * = (Player) cs; Wartung.get().settings = new EinstellungsInv().createInv();
					 * p.openInventory(Wartung.get().settings); } }
					 */
				} else if (args.length == 0) {
					wartung = (Wartung.get().getConfig().getBoolean("Wartung.Status"));
					if (wartung) {
						cs.sendMessage(Wartung.get().getConfig().getString("Wartung.Prefix").replaceAll("&", "§")
								+ "§cDer Wartungsmodus ist aktiviert!");
					} else {
						cs.sendMessage(Wartung.get().getConfig().getString("Wartung.Prefix").replaceAll("&", "§")
								+ "§cDer Wartungsmodus ist deaktiviert!");
					}
				}
			} else {
				cs.sendMessage(sendausgabe("Wartung.Kein Recht"));
			}
			return true;
		}
		return false;
	}

	public String sendausgabe(String message) {
		return (Wartung.get().getConfig().getString("Wartung.Prefix") + (Wartung.get().getConfig().getString(message)))
				.replaceAll("&", "§");
	}

}
