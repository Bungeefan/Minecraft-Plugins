package me.bungeefan.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.bungeefan.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerDisconnectListener implements Listener {

	private BungeeSystem instance;

	public PlayerDisconnectListener(BungeeSystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent e) {
		String lobby = instance.messages.getString("Allgemein.LobbyServer");
		ProxiedPlayer p = (ProxiedPlayer) e.getPlayer();

		instance.messagecd.remove(p);
		instance.msgtoggle.remove(p);
		instance.notifytoggle.remove(p);
		instance.reportcd.remove(p);
		instance.joinMe.remove(p);
		instance.messager.remove(p);
		p.setReconnectServer(ProxyServer.getInstance().getServerInfo(lobby));

		// if (instance.reports.containsKey(p)) {
		// ProxiedPlayer reporter = instance.reports.get(p).getReportingPlayer();
		// ProxiedPlayer reporter2 =
		// BungeeCord.getInstance().getPlayer(reporter.getUniqueId());
		// instance.reports.remove(p);
		// String prefix = instance.prefix;
		// if (reporter2 != null) {
		// if
		// (instance.playerHostname.containsKey(reporter2.getUniqueId())) {
		// if
		// (instance.playerHostname.get(reporter2.getUniqueId()).equalsIgnoreCase("LightningPvP"))
		// {
		// prefix = instance.prefix.replaceAll("Riventus", "LightningPvP");
		// }
		// if
		// (instance.playerHostname.get(reporter2.getUniqueId()).equalsIgnoreCase("SevenPvP"))
		// {
		// prefix = instance.prefix.replaceAll("Riventus", "SevenPvP");
		// }
		// }
		// }
		// for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
		// if (players.hasPermission("bs.moderator") |
		// players.hasPermission("bs.srmoderator")
		// | players.hasPermission("bs.administrator")) {
		// players.sendMessage(
		// "§8§l §c §8 §3Der §cReport §3bezüglich " + p.getDisplayName() + " §3wurde
		// §cgelöscht§3.");
		// }
		// }
		// if (reporter2 != null) {
		// reporter2.sendMessage(
		// prefix + "§3Dein §cReport §3bezüglich " + p.getDisplayName() + " §3wurde
		// §cgelöscht§3.");
		// }
		// }

		// ByteArrayDataOutput out = ByteStreams.newDataOutput();
		// out.writeUTF(p.getUniqueId().toString());
		//
		// for (String servers : ProxyServer.getInstance().getServers().keySet()) {
		// ServerInfo server = ProxyServer.getInstance().getServerInfo(servers);
		// if (server.getName().equalsIgnoreCase("Lobby")) {
		// server.sendData("LabyMod", out.toByteArray());
		// }
		// }
	}
}