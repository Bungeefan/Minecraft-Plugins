package me.bungeefan.commands;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import me.bungeefan.BungeeSystem;
import me.bungeefan.ImageChar;
import me.bungeefan.ImageMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class JoinMeCMD extends Command {

	private BungeeSystem instance;

	public JoinMeCMD(BungeeSystem instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void execute(CommandSender cs, String[] args) {
		String lobby = instance.messages.getString("Allgemein.LobbyServer");
		if (cs instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) cs;
			if (args.length == 0) {
				if (p.hasPermission("bs.joinme") || p.hasPermission("bs.admin")) {
					if (!p.getServer().getInfo().getName().contains(lobby)) {
						if (!instance.joinMe.containsKey(p)) {
							for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
								URL url = null;
								try {
									url = new URL("https://minotar.net/avatar/" + p.getName() + "/8.png");
								} catch (MalformedURLException ex) {
									ex.printStackTrace();
								}
								ImageMessage imageMessage = null;
								try {
									imageMessage = new ImageMessage(ImageIO.read(url), 8, ImageChar.BLOCK.getChar());
								} catch (IOException ex) {
									ex.printStackTrace();
								}
								int i = 1;
								for (String lines : imageMessage.getLines()) {
									if (i != 4 && i != 5) {
										players.sendMessage(lines);
									}
									if (i == 4) {
										players.sendMessage(lines + " " + p.getDisplayName() + " §7spielt §6"
												+ (p.getServer().getInfo().getName().contains("-")
														? p.getServer().getInfo().getName().split("-")[0]
														: p.getServer().getInfo().getName())
												+ "§7.");
									}
									if (i == 5) {
										TextComponent linesString = new TextComponent(lines + " ");
										TextComponent joinString = new TextComponent("§a§nServer betreten");

										joinString.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
												new ComponentBuilder("§8\u00BB §e" + p.getServer().getInfo().getName())
														.create()));
										joinString.setClickEvent(new ClickEvent(Action.RUN_COMMAND,
												"/joinme " + p.getServer().getInfo().getName()));

										linesString.addExtra(joinString);

										players.sendMessage(linesString);
									}
									i++;

									instance.joinMe.put(p, p.getServer().getInfo().getName());
								}
							}
						} else {
							p.sendMessage(instance.sendausgabe("JoinMe.BereitsGesendet"));
						}
					} else {
						p.sendMessage(instance.sendausgabe("JoinMe.InLobby"));
					}
				} else {
					p.sendMessage(instance.sendausgabe(("Allgemein.KeinRecht")));
				}

			} else if (args.length == 1) {
				boolean isValid = false;
				for (String servers : ProxyServer.getInstance().getServers().keySet()) {
					if (args[0].equalsIgnoreCase(servers) && !args[0].contains(lobby)) {
						isValid = true;
					}
				}
				if (!isValid) {
					p.sendMessage(instance.prefix + "§cDieser Befehl existiert nicht.");
				} else {
					isValid = false;
					for (String servers : instance.joinMe.values()) {
						if (args[0].equalsIgnoreCase(servers)) {
							p.connect(ProxyServer.getInstance().getServerInfo(servers));
							isValid = true;
							break;
						}
					}
					if (!isValid) {
						p.sendMessage(
								instance.prefix + "§cDer Spieler dieses JoinMe's hat diesen Server bereits verlassen.");
					}
				}
			} else {
				p.sendMessage(instance.prefix + "§cDieser Befehl existiert nicht.");
			}
		} else {
			cs.sendMessage(instance.sendausgabe("Allgemein.KeinSpieler"));
		}
	}
}