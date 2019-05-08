package me.bungeefan;

import java.io.File;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Iterables;

public class Reset extends JavaPlugin implements Listener {

	public Collection<? extends Player> onlineplayer;

	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
	}

	@EventHandler
	public void onKill(PlayerDeathEvent e) {

		System.out.println(ChatColor.RED + "Ein Spieler ist gestorben!");
		onlineplayer = Bukkit.getOnlinePlayers();

		for (int i = 0; i < onlineplayer.size(); i++) {
			Player player = Iterables.get(onlineplayer, i);
			player.kickPlayer(ChatColor.GOLD + e.getEntity().getName() + " ist gestorben!\n" + "\n" + "§cDie Welt wird resettet!");
			
		}
		World world = e.getEntity().getWorld();
		Bukkit.unloadWorld(world, false);
		if (delete(new File(world.getName()))) {
			WorldCreator wc = new WorldCreator(world.getName());
			wc.environment(World.Environment.NORMAL);
			wc.createWorld();
			Bukkit.shutdown();
		}
	}

	private boolean delete(File file) {
		if (file.isDirectory()) {
			File[] arrayOfFile;
			int j = (arrayOfFile = file.listFiles()).length;
			for (int i = 0; i < j; i++) {
				File subfile = arrayOfFile[i];
				if (!delete(subfile)) {
					return false;
				}
			}
		}
		if (!file.delete()) {
			return false;
		}
		return true;
	}
}