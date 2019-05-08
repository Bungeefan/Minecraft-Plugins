package me.bungeefan;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FeedbacksCMD implements CommandExecutor {

	private Feedback instance;

	public FeedbacksCMD(Feedback instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (cs.hasPermission("feedback.show")) {
			if (args.length == 0) {
				List<String> feedbacks = instance.getConfig().getStringList("Feedback.Feedbacks");
				if (!feedbacks.isEmpty()) {
					for (int i = 0; i < feedbacks.size(); i++) {
						String[] split = feedbacks.get(i).split(":");
						cs.sendMessage(instance.prefix + "§6" + split[0] + "§e: §f" + split[1]);
					}
				} else {
					cs.sendMessage(instance.prefix + "§cEs wurde noch kein Feedback gesendet!");
				}
			} else {
				return false;
			}
		}
		return true;
	}
}
