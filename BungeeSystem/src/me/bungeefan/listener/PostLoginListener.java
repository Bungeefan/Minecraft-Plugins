package me.bungeefan.listener;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginListener implements Listener {

	private BungeeSystem instance;

	public PostLoginListener(BungeeSystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onLogin(PostLoginEvent e) {
		ProxiedPlayer p = (ProxiedPlayer) e.getPlayer();
		if (instance.messages.getBoolean("Login.LayoutEnabled")) {
			for (String msg : instance.messages.getStringList("Login.Layout")) {
				p.sendMessage(msg);
			}
		}
		if (instance.messages.getBoolean("Login.TitleEnabled")) {
			Title title = ProxyServer.getInstance().createTitle();
			TextComponent tc = new TextComponent(instance.messages.getString("Login.Title").replaceAll("&", "§"));
			TextComponent tc2 = new TextComponent(instance.messages.getString("Login.SubTitle").replaceAll("&", "§"));
			title.title(tc);
			title.subTitle(tc2);
			p.sendTitle(title);
		}
	}
}
