package me.bungeefan;

import org.bukkit.plugin.java.JavaPlugin;

public class Spawn extends JavaPlugin {

	private static Spawn instance;

	public void onEnable() {
		instance = this;
		loadConfig();
		saveConfig();
		getCommand("setspawn").setExecutor(new SetSpawnCMD());
		getCommand("spawn").setExecutor(new SpawnCMD());
	}

	public static Spawn get() {
		return instance;
	}

	private void loadConfig() {
		getConfig().options().header("Hier kannst du alles ändern.");
		getConfig().addDefault("spawn.welt", "world");
		getConfig().addDefault("spawn.x", " ");
		getConfig().addDefault("spawn.y", " ");
		getConfig().addDefault("spawn.z", " ");
		getConfig().addDefault("Spawn.Prefix", "&b[Spawn]&r ");
		getConfig().addDefault("Spawn.Kein Recht", "&cDu hast keine Rechte um diesen Command ausführen!");

		getConfig().addDefault("Spawn.Kein Spieler", "&cDu musst diesen Command als Spieler ausführen!");
		getConfig().addDefault("Spawn.Nicht gefunden", "&cSpieler nicht gefunden!");
		getConfig().addDefault("Spawn.Fehler", "&cFehler! Bitte versuche es erneut.");
		getConfig().addDefault("Spawn.Spawn", "&aDu bist jetzt am Spawn!");
		getConfig().addDefault("Spawn.Gesetzt", "&aSpawn wurde erfolgreich gesetzt!");
		getConfig().addDefault("Spawn.Teleportiert", "&aDu wurdest zum Spawn teleportiert!");
		getConfig().addDefault("Spawn.Teleportiert2", "&aDu hast&r &9[Player]&r &azum Spawn teleportiert!");
		getConfig().addDefault("Spawn.Alle Teleportiert", "&9[Player]&r &ahat alle Spieler zum Spawn teleportiert!");

		getConfig().options().copyDefaults(true);

		saveConfig();
		reloadConfig();
	}
}
