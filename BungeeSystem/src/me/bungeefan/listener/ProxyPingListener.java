package me.bungeefan.listener;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyPingListener implements Listener {

	private BungeeSystem instance;

	public ProxyPingListener(BungeeSystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onProxyPing(ProxyPingEvent e) {
		ServerPing ping = e.getResponse();
		Players players = ping.getPlayers();
		ping.setDescriptionComponent(new TextComponent(
				(instance.config.getString("MOTD.Line1") + "\n" + instance.config.getString("MOTD.Line2"))
						.replaceAll("&", "§")));
		players.setMax(instance.config.getInt("MaxPlayers"));

		if (!instance.config.getBoolean("RealOnlinePlayers")) {
			int fake = (int) (players.getOnline() * (double) (instance.config.getDouble("Fake") / 100.0));
			players.setOnline(players.getOnline() + fake);
		}
		ping.setPlayers(players);

		if (instance.wartung) {
			ping.setVersion(new Protocol(instance.config.getString("Wartung.Version"), Short.MAX_VALUE));
			// String versionmsg = "";
			// for (String s : instance.config.getStringList("Wartung.Version")) {
			// versionmsg += s + "\n";
			// }
			TextComponent txt = new TextComponent((instance.config.getString("Wartung.MOTD.Line1") + "\n"
					+ instance.config.getString("Wartung.MOTD.Line2")).replaceAll("&", "§"));
			//txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(versionmsg).create()));
			ping.setDescriptionComponent(txt);
		}

		if (instance.restart) {
			ping.setVersion(new Protocol(instance.config.getString("ServerRestart.Version"), Short.MAX_VALUE));
			ping.setDescriptionComponent(new TextComponent((instance.config.getString("ServerRestart.MOTD.Line1") + "\n"
					+ instance.config.getString("ServerRestart.MOTD.Line2")).replaceAll("&", "§")));
		}

		// ping.setFavicon(Favicon.create(ImageIO.read(new
		// File(BungeeSystem.plugin.getDataFolder().getPath(),
		// "server-icon-Riventus.png"))));
		// ping.setFavicon(Favicon.create(ImageIO.read(new
		// File(instance.getDataFolder().getPath(), "server-icon.png"))));

		e.setResponse(ping);
	}
}