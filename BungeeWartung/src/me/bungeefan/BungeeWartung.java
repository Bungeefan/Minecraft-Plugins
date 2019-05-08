package me.bungeefan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Iterables;

import me.bungeefan.commands.WartungCMD;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class BungeeWartung extends Plugin implements Listener {

	private Collection<? extends ProxiedPlayer> onlineplayer;
	public boolean wartung;
	public boolean reload = true;
	public boolean autowartung;
	public int endhour = 0;
	public int endminute;
	public int hour;
	public int minute;
	public Calendar cal;
	public Calendar start;
	public Calendar end;
	public Configuration config;
	private static BungeeWartung instance;

	public static BungeeWartung get() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		loadDefaultFiles();
		registerCommands();
		ProxyServer.getInstance().getPluginManager().registerListener(this, this);
		wartung = config.getBoolean("BungeeWartung.Status");
		autowartung();
	}

	public void registerCommands() {
		ProxyServer.getInstance().getPluginManager().registerCommand(instance, new WartungCMD("wartung"));
	}

	public void loadDefaultFiles() {
		if (!getDataFolder().exists())
			getDataFolder().mkdir();

		File configfile = new File(getDataFolder(), "config.yml");
		if (!configfile.exists()) {
			try (InputStream in = getResourceAsStream("config.yml")) {
				Files.copy(in, configfile.toPath());
			} catch (IOException e) {
				System.out.println("Fehler");
			}
		}
		try {
			loadFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadFiles() throws IOException {
		config = ConfigurationProvider.getProvider(YamlConfiguration.class)
				.load(new File(getDataFolder(), "config.yml"));
	}

	public void saveFiles() throws IOException {
		ConfigurationProvider.getProvider(YamlConfiguration.class).save(config,
				new File(getDataFolder(), "config.yml"));
	}

	public void toggleWartung(boolean ein) throws IOException {
		if (ein) {
			ProxyServer.getInstance().broadcast(
					(config.getString("BungeeWartung.Prefix") + "§cDer Server befindet sich nun im Wartungsmodus!")
							.replaceAll("&", "§"));
			onlineplayer = ProxyServer.getInstance().getPlayers();
			for (int i = 0; i < onlineplayer.size(); i++) {
				ProxiedPlayer player = Iterables.get(onlineplayer, i);
				if (!player.hasPermission("bungeewartung.exempt")) {
					player.disconnect((config.getString("Kick.Line1").replaceAll("&", "§")) + "\n"
							+ (config.getString("Kick.Line2").replaceAll("&", "§")) + "\n"
							+ (config.getString("Kick.Line3").replaceAll("&", "§")) + "\n"
							+ (config.getString("Kick.Line4").replaceAll("&", "§")));
				}
			}
			wartung = true;
		} else {
			wartung = false;
			ProxyServer.getInstance().broadcast((config.getString("BungeeWartung.Prefix")
					+ "§cDer Server befindet sich nun nicht mehr im Wartungsmodus!").replaceAll("&", "§"));
		}
		config.set("BungeeWartung.Status", wartung);
		saveFiles();
	}

	@EventHandler
	public void onLogin2(LoginEvent e) {
		if (wartung ){//&& (!e.getPlayer().hasPermission("bungeewartung.exempt"))) {
			e.setCancelled(true);
		e.setCancelReason(((config.getString("Kick.Line1")) + "\n" + (config.getString("Kick.Line2")) + "\n"
				+ (config.getString("Kick.Line3")) + "\n" + (config.getString("Kick.Line4")))
				.replaceAll("&", "§"));
			/*e.getPlayer()
					.disconnect(((config.getString("Kick.Line1")) + "\n" + (config.getString("Kick.Line2")) + "\n"
							+ (config.getString("Kick.Line3")) + "\n" + (config.getString("Kick.Line4")))
									.replaceAll("&", "§"));*/
		}// else if (wartung && (e.getPlayer().hasPermission("bungeewartung.exempt")
			//	&& config.getBoolean("BungeeWartung.JoinMSGEnabled"))) {
			//ProxyServer.getInstance()
		//			.broadcast(((config.getString("BungeeWartung.Prefix") + config.getString("BungeeWartung.Join"))
		//					.replaceAll("&", "§")).replace("[Player]", e.getPlayer().getDisplayName()));
		//}
	}
	
	/*@EventHandler
	public void onLogin(ServerConnectEvent e) {
		if (wartung && (!e.getPlayer().hasPermission("bungeewartung.exempt"))) {
			e.setCancelled(true);
			e.getPlayer()
					.disconnect(((config.getString("Kick.Line1")) + "\n" + (config.getString("Kick.Line2")) + "\n"
							+ (config.getString("Kick.Line3")) + "\n" + (config.getString("Kick.Line4")))
									.replaceAll("&", "§"));
		} else if (wartung && (e.getPlayer().hasPermission("bungeewartung.exempt")
				&& config.getBoolean("BungeeWartung.JoinMSGEnabled"))) {
			ProxyServer.getInstance()
					.broadcast(((config.getString("BungeeWartung.Prefix") + config.getString("BungeeWartung.Join"))
							.replaceAll("&", "§")).replace("[Player]", e.getPlayer().getDisplayName()));
		}
	}*/

	@EventHandler
	public void onKick(ServerKickEvent e) {
		if (wartung && config.getBoolean("BungeeWartung.KickMSGEnabled")) {
			e.setKickReason(((config.getString("BungeeWartung.Prefix") + config.getString("BungeeWartung.Kick"))
					.replaceAll("&", "§")).replace("[Player]", e.getPlayer().getDisplayName()));
		}
	}

	@EventHandler
	public void onLeave(ServerDisconnectEvent e) {
		if (wartung && config.getBoolean("BungeeWartung.LeaveMSGEnabled")) {
			ProxyServer.getInstance()
					.broadcast(((config.getString("BungeeWartung.Prefix") + config.getString("BungeeWartung.Leave"))
							.replaceAll("&", "§")).replace("[Player]", e.getPlayer().getDisplayName()));
		}
	}

	@EventHandler
	public void onPing(ProxyPingEvent e) {
		if (wartung) {
			ServerPing ping = e.getResponse();
			ping.setDescription(
					(config.getString("MOTD.Line1") + "\n" + config.getString("MOTD.Line2")).replaceAll("&", "§"));
			int online;
			if (config.getBoolean("BungeeWartung.RealOnlinePlayersActivated")) {
				online = ProxyServer.getInstance().getPlayers().size();
			} else {
				online = config.getInt("BungeeWartung.RealOnlinePlayers");
			}
			int max = config.getInt("BungeeWartung.MaxPlayers");
			Players players = new Players(max, online, null);
			ping.setPlayers(players);
			ping.setVersion(new Protocol(config.getString("BungeeWartung.Version"), 2));
			e.setResponse(ping);
		}
	}

	public void autowartung() {
		long seconds = 0;
		cal = Calendar.getInstance();
		if (cal.get(Calendar.SECOND) > 0) {
			seconds = 60 - cal.get(Calendar.SECOND);
		}
		ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {

			@Override
			public void run() {
				cal = Calendar.getInstance();
				if (reload) {
					reload = false;
					autowartung = config.getBoolean("AutoWartung.aktiviert");
					String uhrzeit = config.getString("AutoWartung.Uhrzeit");
					String enduhrzeit = config.getString("AutoWartung.EndUhrzeit");
					String[] split = uhrzeit.split(":");
					String[] endsplit = enduhrzeit.split(":");
					hour = Integer.valueOf(split[0]);
					minute = Integer.valueOf(split[1]);
					start = Calendar.getInstance();
					start.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), hour, minute);
					endhour = Integer.valueOf(endsplit[0]);
					endminute = Integer.valueOf(endsplit[1]);
					end = Calendar.getInstance();
					end.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), endhour,
							endminute);
				}
				if (autowartung) {
					if (!wartung && cal.compareTo(start) >= 0 && cal.compareTo(end) < 0) {
						try {
							toggleWartung(true);
						} catch (IOException e) {
							System.out.println("§4Config Fehler");
						}
					}
					if (wartung && cal.compareTo(end) == 0) {
						try {
							toggleWartung(false);
						} catch (IOException e) {
							System.out.println("§4Config Fehler");
						}
					}
				}
			}
		}, seconds, 60, TimeUnit.SECONDS);
	}

	public String sendausgabe(String message) {
		return (config.getString("BungeeWartung.Prefix") + (config.getString(message))).replaceAll("&", "§");
	}

}
