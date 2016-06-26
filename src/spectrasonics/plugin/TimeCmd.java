package spectrasonics.plugin;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeCmd extends JavaPlugin {

	private TimeCmd plugin;
	private static FileConfiguration conf;

	@Override
	public void onEnable() {

		this.plugin = this;
		saveDefaultConfig();
		TimeCmd.conf = getConfig();
		Set<String> worlds = conf.getKeys(false);

		for (String world : worlds) {
			ConfigurationSection commandConf = conf.getConfigurationSection(world + ".commands");

			new BukkitRunnable() {
				@Override
				public void run() {
					long nowTime = plugin.getServer().getWorld(world).getTime();
					for (String key : commandConf.getKeys(false)) {
						long time = Long.parseLong(key);
						long abs = Math.abs(time-nowTime);
						if (abs < 10L) {
							for (String cmd : commandConf.getStringList(key)) {
								plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&', cmd));
							}
						}
					}
				}
			}.runTaskTimer(this.plugin, 0, 20);
		}
	}
}
