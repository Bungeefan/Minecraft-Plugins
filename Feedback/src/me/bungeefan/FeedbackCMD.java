package me.bungeefan;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedbackCMD implements CommandExecutor {

	private Feedback instance;

	public FeedbackCMD(Feedback instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cs instanceof Player) {
			Player p = (Player) cs;
			if (args.length >= 1) {
				if (instance.getConfig()
						.getLong("Feedback.Zeit." + p.getName()) <= (System.currentTimeMillis() - 86400000)) {
					String feedback = args[0];
					for (int i = 1; i < args.length; i++) {
						feedback = feedback + " " + args[i];
					}
					List<String> feedbacks = instance.getConfig().getStringList("Feedback.Feedbacks");
					feedbacks.add(p.getName() + ":" + feedback);
					instance.getConfig().set("Feedback.Feedbacks", feedbacks);
					instance.getConfig().set("Feedback.Zeit." + p.getName(), System.currentTimeMillis());
					instance.saveConfig();
					p.sendMessage(instance.prefix + "§aDanke für dein Feedback!");
					for (Player player : instance.getServer().getOnlinePlayers()) {
						if (player.hasPermission("feedback.show")) {
							player.sendMessage(instance.prefix + "§6" + p.getName() + "§e: §f" + feedback);
						}
					}
				} else {
					p.sendMessage(instance.prefix + "§cDu kannst nur einmal innerhalb 24h Feedback senden!");
				}
				return true;
			}
		}
		return false;
	}

}
