package me.bungeefan;

import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardChanger extends JavaPlugin implements Listener {

	private Collection<? extends Player> onlineplayer;

	public void onEnable() {
		loadConfig();
		saveConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("board")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("update")) {
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
						@Override
						public void run() {
							onlineplayer = Bukkit.getOnlinePlayers();
							for (Player p : onlineplayer) {
								updateScoreboard(p);
							}
						}
					});
					cs.sendMessage(sendausgabe2("§aScoreboard wurde geupdated"));
				}
			}
			return true;
		}
		return false;
	}

	public void updateScoreboard(Player p) {
		List<String> zeilen = getConfig().getStringList("Scoreboard.Zeilen");
		int anzahl = zeilen.size();
		int j = anzahl;
		List<String> rangrechte = getConfig().getStringList("Scoreboard.RangRechte");
		String rang = getConfig().getString("Scoreboard.DefaultName").replaceAll("&", "§");
		for (int i = 0; i < rangrechte.size(); i++) {
			String perm = rangrechte.get(i);
			if (p.hasPermission(perm)) {
				rang = perm.substring(6);
			}
		}
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("a", "b");
		obj.setDisplayName(getConfig().getString("Scoreboard.DisplayName").replaceAll("&", "§"));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		for (int i = 0; i < anzahl; i++) {
			String zeile = zeilen.get(i).replaceAll("&", "§");
			zeile = zeile.replace("%Rang%", rang);
			zeile = zeile.replace("%Online%", String.valueOf(Bukkit.getOnlinePlayers().size()));
			zeile = zeile.replace("%Max%", String.valueOf(Bukkit.getMaxPlayers()));
			System.out.println(zeile);
			Score score = obj.getScore(zeile);
			score.setScore(j);
			j--;
		}
		p.setScoreboard(board);
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				onlineplayer = Bukkit.getOnlinePlayers();
				for (Player p : onlineplayer) {
					updateScoreboard(p);
				}
			}
		});
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				onlineplayer = Bukkit.getOnlinePlayers();
				for (Player p : onlineplayer) {
					updateScoreboard(p);
				}
			}
		});
	}

	public String sendausgabe(String message) {
		return (getConfig().getString("Scoreboard.Prefix") + (getConfig().getString(message))).replaceAll("&", "§");
	}

	public String sendausgabe2(String message) {
		return (getConfig().getString("Scoreboard.Prefix").replaceAll("&", "§") + message);
	}

	private void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();
	}

}
