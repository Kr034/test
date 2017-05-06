package fr.kro.race;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		getCommand("mage").setExecutor(new magecmd());
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		if (Mage.enable) {
			Bukkit.getServer().getPluginManager().registerEvents(new Mage(), this);
			Mage.initMage();
		}
	}

	@Override
	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.kickPlayer("Server shutdown !");
		}
	}
}
