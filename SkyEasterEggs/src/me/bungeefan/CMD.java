package me.bungeefan;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD implements CommandExecutor {

	private SkyEasterEggs instance;

	public CMD(SkyEasterEggs instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cs instanceof Player) {
			int coins = instance.getConfig().getInt("Coins");
			instance.econ.bankDeposit(cs.getName(), coins);
			// instance.econ.depositPlayer(((Player) cs).getPlayer(), coins);
			cs.sendMessage(instance.getConfig().getString("CoinsMSG").replaceAll("&", "§").replaceAll("%COINS",
					String.valueOf(coins)));
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				p.sendMessage(instance.getConfig().getString("ALLMSG").replaceAll("&", "§").replaceAll("%PLAYER%",
						cs.getName()));
			}
		}
		return true;
	}

}
