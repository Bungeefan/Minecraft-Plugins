package me.bungeefan.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import me.bungeefan.LobbySystem;

public class WeatherLST implements Listener {

	public LobbySystem instance;

	public WeatherLST(LobbySystem instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		e.setCancelled(true);
	}

}
